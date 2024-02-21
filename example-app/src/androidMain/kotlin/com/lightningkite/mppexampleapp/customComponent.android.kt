package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewWriter

actual class CustomComponent {
    actual var src: String
        get() = TODO("Not yet implemented")
        set(value) {}
}

actual fun ViewWriter.customComponent(setup: CustomComponent.() -> Unit) {
}