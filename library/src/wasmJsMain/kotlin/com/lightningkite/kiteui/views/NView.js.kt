package com.lightningkite.kiteui.views

import com.lightningkite.kiteui.Cancellable
import com.lightningkite.kiteui.jsObj
import com.lightningkite.kiteui.models.Align
import com.lightningkite.kiteui.models.Angle
import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.reactive.CalculationContext
import com.lightningkite.kiteui.set
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

actual class NContext { companion object { val shared = NContext() }}
actual val NView.nContext: NContext get() = NContext.shared
actual fun NView.removeNView(child: NView) {
    js.removeChild(child.js)
    js.shutdown()
}

actual fun NView.listNViews(): List<NView> = js.children.let {
    (0..<it.length).mapNotNull { index -> it.get(index) as? HTMLElement }.toList().map { NView3(it) }
}

actual fun NView.scrollIntoView(horizontal: Align?, vertical: Align?, animate: Boolean) {
    val d = jsObj()
    d.set("behavior", (if(animate) "smooth" else "instant").toJsString())
    d.set("inline", when(horizontal) {
        Align.Start -> "start"
        Align.Center -> "center"
        Align.End -> "end"
        else -> "nearest"
    }.toJsString())
    d.set("block", when(vertical) {
        Align.Start -> "start"
        Align.Center -> "center"
        Align.End -> "end"
        else -> "nearest"
    }.toJsString())
    js.scrollIntoView(d)
}

var animationsEnabled: Boolean = true
actual inline fun NView.withoutAnimation(action: () -> Unit) = js.withoutAnimation(action)
inline fun HTMLElement.withoutAnimation(action: () -> Unit) {
    if(!animationsEnabled) {
        action()
        return
    }
    try {
        animationsEnabled = false
        clientWidth
        classList.add("notransition")
        clientWidth
        action()
    } finally {
        offsetHeight  // force layout calculation
        window.setTimeout({
            classList.remove("notransition")
            null
        }, 100)
        animationsEnabled = true
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual abstract class NView {
    abstract val js: HTMLElement
}
abstract class NView2<T: HTMLElement>(): NView() {
    abstract override val js: T
}
class NView3<T: HTMLElement>(override val js: T): NView2<T>()

actual var NView.exists: Boolean
    get() = throw NotImplementedError()
    set(value) {
        js.hidden = !value
    }

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        js.style.visibility = if (value) "visible" else "hidden"
    }

actual var NView.spacing: Dimension
    get() {
        return Dimension(js.style.getPropertyValue("--spacing").takeUnless { it.isBlank() } ?: "0px")
    }
    set(value) {
        js.style.setProperty("--spacing", value.value)
        val cn = "spacingOf${value.value.replace(".", "_").filter { it.isLetterOrDigit() || it == '_' }}"
        DynamicCSS.styleIfMissing(".$cn.$cn.$cn.$cn.$cn.$cn.$cn > *, .$cn.$cn.$cn.$cn.$cn.$cn.$cn > .hidingContainer > *", mapOf(
            "--parentSpacing" to value.value
        ))
        js.className = js.className.split(' ').filter { !it.startsWith("spacingOf") }.plus(cn).joinToString(" ")
    }

actual var NView.ignoreInteraction: Boolean
    get() = this.js.classList.contains("noInteraction")
    set(value) { if(value) this.js.classList.add("noInteraction") else this.js.classList.remove("noInteraction") }

actual var NView.opacity: Double
    get() = throw NotImplementedError()
    set(value) {
        js.style.opacity = value.toString()
    }

actual var NView.nativeRotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        js.style.transform = "rotate(${value.turns}turn)"
    }

actual fun NView.clearNViews() {
    val c = js.childNodes
    (0 ..< c.length).forEach { (c.get(it) as? HTMLElement)?.shutdown() }
    js.innerHTML = ""
}
actual fun NView.addNView(child: NView) {
    // Cursed as fuck
    if(this.js.classList.contains("kiteui-stack")){
        child.js.style.zIndex = this.js.childElementCount.toString()
    }
    js.appendChild(child.js)
}

fun HTMLElement.shutdown() {
    calculationContextMaybe?.cancel()
    val c = childNodes
    (0 ..< c.length).forEach { (c.get(it) as? HTMLElement)?.shutdown() }
}

data class NViewCalculationContext(val native: HTMLElement): CalculationContext.WithLoadTracking(), Cancellable {
    val removeListeners = ArrayList<()->Unit>()
    override fun cancel() {
        removeListeners.removeAll { it(); true }
    }

    override fun onRemove(action: () -> Unit) {
        removeListeners.add(action)
    }

    override fun showLoad() {
        native.classList.add("loading")
    }

    override fun hideLoad() {
        native.classList.remove("loading")
    }
}

private fun calculationContext(view: HTMLElement): JsReference<NViewCalculationContext>? = js("view.__ROCK__calculationContext")
private fun setCalculationContext(view: HTMLElement, calculationContext: JsReference<NViewCalculationContext>): Unit {
    js("view.__ROCK__calculationContext = calculationContext")
}

val HTMLElement.calculationContextMaybe: NViewCalculationContext?
    get() = calculationContext(this)?.get()
actual val NView.calculationContext: CalculationContext get() = js.calculationContext
val HTMLElement.calculationContext: CalculationContext
    get() {
        return calculationContext(this)?.get() ?: run {
            val new = NViewCalculationContext(this)
            setCalculationContext(this, new.toJsReference())
            return new
        }
    }

actual fun NView.consumeInputEvents() {
    js.onclick = { it.stopImmediatePropagation() }
}