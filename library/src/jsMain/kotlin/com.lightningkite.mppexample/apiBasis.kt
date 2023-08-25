package com.lightningkite.mppexample

import kotlinx.browser.window
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise

//actual fun fetch(url: String, options: FetchOptions): Promise<Response> {
//    return window.fetch(
//        url, RequestInit(
//            method = options.method,
//            headers = options.headers,
//            body = options.body,
//        )
//    )
//}
//
//expect class Blob
//
//expect sealed interface HttpBody
//expect class HttpBodyString
//expect class HttpBodyBlob
//expect class HttpBodyFileRef
//
//fun test(body: HttpBody) = when(body) {
//    is HttpBody.Binary -> TODO()
//    is HttpBody.Text -> TODO()
//}
