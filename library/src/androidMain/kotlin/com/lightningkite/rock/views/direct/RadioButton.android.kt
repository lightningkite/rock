package com.lightningkite.rock.views.direct

import android.R
import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.widget.CompoundButtonCompat
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.reactiveScope

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
actual fun ViewWriter.radioButtonActual(setup: RadioButton.() -> Unit) {
    return viewElement(factory = ::NRadioButton, wrapper = ::RadioButton) {
        val theme = currentTheme
        reactiveScope {
            val it = theme()
            CompoundButtonCompat.setButtonTintList(native, ColorStateList(
                arrayOf<IntArray>(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)), intArrayOf(
                    it.selected().background.closestColor().copy(alpha = 0.5f).colorInt(),
                    it.selected().background.colorInt()
                )
            ))
        }
        setup(this)
    }
}