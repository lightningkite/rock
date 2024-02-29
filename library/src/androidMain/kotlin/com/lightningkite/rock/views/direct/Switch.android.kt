package com.lightningkite.rock.views.direct

import android.R
import android.content.res.ColorStateList
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.CompoundButtonCompat
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.reactiveScope

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
actual inline fun ViewWriter.switchActual(crossinline setup: Switch.() -> Unit) {
    return viewElement(factory = ::SwitchCompat, wrapper = ::Switch) {
        val theme = currentTheme
        reactiveScope {
            val it = theme()
            native.thumbTintList = ColorStateList(
                arrayOf<IntArray>(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)), intArrayOf(
                    it.background.closestColor().highlight(.2f).colorInt(),
                    it.selected().background.colorInt()
                )
            )
            native.trackTintList = ColorStateList(
                arrayOf<IntArray>(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked)), intArrayOf(
                    it.background.closestColor().highlight(.1f).colorInt(),
                    it.background.closestColor().highlight(.1f).colorInt(),
                )
            )
        }
        setup(this)
    }
}