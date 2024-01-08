package com.lightningkite.rock.views.direct

import androidx.appcompat.widget.SwitchCompat
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = SwitchCompat

actual var Switch.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val Switch.checked: Writable<Boolean>
    get() {
        return native.checked
    }

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit) {
    return viewElement(factory = ::SwitchCompat, wrapper = ::Switch) {
        handleTheme(native)
        setup(this)
    }
}