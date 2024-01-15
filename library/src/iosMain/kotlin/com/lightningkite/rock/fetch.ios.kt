package com.lightningkite.rock

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.cinterop.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import platform.Foundation.*
import platform.UniformTypeIdentifiers.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy
import kotlin.coroutines.resume

val client = HttpClient {
    install(WebSockets)
}

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
): RequestResponse {
    try {
        val response = client.request(url) {
            this.method = when (method) {
                HttpMethod.GET -> io.ktor.http.HttpMethod.Get
                HttpMethod.POST -> io.ktor.http.HttpMethod.Post
                HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
                HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
                HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
            }
            this.headers {
                for ((key, values) in headers.map) {
                    for (value in values) append(key, value)
                }
            }
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
        }
        backToMainThread()
        return RequestResponse(response)
    } catch (e: Exception) {
        backToMainThread()
        throw e
    }
}

suspend fun backToMainThread() {
    suspendCoroutineCancellable<Unit> {
        dispatch_async(queue = dispatch_get_main_queue(), block = {
            it.resume(Unit)
        })
        return@suspendCoroutineCancellable {}
    }
    assertMainThread()
}

actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders =
    HttpHeaders(map.entries.associateTo(HashMap()) { it.key.lowercase() to listOf(it.value) })

actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders = HttpHeaders(headers.map.toMutableMap())
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders =
    HttpHeaders(list.groupBy { it.first.lowercase() }.mapValues { it.value.map { it.second } }.toMutableMap())

actual class HttpHeaders(val map: MutableMap<String, List<String>>) {
    actual fun append(name: String, value: String): Unit {
        map[name.lowercase()] = (map[name.lowercase()] ?: listOf()) + value
    }

    actual fun delete(name: String): Unit {
        map.remove(name.lowercase())
    }

    actual fun get(name: String): String? = map[name.lowercase()]?.joinToString(",")
    actual fun has(name: String): Boolean = map.containsKey(name.lowercase())
    actual fun set(name: String, value: String): Unit {
        map[name.lowercase()] = listOf(value)
    }
}

actual class RequestResponse(val wraps: HttpResponse) {
    actual val status: Short get() = wraps.status.value.toShort()
    actual val ok: Boolean get() = wraps.status.isSuccess()
    actual suspend fun text(): String {
        try {
            val result = wraps.bodyAsText()
            backToMainThread()
            return result
        } catch (e: Exception) {
            backToMainThread()
            throw e
        }
    }

    actual suspend fun blob(): Blob {
        try {
            val result = wraps.body<ByteArray>()
                .let { Blob(it.toNSData(), wraps.contentType()?.toString() ?: "application/octet-stream") }
            backToMainThread()
            return result
        } catch (e: Exception) {
            backToMainThread()
            throw e
        }
    }
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
        launchGlobal {
            client.webSocket(url) {
                dispatch_async(queue = dispatch_get_main_queue(), block = {
                    onOpen.forEach { it() }
                })
                launch {
                    while (stayOn) {
                        send(sending.receive())
                    }
                }
                launch {
                    this@WebSocketWrapper.closeReason.receive().let { reason ->
                        close(reason)
                        dispatch_async(queue = dispatch_get_main_queue(), block = {
                            onClose.forEach { it(reason.code) }
                        })
                    }
                }
                var reason: CloseReason? = null
                while (stayOn) {
                    when (val x = incoming.receive()) {
                        is Frame.Binary -> {
                            val data = Blob(x.data.toNSData())
                            dispatch_async(queue = dispatch_get_main_queue(), block = {
                                onBinaryMessage.forEach { it(data) }
                            })
                        }

                        is Frame.Text -> {
                            val text = x.readText()
                            dispatch_async(queue = dispatch_get_main_queue(), block = {
                                onMessage.forEach { it(text) }
                            })
                        }

                        is Frame.Close -> {
                            reason = x.readReason()
                            break
                        }

                        else -> {}
                    }
                }
                dispatch_async(queue = dispatch_get_main_queue(), block = {
                    onClose.forEach { it(reason?.code ?: 0) }
                })
            }
        }
    }

    override fun close(code: Short, reason: String) {
        stayOn = false
        launchGlobal {
            closeReason.send(CloseReason(code, reason))
            closeReason.close()
            sending.close()
        }
    }

    override fun send(data: String) {
        launchGlobal { sending.send(Frame.Text(data)) }
    }

    override fun send(data: Blob) {
        launchGlobal { sending.send(Frame.Binary(false, data.data.toByteArray())) }
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