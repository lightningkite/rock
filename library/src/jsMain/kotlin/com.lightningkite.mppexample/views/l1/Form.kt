package com.lightningkite.mppexample

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Form = HTMLFormElement

actual inline fun ViewContext.form(setup: Form.() -> Unit): Unit = element<HTMLFormElement>("form") {
    setup()
}

actual fun <T : MutableMap<String, Any>> Form.bind(prop: Writable<T>, onSubmit: (T)->Unit) {
    addEventListener("submit", { event ->
        event.preventDefault()
        val inputs = getElementsByTagName("input")
        inputs.asList().forEach {input ->
            input as HTMLInputElement
            prop.once[input.name] = input.value
        }
        onSubmit(prop.once)
    })
}
