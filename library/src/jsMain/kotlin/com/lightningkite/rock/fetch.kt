package com.lightningkite.rock

import kotlinx.browser.window
import org.w3c.dom.CloseEvent
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.files.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UnsafeCastFromDynamic")
actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
): RequestResponse {
    val a: dynamic = js("new AbortController()")
    val o = js("({})")
    o.method = method.name
    o.body = when(body) {
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

    return suspendCancellableCoroutine { cont ->
        println("Fetch: Inside suspendCoroutineCancellable")
        var resumed = false
        promise.then(
            onFulfilled = {
                if(resumed) return@then
                resumed = true
                println("Fetch: OnFulfilled")
                cont.resume(RequestResponse(it))
            },
            onRejected = {
                if(resumed) return@then
                resumed = true
                println("Fetch: OnRejected")
                cont.resumeWithException(it)
            }
        )
        cont.invokeOnCancellation {
            if(resumed) return@invokeOnCancellation
            resumed = true
            println("Fetch: Cancelled")
            a.abort(); Unit
        }
    }
}
actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders = HttpHeaders().apply {
    for(entry in map) {
        append(entry.key, entry.value)
    }
}
actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders = HttpHeaders(init = headers)
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders = HttpHeaders().apply {
    for(entry in list) {
        append(entry.first, entry.second)
    }
}
actual typealias HttpHeaders = Headers
actual class RequestResponse(val wraps: Response) {
    actual val status: Short get() = wraps.status
    actual val ok: Boolean get() = wraps.ok
    actual suspend fun text(): String {
        println("text(): Getting Text Promise")
        val textPromise = wraps.text()
        println("text(): Have Text Promise")
        val textResult = textPromise.await()
        println("text(): Have Text Result: $textResult")
        return textResult
    }
    actual suspend fun blob(): Blob = wraps.blob().await()
}

actual typealias Blob = org.w3c.files.Blob
actual typealias FileReference = File

actual fun websocket(url: String): WebSocket {
    return WebSocketWrapper(org.w3c.dom.WebSocket(url))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
class WebSocketWrapper(val native: org.w3c.dom.WebSocket): WebSocket {
    override fun close(code: Short, reason: String) = native.close(code, reason)
    override fun send(data: String) = native.send(data)
    override fun send(data: Blob) = native.send(data)
    override fun onOpen(action: ()->Unit) { native.addEventListener("open", { action() }) }
    override fun onMessage(action: (String)->Unit) { native.addEventListener("message", { it as MessageEvent; (it.data as? String)?.let { action(it) } }) }
    override fun onBinaryMessage(action: (Blob)->Unit) { native.addEventListener("message", { it as MessageEvent; (it.data as? Blob)?.let { action(it) } }) }
    override fun onClose(action: (Short)->Unit) { native.addEventListener("close", { action((it as CloseEvent).code) }) }
}