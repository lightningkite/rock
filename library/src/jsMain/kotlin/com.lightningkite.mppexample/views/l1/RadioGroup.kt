package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias RadioGroup = HTMLDivElement

actual inline fun ViewContext.radioGroup(setup: RadioGroup.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

actual fun <T> RadioGroup.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
) {
    reactiveScope {
        val currentKey = getKey(prop.current)
        querySelectorAll("input").asList().forEach {
            it as HTMLInputElement
            it.checked = it.value == currentKey
        }
    }

    reactiveScope {
        innerHTML = ""
        var foundSelected = false
        val optionValues = options()
        val inputs = mutableListOf<HTMLInputElement>()

        optionValues.forEach { value ->
            appendChild(document.createElement("label").apply {
                this as HTMLLabelElement

                className = "rock-mui-radio"
                style.display = "flex"
                style.flexDirection = "row"
                style.setProperty("--rock-mui-radio-color", "25, 118, 210")


                val key = getKey(value)
                val selected = key == prop.once
                foundSelected = foundSelected || selected


                val input = document.createElement("input") as HTMLInputElement
                input.type = "radio"
                input.checked = selected
                input.value = key
                appendChild(input)
                inputs.add(input)

                val check = document.createElement("div") as HTMLDivElement
                check.className = "rock-mui-radio-check"
                appendChild(check)

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
    }
}
