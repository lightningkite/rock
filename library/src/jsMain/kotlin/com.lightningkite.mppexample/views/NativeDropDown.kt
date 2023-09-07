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


//actual fun <T> NativeDropDown.bind(
//    options: ReactiveScope.() -> List<T>,
//    getLabel: (T) -> String,
//    getKey: (T) -> String,
//    prop: Writable<T?>
//) {
//    var lastListener: ((Event) -> Unit)? = null
//
//    reactiveScope {
//        val current = prop.current ?: return@reactiveScope
//        val selected = getKey(current)
//        querySelectorAll("option").asList().forEach {
//            it as HTMLOptionElement
//            it.selected = selected == it.value
//        }
//    }
//
//    reactiveScope {
//        innerHTML = ""
//        var foundSelected = false
//        val optionValues = options()
//        appendChild(document.createElement("option").apply {
//            this as HTMLOptionElement
//            this.value = ""
//            this.innerText = ""
//        })
//        optionValues.forEach { value ->
//            appendChild(document.createElement("option").apply {
//                this as HTMLOptionElement
//                val key = getKey(value)
//                this.value = key
//                this.innerText = getLabel(value)
//                if (key == prop.once)
//                    foundSelected = true
//            })
//        }
//        if (lastListener != null)
//            removeEventListener("change", lastListener)
//        if (prop.once != null && !foundSelected)
//            if (optionValues.isEmpty()) {
//                value = ""
//                prop set null
//            } else {
//                value = getKey(optionValues.first())
//                prop set optionValues.first()
//            }
//        lastListener = { it: Event ->
//            val key = it.currentTarget.asDynamic().value as String
//            val option = optionValues.find { getKey(it) == key }
//            prop set option
//        }
//        addEventListener("change", lastListener)
//    }
//}
