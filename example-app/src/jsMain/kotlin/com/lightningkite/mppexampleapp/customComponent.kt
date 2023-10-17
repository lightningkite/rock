package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewContext
import org.w3c.dom.HTMLVideoElement

actual class CustomComponent(val native: HTMLVideoElement) {
    actual var src: String
        get() = native.src
        set(value) {
            native.src = value
        }
}
actual fun ViewContext.customComponent(setup: CustomComponent.()->Unit) {
    element<HTMLVideoElement>("video") {
        setup(CustomComponent(this))
    }
}