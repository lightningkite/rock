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

//
//actual class ViewWriter(
//    parent: HTMLElement
//) {
//    actual val addons: MutableMap<String, Any?> = mutableMapOf()
//    actual fun split(): ViewWriter = ViewWriter(stack.last()).also {
//        it.addons.putAll(this.addons)
//    }
//
//    var themeJustChanged: Boolean = false
//
//    val stack = arrayListOf(parent)
//    fun <T : HTMLElement> stackUse(item: T, action: T.() -> Unit) =
//        CalculationContextStack.useIn(item.onRemove) {
//            stack.add(item)
//            try {
//                action(item)
//            } finally {
//                stack.removeLast()
//            }
//        }
//
//    val onRemoveList = ArrayList<() -> Unit>()
//    actual val calculationContext: CalculationContext = stack.last().onRemove
//
//    fun close() {
//        onRemoveList.forEach { it() }
//        onRemoveList.clear()
//    }
//
//    actual fun beforeNextElementSetup(action: NView.()->Unit) {
//        beforeNextElementSetupList.add(action)
//    }
//    actual fun afterNextElementSetup(action: NView.()->Unit) {
//        afterNextElementSetupList.add(action)
//    }
//    var beforeNextElementSetupList = ArrayList<HTMLElement.() -> Unit>()
//    var afterNextElementSetupList = ArrayList<HTMLElement.() -> Unit>()
//    //    private val wrapperToDoList = ArrayList<HTMLElement.() -> Unit>()
//    var popCount = 0
//
//    @Suppress("UNCHECKED_CAST")
//        val element = (document.createElement(name) as T)
//        stack.add(element)
//        CalculationContextStack.useIn(element.onRemove) {
//            setup(element)
//        }
//        popCount++
//        return ViewWrapper
//    }
//
//    @Suppress("UNCHECKED_CAST")
//
//    fun <T : HTMLElement> element(initialElement: T, setup: T.() -> Unit) {
//        initialElement.apply {
//            stack.last().appendChild(this)
//            val beforeCopy = if(beforeNextElementSetupList.isNotEmpty()) beforeNextElementSetupList.toList() else listOf()
//            beforeNextElementSetupList = ArrayList()
//            val afterCopy = if(afterNextElementSetupList.isNotEmpty()) afterNextElementSetupList.toList() else listOf()
//            afterNextElementSetupList = ArrayList()
//            var toPop = popCount
//            popCount = 0
//            stackUse(this) {
//                beforeCopy.forEach { it(this) }
//                setup()
//                afterCopy.forEach { it(this) }
//            }
//            while (toPop > 0) {
//                val item = stack.removeLast()
//                stack.last().appendChild(item)
//                toPop--
//            }
////            wrapperToDoList.clear()
//        }
//    }
//
//
//    actual fun <T> forEachUpdating(items: Readable<List<T>>, render: ViewWriter.(Readable<T>)->Unit) {
//        // TODO: Faster version
//        return with(split()) {
//            calculationContext.reactiveScope {
//                clearChildren()
//                repeat(5) {
//                    render(Never)
//                }
//                val data = items.await()
//                clearChildren()
//                data.forEach {
//                    render(Constant(it))
//                }
//            }
//        }
//    }
//}
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

actual var NView.alpha: Double
    get() = throw NotImplementedError()
    set(value) {
        style.opacity = value.toString()
    }

actual var NView.rotation: Angle
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


actual fun ViewWriter.setTheme(calculate: suspend ()-> Theme?): ViewWrapper {
    val old = themeStack
    themeStack += calculate
    themeJustChanged = true
    this.afterNextElementSetup {
        themeStack = old
        themeJustChanged = false
    }
    return ViewWrapper
}
