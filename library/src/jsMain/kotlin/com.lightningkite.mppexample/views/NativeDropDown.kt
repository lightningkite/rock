package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NativeDropDown = HTMLSelectElement

@ViewDsl
actual inline fun <T> ViewContext.nativeDropDown(
    options: Readable<List<T>>,
    crossinline getLabel: (T) -> String,
    crossinline getKey: (T) -> String,
    prop: Writable<T>,
    setup: NativeDropDown.() -> Unit,
): Unit = element<HTMLSelectElement>("select") {
    forEach(
        data = { options.current },
        render = { it ->
            element<HTMLOptionElement>("option") {
                value = getKey(it)
                innerText = getLabel(it)
                selected = prop.once == getKey(it)
            }
        }
    )

    addEventListener("change", {
        val key = (it.currentTarget as HTMLSelectElement).value
        @Suppress("UNCHECKED_CAST")
        val selected = options.once.find { option -> getKey(option) == key } as T
        prop set selected
    })

    reactiveScope {
        value = getKey(prop.current)
    }

//    default to first element if the selected value is not an option
    reactiveScope {
        val found = options.current.any { it == prop.current }
        if (!found && options.current.isNotEmpty())
            prop set options.current.first()
    }

    setup()
}

actual var NativeDropDown.disabled: Boolean
    get() = throw NotImplementedError()
    set(value) {
        this.disabled = value
    }
