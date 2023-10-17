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


actual class ViewContext(
    parent: HTMLElement
) {
    actual val addons: MutableMap<String, Any?> = mutableMapOf()
    actual fun split(): ViewContext = ViewContext(stack.last()).also {
        it.addons.putAll(this.addons)
    }

    var themeJustChanged: Boolean = false

    val stack = arrayListOf(parent)
    fun <T : HTMLElement> stackUse(item: T, action: T.() -> Unit) =
        ListeningLifecycleStack.useIn(item.onRemove) {
            stack.add(item)
            try {
                action(item)
            } finally {
                stack.removeLast()
            }
        }

    val onRemoveList = ArrayList<() -> Unit>()
    actual val onRemove: OnRemoveHandler = object: OnRemoveHandler {
        override fun onRemove(action: () -> Unit) {
            onRemoveList.add(action)
        }
    }

    fun close() {
        onRemoveList.forEach { it() }
        onRemoveList.clear()
    }

    actual fun beforeNextElementSetup(action: NView.()->Unit) {
        beforeNextElementSetupList.add(action)
    }
    actual fun afterNextElementSetup(action: NView.()->Unit) {
        afterNextElementSetupList.add(action)
    }
    val beforeNextElementSetupList = ArrayList<HTMLElement.() -> Unit>()
    var afterNextElementSetupList = ArrayList<HTMLElement.() -> Unit>()
//    private val wrapperToDoList = ArrayList<HTMLElement.() -> Unit>()
    var popCount = 0

    @Suppress("UNCHECKED_CAST")
    inline fun <T : HTMLElement> containsNext(name: String, setup: T.() -> Unit): ViewWrapper {
        val element = (document.createElement(name) as T)
        ListeningLifecycleStack.useIn(element.onRemove) {
            setup(element)
        }
        stack.add(element)
        popCount++
        return ViewWrapper
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <T : HTMLElement> element(name: String, noinline setup: T.() -> Unit) =
        element(document.createElement(name) as T, setup)

    fun <T : HTMLElement> element(initialElement: T, setup: T.() -> Unit) {
        initialElement.apply {
            stack.last().appendChild(this)
            beforeNextElementSetupList.forEach { it(this) }
            beforeNextElementSetupList.clear()
            val afterCopy = if(afterNextElementSetupList.isNotEmpty()) afterNextElementSetupList.toList() else listOf()
            afterNextElementSetupList = ArrayList()
            var toPop = popCount
            popCount = 0
            stackUse(this) {
                setup()
            }
            afterCopy.forEach { it(this) }
            while (toPop > 0) {
                val item = stack.removeLast()
//                wrapperToDoList.forEach { it(item) }
                stack.last().appendChild(item)
                toPop--
            }
//            wrapperToDoList.clear()
        }
    }

    actual fun clearChildren() {
        stack.last().innerHTML = ""
    }

    actual fun <T> forEachUpdating(items: Readable<List<T>>, render: ViewContext.(Readable<T>)->Unit) {
        // TODO: Faster version
        return with(split()) {
            reactiveScope {
                try {
                    clearChildren()
                    items.current.forEach {
                        render(Constant(it))
                    }
                } catch(_: Loading) {
                    clearChildren()
                    repeat(5) {
                        render(LoadingForever)
                    }
                }
            }
        }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = HTMLElement

actual val NView.onRemove: OnRemoveHandler
    get() {
        return object: OnRemoveHandler{
            val native:NView = this@onRemove
            override fun onRemove(action: () -> Unit) {
                native.removeListeners.add(action)
            }

            override fun onLoading() {
                native.classList.add("loading")
            }

            override fun onTry() {
                native.classList.remove("loading")
            }

            override fun onFail() {

            }

            override fun onOk() {
            }
        }
    }

actual var NView.exists: Boolean
    get() = throw NotImplementedError()
    set(value) {
        style.display = if (value) "flex" else "none"
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


actual fun ViewContext.setTheme(calculate: ReactiveScope.()-> Theme): ViewWrapper {
    val old = themeStack
    themeStack += calculate
    themeJustChanged = true
    this.afterNextElementSetup {
        themeStack = old
        themeJustChanged = false
    }
    return ViewWrapper
}