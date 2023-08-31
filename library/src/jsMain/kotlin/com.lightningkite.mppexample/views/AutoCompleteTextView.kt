package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias AutoCompleteTextView = HTMLDataListElement

actual inline fun ViewContext.autoCompleteTextView(setup: AutoCompleteTextView.() -> Unit): Unit =
    element<HTMLDataListElement>("div") {
        nativeTextField {}
        setup()
    }

var nextAutocompleteId = 0

@JsName("AutocompleteTextViewBind")
actual fun <T> AutoCompleteTextView.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
) {
    val input = getElementsByTagName("input")[0] as HTMLInputElement

    val currentValue = prop.once
    input.value = if (currentValue == null) "" else getLabel(currentValue)
    reactiveScope {
        val newValue = prop.current
        input.value = if (newValue == null) "" else getLabel(newValue)
    }


    val datalist = document.createElement("datalist") as HTMLDataListElement
    datalist.id = "autocomplete-${nextAutocompleteId++}"
    val map = mutableMapOf<String, T>()
    reactiveScope {
        map.clear()
        datalist.innerHTML = ""
        options().forEach { option ->
            datalist.appendChild(
                document.createElement("option").apply {
                    this as HTMLOptionElement
                    val value = getKey(option)
                    this.value = value
                    this.innerText = getLabel(option)
                    map[value] = option
                }
            )
        }
    }
    document.body?.appendChild(datalist)
    input.setAttribute("list", datalist.id)

    input.addEventListener("input", {
        val stringValue = it.currentTarget.asDynamic().value as String
        val value = map[stringValue]
        if (prop.once != value)
            prop set value
    })
}

actual var AutoCompleteTextView.label: String
    get() = throw NotImplementedError()
    set(value) {
        val label = getElementsByTagName("div")[0] as NativeTextField
        label.hint = value
    }

actual var AutoCompleteTextView.textStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        val input = getElementsByTagName("input")[0] as HTMLInputElement
        input.setStyles(value)
    }

actual var AutoCompleteTextView.labelStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        val child = firstChild as HTMLDivElement
        child.style.setProperty("--text-field-color", value.color.toWeb()) // set the outline color to match the label color
    }
