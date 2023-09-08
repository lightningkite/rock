package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.MutationObserver
import org.w3c.dom.MutationObserverInit
import org.w3c.dom.asList


actual class ViewContext(
    parent: HTMLElement
) {
    actual val addons: MutableMap<String, Any?> = mutableMapOf()
    fun derive(parent: HTMLElement): ViewContext = ViewContext(parent).also {
        it.addons.putAll(this.addons)
    }

    val stack = arrayListOf(parent)
    inline fun <T : HTMLElement> stackUse(item: T, action: T.() -> Unit) =
        ListeningLifecycleStack.useIn(item.onRemove) {
            stack.add(item)
            try {
                action(item)
            } finally {
                stack.removeLast()
            }
        }

    val onRemoveList = ArrayList<() -> Unit>()
    actual val onRemove: OnRemoveHandler = {
        onRemoveList.add(it)
    }

    fun close() {
        onRemoveList.forEach { it() }
        onRemoveList.clear()
    }

    val elementToDoList = ArrayList<HTMLElement.() -> Unit>()
    val wrapperToDoList = ArrayList<HTMLElement.() -> Unit>()
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
    inline fun <T : HTMLElement> element(name: String, setup: T.() -> Unit) =
        element(document.createElement(name) as T, setup)

    inline fun <T : HTMLElement> element(initialElement: T, setup: T.() -> Unit) {
        initialElement.apply {
            elementToDoList.forEach { it(this) }
            elementToDoList.clear()
            var toPop = popCount
            popCount = 0
            stackUse(this) {
                setup()
            }
            stack.last().appendChild(this)
            while (toPop > 0) {
                val item = stack.removeLast()
                wrapperToDoList.forEach { it(item) }
                stack.last().appendChild(item)
                toPop--
            }
            wrapperToDoList.clear()
        }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = HTMLElement

actual val NView.onRemove: OnRemoveHandler
    get() {
        return {
            this.removeListeners.add(it)
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

actual var NView.elevation: Dimension
    get() = throw NotImplementedError()
    set(value) {
        style.boxShadow = value.toBoxShadow()
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

actual var NView.cursor: String
    get() = throw NotImplementedError()
    set(value) {
        style.cursor = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NViewWithTextStyle = HTMLElement

actual fun NViewWithTextStyle.setStyles(styles: TextStyle) {
    style.color = styles.color.toWeb()
    style.setProperty("--disabled-color", styles.disabledColor.toWeb())
    style.fontSize = "${styles.size}px"
    style.fontFamily = styles.font
    style.fontWeight = if (styles.bold) "bold" else "normal"
    style.fontStyle = if (styles.italic) "italic" else "normal"
    style.textTransform = if (styles.allCaps) "uppercase" else "none"
    style.lineHeight = styles.lineSpacingMultiplier.toString()
    style.letterSpacing = styles.letterSpacing.toString()
}
