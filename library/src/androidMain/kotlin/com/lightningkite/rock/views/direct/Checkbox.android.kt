package com.lightningkite.rock.views.direct

import android.R
import android.content.res.ColorStateList
import androidx.core.widget.CompoundButtonCompat
import android.widget.CheckBox as AndroidCheckBox
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.reactiveScope

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
        val theme = currentTheme
        reactiveScope {
            val it = theme()
            CompoundButtonCompat.setButtonTintList(
                native, ColorStateList(
                    arrayOf<IntArray>(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)), intArrayOf(
                        it.selected().background.closestColor().copy(alpha = 0.5f).colorInt(),
                        it.selected().background.colorInt()
                    )
                )
            )
        }
        setup(this)
    }
}