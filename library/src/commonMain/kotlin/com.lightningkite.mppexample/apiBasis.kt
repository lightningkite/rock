package com.lightningkite.mppexample
//
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.encodeToString
//import kotlin.coroutines.resume
//import kotlin.coroutines.resumeWithException
//import kotlin.coroutines.suspendCoroutine
//import kotlin.js.Promise
//
//@Serializable
//data class Thing(
//    val x: Int = 0,
//    val y: String = ""
//)
//
//fun example() {
//    kotlinx.serialization.json.Json.encodeToString(Thing())
//    sequence<Int> {
//        yield(1)
//        yield(1)
//        yield(1)
//        for(i in 0..20) yield(i)
//        while(true) {
//            yield(0)
//        }
//    }.take(400).forEach { println(it) }
//
//
//}
//
//suspend fun fetch(url: String): Response
//
//suspend fun <T> Promise<T>.await() = suspendCoroutine<T> {
//    this.then { result -> it.resume(result) }.catch { e -> it.resumeWithException(e) }
//}
//
//
//
//data class FetchOptions (
//    val method: String = "GET",
//    val headers: Json = json(),
//    val body: Any? = null,
//)
//
//
//
//expect fun fetch(url: String, options: FetchOptions = FetchOptions()): Promise<Response>
