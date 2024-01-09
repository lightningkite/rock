package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import android.widget.RadioButton as AndroidRadioButton

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = AndroidRadioButton

actual var RadioButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val RadioButton.checked: Writable<Boolean>
    get() {
        return native.checked
    }

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit) {
    return viewElement(factory = ::AndroidRadioButton, wrapper = ::RadioButton) {
        handleTheme(native)
        setup(this)
    }
}