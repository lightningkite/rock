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

    var popCount = 0
    inline fun <T : HTMLElement> containsNext(name: String, setup: T.() -> Unit): ViewWrapper {
        val element = (document.createElement(name) as T)
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
            stackUse(this) {
                setup()
            }
            stack.last().appendChild(this)
            while (popCount > 0) {
                println("End containsNext")
                val item = stack.removeLast()
                stack.last().appendChild(item)
                popCount--
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

actual var NView.background: Background?
    get() {
        return null
    }
    set(value) {
        if (value != null) {
            this.style.removeProperty("background")
            this.style.removeProperty("backgroundImage")
            if (value.fill is Color)
                this.style.background = value.fill.toWeb()
            if (value.fill is LinearGradient) {
                this.style.backgroundImage = "linear-gradient(${value.fill.angle.turns}turn, ${
                    value.fill.stops.joinToString {
                        it.color.toWeb()
                    }
                })"
            }
//                this.style.backgroundImage = "linear-gradient(${value.fill.angle.turns}turn, ${
//                    value.fill.stops.map {
//                        it.color.toWeb()
//                    }})"
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

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias SimpleLabel = HTMLParagraphElement

actual inline fun ViewContext.simpleLabel(setup: SimpleLabel.() -> Unit): Unit = element("p", setup)
actual var SimpleLabel.text: String
    get() = this.textContent ?: ""
    set(value) {
        this.textContent = value
    }


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Column = HTMLDivElement

actual inline fun ViewContext.column(setup: Column.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Row = HTMLDivElement

actual inline fun ViewContext.row(setup: Row.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}

actual fun ViewContext.padding(): ViewWrapper = containsNext<HTMLDivElement>("div") { style.padding = "1rem" }