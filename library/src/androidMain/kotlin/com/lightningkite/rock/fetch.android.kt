@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock

import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.lightningkite.rock.views.AndroidAppContext
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

val client: HttpClient
    get() {
        return AndroidAppContext.ktorClient
    }

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
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
                    }
                    this.headers {
                        for ((key, values) in headers.map) {
                            for (value in values) append(key, value)
                        }
                    }
                    when (body) {
                        is RequestBodyBlob -> {
                            contentType(ContentType.parse(body.content.type))
                            setBody(body.content.data)
                        }

                        is RequestBodyFile -> {
                            val length =
                                AndroidAppContext.applicationCtx.contentResolver.openAssetFileDescriptor(
                                    body.content.uri,
                                    "r"
                                )
                                    ?.use {
                                        it.length
                                    } ?: -1L
                            val type = AndroidAppContext.applicationCtx.contentResolver.getType(body.content.uri)
                                ?: "application/octet-stream"
                            contentType(ContentType.parse(type))
                            setBody(
                                AndroidAppContext.applicationCtx.contentResolver.openInputStream(body.content.uri)!!
                                    .toByteReadChannel()
                            )
                        }

                        is RequestBodyText -> {
                            contentType(ContentType.parse(body.type))
                            setBody(body.content)
                        }

                        null -> {}
                    }
                }
            }
            RequestResponse(response)
        } catch (e: Exception) {
            throw e
        }
    }
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
                        .let { Blob(it, wraps.contentType()?.toString() ?: "application/octet-stream") }
                }
            }
            return result
        } catch (e: Exception) {
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
                                val data = Blob(x.data, "application/octet-stream")
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
        sending.trySend(Frame.Binary(false, data.data))
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

actual class FileReference(val uri: Uri)
actual class Blob(val data: ByteArray, val type: String)

val webSocketClient: HttpClient by lazy {
    HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 20_000
        }
    }
}