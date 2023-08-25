package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RadioButton = HTMLDivElement

actual inline fun ViewContext.radioButton(setup: RadioButton.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

actual fun <T> RadioButton.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
) {
    reactiveScope {
        innerHTML = ""
        var foundSelected = false
        val optionValues = options()
        val inputs = mutableListOf<HTMLInputElement>()

        optionValues.forEach { value ->
            appendChild(document.createElement("label").apply {
                this as HTMLLabelElement

                style.display = "flex"
                style.flexDirection = "row"

                val key = getKey(value)
                val selected = key == prop.once
                foundSelected = foundSelected || selected

                val input = document.createElement("input") as HTMLInputElement
                input.type = "radio"
                input.checked = selected
                input.value = key
                appendChild(input)
                inputs.add(input)

                val span = document.createElement("span")
                span.innerHTML = getLabel(value)
                appendChild(span)

                input.addEventListener("change", {
                    inputs.forEach { it.checked = false }
                    prop set value
                    input.checked = true
                })
            })
        }
//        previousListeners = { it: Event ->
//            val key = it.currentTarget.asDynamic().value as String?
//            val option = optionValues.find { getKey(it) == key }
//            if (option != null)
//                prop set option
//            inputs.forEach { it.checked = it.value == key }
//        }
//        addEventListener("change", previousListeners)
//        if (!foundSelected && optionValues.isNotEmpty()) {
//            value = getKey(optionValues.first())
//            prop set optionValues.first()
//        }
    }
}
