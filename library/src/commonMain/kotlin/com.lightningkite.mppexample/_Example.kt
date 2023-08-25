package com.lightningkite.mppexample

//expect val codePlatform: String
//expect val uiPlatform: String
//
//@Serializable
//data class Example(val x: Int, val y: String)
//
//
//expect interface PlatformView {}
//data class Color(val r: UByte, val g: UByte, val b: UByte, val a: UByte = 255.toUByte()) {
//    companion object {
//        val BLACK = Color(0.toUByte(), 0.toUByte(), 0.toUByte())
//        val RED = Color(255.toUByte(), 0.toUByte(), 0.toUByte())
//    }
//}
//expect fun PlatformView.setBackgroundColor(color: Color)
//
//
//fun fireAndForgetLaunch(action: suspend () -> Unit) {
//    lateinit var context: Continuation<Unit>
//
//    action.startCoroutine(
//        object : Continuation<Unit> {
//            override val context: CoroutineContext = EmptyCoroutineContext
//            // called when a coroutine ends. do nothing.
//            override fun resumeWith(result: Result<Unit>) {
//                result.onFailure { ex : Throwable -> throw ex }
//            }
//        }
//    )
//
//    context.resume(Unit)
//}



