package com.lightningkite.rock.views.direct

import android.widget.FrameLayout
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = FrameLayout

actual var ToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() {
        return this@checked.native.selected
    }

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit) {
    return viewElement(factory = ::FrameLayout, wrapper = ::ToggleButton) {
        handleTheme(native)
        setup(this)
    }
}