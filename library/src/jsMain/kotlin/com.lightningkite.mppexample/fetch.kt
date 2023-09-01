package com.lightningkite.mppexample

import kotlinx.browser.window
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
    body: RequestBody?
): RequestResponse {
    val a: dynamic = js("new AbortController()")
    val o = js("({})")
    o.method = method.name
    o.headers = headers
    o.body = when(body) {
        null -> undefined
        is RequestBodyBlob -> body.content
        is RequestBodyFile -> body.content
        is RequestBodyText -> body.content
        else -> throw NotImplementedError()
    }
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
    actual suspend fun text(): String = wraps.text().await()
    actual suspend fun blob(): Blob = wraps.blob().await()
}

actual typealias Blob = org.w3c.files.Blob
actual typealias FileReference = File
