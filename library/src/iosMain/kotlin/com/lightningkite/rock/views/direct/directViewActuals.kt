@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView

fun ViewWriter.todo(name: String) = element(UIView())  {}



class SpacerView: UIView(CGRectZero.readValue()) {

}

class FrameView: UIView(CGRectZero.readValue()) {

}

//private val UIViewExtensionGravity = ExtensionProperty<UIView, Pair<Align, Align>>()
//val UIView.extensionGravity by UIViewExtensionGravity
//class FrameView: UIView(CGRectZero.readValue()) {
//    override fun addSubview(view: UIView) {
//        super.addSubview(view)
//    }
//}

//@ViewDsl
//internal fun ViewWriter.textElement(elementBase: String, setup: TextView.() -> Unit): Unit =
//    themedElement<HTMLDivElement>(elementBase) {
//        setup(TextView(this))
//        style.whiteSpace = "pre-wrap"
//    }
//
//@ViewDsl
//internal fun ViewWriter.headerElement(elementBase: String, setup: TextView.() -> Unit): Unit =
//    themedElement<HTMLDivElement>(elementBase) {
//        setup(TextView(this))
//        style.whiteSpace = "pre-wrap"
//        classList.add("title")
//    }
//
//fun UIView.__resetContentToOptionList(options: List<WidgetOption>, selected: String) {
//    innerHTML = ""
//    for (item in options) appendChild((document.createElement("option") as HTMLOptionElement).apply {
//        this.value = item.key
//        this.innerText = item.display
//        this.selected = item.key == selected
//    })
//}
//
//internal fun Canvas.pointerListenerHandler(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): (Event) -> Unit =
//    {
//        val event = it as PointerEvent
//        val b = native.getBoundingClientRect()
//        action(event.pointerId, event.pageX - b.x, event.pageY - b.y, b.width, b.height)
//    }
//
//
//external class ResizeObserver(callback: (Array<ResizeObserverEntry>, observer: ResizeObserver)->Unit) {
//    fun disconnect()
//    fun observe(target: Element, options: ResizeObserverOptions = definedExternally)
//    fun unobserve(target: Element)
//}
//external interface ResizeObserverOptions {
//    val box: String
//}
//external interface ResizeObserverEntry {
//    val target: Element
//    val contentRect: DOMRectReadOnly
//    val contentBoxSize: ResizeObserverEntryBoxSize
//    val borderBoxSize: ResizeObserverEntryBoxSize
//}
//external interface ResizeObserverEntryBoxSize {
//    val blockSize: Double
//    val inlineSize: Double
//}
//
//class SizeReader(val native: UIView, val key: String): Readable<Double> {
//    override suspend fun awaitRaw(): Double = native.asDynamic()[key].unsafeCast<Int>().toDouble()
//    override fun addListener(listener: () -> Unit): () -> Unit {
//        val o = ResizeObserver { _, _ ->
//            listener()
//        }
//        o.observe(native)
//        return { o.disconnect() }
//    }
//}