package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeAutoComplete = HTMLInputElement

@ViewDsl
actual inline fun ViewContext.nativeAutoComplete(
    setup: NativeAutoComplete.() -> Unit,
): Unit {
    nativeTextField {
        setup()
    }
}

var nextAutocompleteId = 0

@JsName("AutocompleteTextViewBind")
actual fun <T> NativeAutoComplete.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
) {
    val currentValue = prop.once
    value = if (currentValue == null) "" else getLabel(currentValue)
    reactiveScope {
        val newValue = prop.current
        value = if (newValue == null) "" else getLabel(newValue)
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
    setAttribute("list", datalist.id)

    addEventListener("input", {
        val stringValue = it.currentTarget.asDynamic().value as String
        val value = map[stringValue]
        if (prop.once != value)
            prop set value
    })
}

actual var NativeAutoComplete.autoCompleteTextStyle: TextStyle
    get() = throw NotImplementedError()
    set(value) {
        setStyles(value)
    }

actual var NativeAutoComplete.autoCompleteHint: String
    get() = throw NotImplementedError()
    set(value) {
        placeholder = value
    }
