package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.MutationObserver
import org.w3c.dom.MutationObserverInit
import org.w3c.dom.asList

inline fun <T : HTMLElement> ViewWriter.containsNext(name: String, noinline setup: T.() -> Unit): ViewWrapper =
    wrapNext<T>(document.createElement(name) as T, setup)

inline fun <T : HTMLElement> ViewWriter.element(name: String, noinline setup: T.() -> Unit) =
    element(document.createElement(name) as T, setup)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = HTMLElement

data class NViewCalculationContext(val native: NView): CalculationContext {
    override fun onRemove(action: () -> Unit) {
        native.removeListeners.add(action)
    }

    override fun notifyStart() {
        native.classList.add("loading")
    }

    override fun notifySuccess() {
        native.classList.remove("loading")
    }

    override fun notifyFailure() {
        native.classList.remove("loading")
    }
}
actual val NView.calculationContext: CalculationContext
    get() = NViewCalculationContext(this)

actual var NView.exists: Boolean
    get() = throw NotImplementedError()
    set(value) {
//        style.display = if (value) "flex" else "none"
        hidden = !value
    }

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        style.visibility = if (value) "visible" else "hidden"
    }

actual var NView.opacity: Double
    get() = throw NotImplementedError()
    set(value) {
        style.opacity = value.toString()
    }

actual var NView.nativeRotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        style.transform = "rotate(${value.turns}turn)"
    }

actual fun NView.clearChildren() {
    innerHTML = ""
}
actual fun NView.addChild(child: NView) {
    appendChild(child)
}


private object RemoveListeners {
    val symbol = js("Symbol('removeListeners')")

    init {
        MutationObserver { list, ob ->
            list.forEach {
                it.removedNodes.asList().forEach { node ->
                    if(node is HTMLElement) shutdown(node)
                }
            }
        }.observe(
            document.body!!, MutationObserverInit(
                childList = true,
                subtree = true,
            )
        )
    }

    private fun shutdown(element: HTMLElement) {
        element.removeListenersMaybe?.let {
            it.forEach { it() }
            it.clear()
        }
        for(child in element.childNodes.asList()) {
            if(child is HTMLElement) shutdown(child)
        }
    }
}

private val HTMLElement.removeListeners: MutableList<() -> Unit>
    get() = removeListenersMaybe ?: run {
        val newList = ArrayList<() -> Unit>()
        this.asDynamic()[RemoveListeners.symbol] = newList
        newList
    }
private val HTMLElement.removeListenersMaybe: MutableList<() -> Unit>?
    get() = this.asDynamic()[RemoveListeners.symbol] as? MutableList<() -> Unit>
