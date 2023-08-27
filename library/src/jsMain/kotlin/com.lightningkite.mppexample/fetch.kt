package com.lightningkite.mppexample

import kotlinx.browser.window
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.files.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun fetch(
    url: String,
    method: HttpMethod,
    headers: HttpHeaders,
    body: RequestBody?
): RequestResponse {
    val a = js("new AbortController()")
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
    val promise = window.fetch(url, o)
    return suspendCoroutineCancellable { cont ->
        promise.then(
            onFulfilled = {
                cont.resume(RequestResponse(it))
            },
            onRejected = {
                cont.resumeWithException(it)
            }
        )
        return@suspendCoroutineCancellable { a.abort() }
    }
}
actual inline fun httpHeaders(map: Map<String, String>): HttpHeaders = HttpHeaders(init = map)
actual inline fun httpHeaders(headers: HttpHeaders): HttpHeaders = HttpHeaders(init = headers)
actual inline fun httpHeaders(list: List<Pair<String, String>>): HttpHeaders = HttpHeaders(init = list)
actual typealias HttpHeaders = Headers
actual class RequestResponse(val wraps: Response) {
    actual val status: Short get() = wraps.status
    actual val ok: Boolean get() = wraps.ok
    actual suspend fun text(): String = wraps.text().await()
    actual suspend fun blob(): Blob = wraps.blob().await()
}

actual typealias Blob = org.w3c.files.Blob
actual typealias FileReference = File
