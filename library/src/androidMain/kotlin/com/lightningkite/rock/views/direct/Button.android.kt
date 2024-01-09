package com.lightningkite.rock.views.direct

import android.widget.FrameLayout
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = FrameLayout

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit) {
    viewElement(factory = ::FrameLayout, wrapper = ::ContainingView) {
        val frame = native as FrameLayout
        handleThemeControl(frame) {
            setup(Button(frame))
        }
    }
}

actual fun Button.onClick(action: suspend () -> Unit) {
    native.setOnClickListener { view ->
        launch { action() }
    }
}

actual var Button.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }