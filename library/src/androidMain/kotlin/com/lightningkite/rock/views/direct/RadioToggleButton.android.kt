package com.lightningkite.rock.views.direct

import android.view.View
import androidx.appcompat.widget.AppCompatToggleButton
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = View

actual var RadioToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        return this@checked.native.selected
    }

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit) {
    return viewElement(factory = ::AppCompatToggleButton, wrapper = ::RadioToggleButton) {
        handleTheme(native)
        setup(this)
    }
}