package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*

actual class ViewContext(
    parent: HTMLElement
) {
    val stack = arrayListOf(parent)
    inline fun <T : HTMLElement> stackUse(item: T, action: T.() -> Unit) =
        ListeningLifecycleStack.useIn(this.onRemove) {
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

    val elementToDoList = ArrayList<HTMLElement.()->Unit>()
    var popCount = 0
    inline fun <T : HTMLElement> containsNext(name: String, setup: T.() -> Unit): ViewWrapper {
        val element = (document.createElement(name) as T)
        elementToDoList.forEach { it(element) }
        elementToDoList.clear()
        ListeningLifecycleStack.useIn(element.onRemove) {
            setup(element)
        }
        println("Start containsNext")
        stack.add(element)
        popCount++
        return ViewWrapper
    }

    inline fun <T : HTMLElement> element(name: String, setup: T.() -> Unit) {
        (document.createElement(name) as T).apply {
            elementToDoList.forEach { it(this) }
            elementToDoList.clear()
            var toPop = popCount
            popCount = 0
            stackUse(this) {
                setup()
            }
            stack.last().appendChild(this)
            while (toPop > 0) {
                println("End containsNext")
                val item = stack.removeLast()
                println("${stack.size} left")
                stack.last().appendChild(item)
                toPop--
            }
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

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        style.display = if (value) "flex" else "none"
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NViewWithTextStyle = HTMLElement

actual fun NViewWithTextStyle.setStyles(styles: TextStyle) {
    style.color = styles.color.toWeb()
    style.fontSize = styles.size.toString()
    style.fontFamily = styles.font
    style.fontWeight = if (styles.bold) "bold" else "normal"
    style.fontStyle = if (styles.italic) "italic" else "normal"
    style.textTransform = if (styles.allCaps) "capitalize" else "none"
    style.lineHeight = styles.lineSpacingMultiplier.toString()
    style.letterSpacing = styles.letterSpacing.toString()
}

private val HTMLElement.removeListeners: MutableList<() -> Unit>
    get() = removeListenersMaybe ?: run {
        val newList = ArrayList<() -> Unit>()
        this.asDynamic()[RemoveListeners.symbol] = newList
        newList
    }
private val HTMLElement.removeListenersMaybe: MutableList<() -> Unit>?
    get() = this.asDynamic()[RemoveListeners.symbol] as? MutableList<() -> Unit>

private object RemoveListeners {
    val symbol = js("Symbol('removeListeners')")

    init {
        MutationObserver { list, ob ->
            list.forEach {
                it.removedNodes.asList().forEach {
                    (it as? HTMLElement)?.removeListenersMaybe?.let {
                        it.forEach { it() }
                        it.clear()
                    }
                }
            }
        }.observe(
            document.body!!, MutationObserverInit(
                childList = true,
                subtree = true,
            )
        )
    }
}
