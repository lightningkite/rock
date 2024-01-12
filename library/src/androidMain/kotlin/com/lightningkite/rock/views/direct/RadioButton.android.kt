package com.lightningkite.rock.views.direct

import android.R
import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.widget.CompoundButtonCompat
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = AppCompatRadioButton

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
    return viewElement(factory = ::NRadioButton, wrapper = ::RadioButton) {
        handleTheme(native) { it, radio ->
            CompoundButtonCompat.setButtonTintList(radio, ColorStateList(
                arrayOf<IntArray>(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)), intArrayOf(
                    it.unselected().foreground.colorInt(),
                    it.selected().foreground.colorInt()
                )
            ))
        }
        setup(this)
    }
}