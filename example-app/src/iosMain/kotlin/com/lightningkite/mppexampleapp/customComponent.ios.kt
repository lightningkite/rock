package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewWriter
import kotlin.native.runtime.NativeRuntimeApi

actual class CustomComponent {
    actual var src: String
        get() = TODO("Not yet implemented")
        set(value) {}
}

actual fun ViewWriter.customComponent(setup: CustomComponent.() -> Unit) {
}
