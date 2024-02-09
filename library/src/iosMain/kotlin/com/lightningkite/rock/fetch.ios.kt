package com.lightningkite.rock

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import platform.Foundation.*
import platform.UniformTypeIdentifiers.*
import platform.posix.memcpy

val client = HttpClient {
    install(WebSockets)
}

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?,
    onUploadProgress: ((bytesComplete: Int, bytesExpectedOrNegativeOne: Int) -> Unit)?,
    onDownloadProgress: ((bytesComplete: Int, bytesExpectedOrNegativeOne: Int) -> Unit)?,
): RequestResponse {
    return withContext(Dispatchers.Main) {
        try {
            val response = withContext(Dispatchers.IO) {
                client.request(url) {
                    this.method = when (method) {
                        HttpMethod.GET -> io.ktor.http.HttpMethod.Get
                        HttpMethod.POST -> io.ktor.http.HttpMethod.Post
                        HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
                        HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
                        HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
                        HttpMethod.HEAD -> io.ktor.http.HttpMethod.Head
                    }
                    this.headers { appendAll(headers) }
                    when (body) {
                        is RequestBodyBlob -> {
                            contentType(ContentType.parse(body.content.type))
                            setBody(body.content.data.toByteArray())
                        }

                        is RequestBodyFile -> {
                            val mime = body.content.suggestedType
                                ?: (body.content.provider.registeredContentTypes.firstOrNull() as? UTType ?: UTTypeData)
                            contentType(ContentType.parse(mime.preferredMIMEType!!))
                            body.content.provider.loadDataRepresentationForContentType(mime) { data, error ->
                                if (error != null) throw Exception(error?.description)
                                setBody(data!!.toByteArray())
                            }
                        }

                        is RequestBodyText -> {
                            contentType(ContentType.parse(body.type))
                            setBody(body.content)
                        }

                        null -> {}
                    }
                    onUploadProgress?.let {
                        onUpload { a, b -> it(a.toInt(), b.toInt()) }
                    }
                    onDownloadProgress?.let {
                        onDownload { a, b -> it(a.toInt(), b.toInt()) }
                    }
                }
            }

            RequestResponse(response)
        } catch (e: Exception) {
            throw e
        }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias HttpHeaders = Headers

actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders = Headers.build { map.forEach { append(it.key,  it.value) } }
actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders = Headers.build { appendAll(headers) }
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders = Headers.build { list.forEach { append(it.first, it.second) } }
actual inline fun httpHeaders(sequence: Sequence<Pair<String, String>>): HttpHeaders = Headers.build { sequence.forEach { append(it.first, it.second) } }

actual class RequestResponse(val wraps: HttpResponse) {
    actual val status: Short get() = wraps.status.value.toShort()
    actual val ok: Boolean get() = wraps.status.isSuccess()
    actual suspend fun text(): String {
        try {
            val result = withContext(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    wraps.bodyAsText()
                }
            }
            return result
        } catch (e: Exception) {
            throw e
        }
    }

    actual suspend fun blob(): Blob {
        try {
            val result = withContext(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    wraps.body<ByteArray>()
                        .let { Blob(it.toNSData(), wraps.contentType()?.toString() ?: "application/octet-stream") }
                }
            }
            return result
        } catch (e: Exception) {
            throw e
        }
    }

    actual val headers: HttpHeaders get() = wraps.headers
}

actual fun websocket(url: String): WebSocket {
    return WebSocketWrapper(url)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
class WebSocketWrapper(val url: String) : WebSocket {
    val closeReason = Channel<CloseReason>()
    val sending = Channel<Frame>(10)
    var stayOn = true
    val onOpen = ArrayList<() -> Unit>()

    init {
        onOpen.add { assertMainThread() }
    }

    val onClose = ArrayList<(Short) -> Unit>()

    init {
        onClose.add { assertMainThread() }
    }

    val onMessage = ArrayList<(String) -> Unit>()

    init {
        onMessage.add { assertMainThread() }
    }

    val onBinaryMessage = ArrayList<(Blob) -> Unit>()

    init {
        onBinaryMessage.add { assertMainThread() }
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            client.webSocket(url) {
                withContext(Dispatchers.Main) {
                    onOpen.forEach { it() }
                }
                launch {
                    try {
                        while (stayOn) {
                            send(sending.receive())
                        }
                    } catch (e: ClosedReceiveChannelException) {
                    }
                }
                launch {
                    try {
                        this@WebSocketWrapper.closeReason.receive().let { reason ->
                            close(reason)
                            withContext(Dispatchers.Main) {
                                onClose.forEach { it(reason.code) }
                            }
                        }
                    } catch (e: ClosedReceiveChannelException) {
                    }
                }
                var reason: CloseReason? = null
                while (stayOn) {
                    try {
                        when (val x = incoming.receive()) {
                            is Frame.Binary -> {
                                val data = Blob(x.data.toNSData())
                                withContext(Dispatchers.Main) {
                                    onBinaryMessage.forEach { it(data) }
                                }
                            }

                            is Frame.Text -> {
                                val text = x.readText()
                                withContext(Dispatchers.Main) {
                                    onMessage.forEach { it(text) }
                                }
                            }

                            is Frame.Close -> {
                                reason = x.readReason()
                                break
                            }

                            else -> {}
                        }
                    } catch (e: ClosedReceiveChannelException) {
                    }
                }
                withContext(Dispatchers.Main) {
                    onClose.forEach { it(reason?.code ?: 0) }
                }
            }
        }
    }

    override fun close(code: Short, reason: String) {
        stayOn = false
        closeReason.trySend(CloseReason(code, reason))
    }

    override fun send(data: String) {
        sending.trySend(Frame.Text(data))
    }

    override fun send(data: Blob) {
        sending.trySend(Frame.Binary(false, data.data.toByteArray()))
    }

    override fun onOpen(action: () -> Unit) {
        onOpen.add(action)
    }

    override fun onMessage(action: (String) -> Unit) {
        onMessage.add(action)
    }

    override fun onBinaryMessage(action: (Blob) -> Unit) {
        onBinaryMessage.add(action)
    }

    override fun onClose(action: (Short) -> Unit) {
        onClose.add(action)
    }
}

actual class Blob(val data: NSData, val type: String = "application/octet-stream")
actual class FileReference(val provider: NSItemProvider, val suggestedType: UTType? = null)


actual fun FileReference.mimeType(): String {
    TODO()
}
actual fun FileReference.fileName(): String {
    TODO()
}

fun String.nsdata(): NSData? =
    NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

fun NSData.string(): String? =
    NSString.create(data = this, encoding = NSUTF8StringEncoding)?.toString()

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong()
    )
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
}