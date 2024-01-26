package com.lightningkite.rock

import kotlinx.browser.window
import org.w3c.dom.CloseEvent
import org.w3c.dom.MessageEvent
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.files.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UnsafeCastFromDynamic")
actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?,
): RequestResponse {
    val a: dynamic = js("new AbortController()")
    val o = js("({})")
    o.method = method.name
    o.body = when (body) {
        null -> undefined
        is RequestBodyBlob -> {
            headers.append("Content-Type", body.content.type)
            body.content
        }

        is RequestBodyFile -> {
            headers.append("Content-Type", body.content.type)
            body.content
        }

        is RequestBodyText -> {
            headers.append("Content-Type", body.type)
            body.content
        }

        else -> throw NotImplementedError()
    }
    o.headers = headers
    o.signal = a.signal
    val promise = window.fetch(url, o as RequestInit)
    return suspendCoroutineCancellable { cont ->
        promise.then(
            onFulfilled = {
                cont.resume(RequestResponse(it))
            },
            onRejected = {
                cont.resumeWithException(it)
            }
        )
        return@suspendCoroutineCancellable { a.abort(); Unit }
    }
}

actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders = HttpHeaders().apply {
    for (entry in map) {
        append(entry.key, entry.value)
    }
}

actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders = HttpHeaders(init = headers)
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders = HttpHeaders().apply {
    for (entry in list) {
        append(entry.first, entry.second)
    }
}
actual typealias HttpHeaders = Headers

actual class RequestResponse(val wraps: Response) {
    actual val status: Short get() = wraps.status
    actual val ok: Boolean get() = wraps.ok
    actual suspend fun text(): String = wraps.text().await()
    actual suspend fun blob(): Blob = wraps.blob().await()
    actual suspend fun headers(): Map<String, List<String>> {
        return buildMap {
            val keys = wraps.headers.asDynamic().keys()
            var nextKey: dynamic
            do {
                nextKey = keys.next()
                if (nextKey.value != null && nextKey.value != undefined) {
                    val nextValue: String = wraps.headers.asDynamic().get(nextKey.value).unsafeCast<String>()
                    val temp = this[nextKey.value] ?: emptyList()
                    this[nextKey.value] = temp + nextValue
                }
            } while (!nextKey.done)
        }
    }
}

actual typealias Blob = org.w3c.files.Blob
actual typealias FileReference = File


actual fun FileReference.mimeType(): String {
    return this.type
}

actual fun FileReference.fileName(): String {
    return this.name
}

actual fun websocket(url: String): WebSocket {
    return WebSocketWrapper(org.w3c.dom.WebSocket(url))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
class WebSocketWrapper(val native: org.w3c.dom.WebSocket) : WebSocket {
    override fun close(code: Short, reason: String) = native.close(code, reason)
    override fun send(data: String) = native.send(data)
    override fun send(data: Blob) = native.send(data)
    override fun onOpen(action: () -> Unit) {
        native.addEventListener("open", { action() })
    }

    override fun onMessage(action: (String) -> Unit) {
        native.addEventListener("message", { it as MessageEvent; (it.data as? String)?.let { action(it) } })
    }

    override fun onBinaryMessage(action: (Blob) -> Unit) {
        native.addEventListener("message", { it as MessageEvent; (it.data as? Blob)?.let { action(it) } })
    }

    override fun onClose(action: (Short) -> Unit) {
        native.addEventListener("close", { action((it as CloseEvent).code) })
    }
}