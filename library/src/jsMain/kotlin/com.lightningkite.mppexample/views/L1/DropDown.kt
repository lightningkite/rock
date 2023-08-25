package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias DropDown = HTMLSelectElement

actual inline fun ViewContext.dropDown(setup: DropDown.() -> Unit): Unit = element<HTMLSelectElement>("select") {
    setup()
}

actual fun <T> DropDown.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>
) {
    var lastListener: ((Event) -> Unit)? = null
    reactiveScope {
        innerHTML = ""
        var idx = 0
        var foundSelected = false
        val optionValues = options()
        appendChild(document.createElement("option").apply {
            this as HTMLOptionElement
            this.value = ""
            this.innerText = ""
        })
        optionValues.forEach { value ->
            appendChild(document.createElement("option").apply {
                this as HTMLOptionElement
                val key = getKey(value)
                this.value = key
                this.innerText = getLabel(value)
                if (key == prop.once)
                    foundSelected = true
            })
            idx++
        }
        if (lastListener != null)
            removeEventListener("change", lastListener)
        if (prop.once != null && !foundSelected)
            if (optionValues.isEmpty()) {
                value = ""
                prop set null
            } else {
                value = getKey(optionValues.first())
                prop set optionValues.first()
            }
        lastListener = { it: Event ->
            val key = it.currentTarget.asDynamic().value as String
            val option = optionValues.find { getKey(it) == key }
            prop set option
        }
        addEventListener("change", lastListener)
    }
}

//actual fun DropDown.bind(writable: Writable<String>) {
//    value = writable.once
//    addEventListener("change", {
//        writable set it.currentTarget.asDynamic().value as String
//    })
//}

actual var DropDown.values: List<DropDownOption>
    get() = throw NotImplementedError()
    set(values) {

    }
