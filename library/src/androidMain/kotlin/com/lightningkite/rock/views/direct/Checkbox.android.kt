package com.lightningkite.rock.views.direct

import android.widget.CheckBox as AndroidCheckBox
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = AndroidCheckBox

actual var Checkbox.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val Checkbox.checked: Writable<Boolean>
    get() {
        return native.checked
    }

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit) {
    return viewElement(factory = ::AndroidCheckBox, wrapper = ::Checkbox) {
        handleTheme(native)
        setup(this)
    }
}