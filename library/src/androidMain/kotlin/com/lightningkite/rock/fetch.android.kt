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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

val client: HttpClient get() {
    return AndroidAppContext.ktorClient
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
                    setBody(body.content.data)
                }

                is RequestBodyFile -> {
                    val length =
                        AndroidAppContext.applicationCtx.contentResolver.openAssetFileDescriptor(body.content.uri, "r")
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
        backToMainThread()
        return RequestResponse(response)
    } catch(e: Exception) {
        backToMainThread()
        throw e
    }
}

suspend fun backToMainThread() {
    suspendCoroutineCancellable<Unit> {
        globalPost {
            it.resume(Unit)
        }
        return@suspendCoroutineCancellable {}
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
            val result = wraps.bodyAsText()
            backToMainThread()
            return result
        } catch(e: Exception) {
            backToMainThread()
            throw e
        }
    }

    actual suspend fun blob(): Blob {
        try {
            val result = wraps.body<ByteArray>()
                .let { Blob(it, wraps.contentType()?.toString() ?: "application/octet-stream") }
            backToMainThread()
            return result
        } catch(e: Exception) {
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
    val onClose = ArrayList<(Short) -> Unit>()
    val onMessage = ArrayList<(String) -> Unit>()
    val onBinaryMessage = ArrayList<(Blob) -> Unit>()

    init {
        launchGlobal {
            client.webSocket(url) {
                globalPost {
                    onOpen.forEach { it() }
                }
                launch {
                    while (stayOn) {
                        send(sending.receive())
                    }
                }
                launch {
                    this@WebSocketWrapper.closeReason.receive().let { reason ->
                        close(reason)
                        globalPost {
                            onClose.forEach { it(reason.code) }
                        }
                    }
                }
                var reason: CloseReason? = null
                while (stayOn) {
                    when (val x = incoming.receive()) {
                        is Frame.Binary -> {
                            val data = Blob(x.data, "application/octet-stream")
                            globalPost {
                                onBinaryMessage.forEach { it(data) }
                            }
                        }

                        is Frame.Text -> {
                            val text = x.readText()
                            globalPost {
                                onMessage.forEach { it(text) }
                            }
                        }

                        is Frame.Close -> {
                            reason = x.readReason()
                            break
                        }

                        else -> {}
                    }
                }
                globalPost {
                    onClose.forEach { it(reason?.code ?: 0) }
                }
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
        launchGlobal { sending.send(Frame.Binary(false, data.data)) }
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