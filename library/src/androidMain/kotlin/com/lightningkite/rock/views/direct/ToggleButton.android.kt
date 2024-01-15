package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NToggleButton(context: Context): FrameLayout(context), View.OnClickListener {
    val checked = Property(false)
    init { setOnClickListener(this) }

    override fun onClick(v: View?) {
        checked.value = !checked.value
    }
}

actual var ToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() = native.checked

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit) {
    return viewElement(factory = ::NToggleButton, wrapper = ::ToggleButton) {
        handleThemeControl(native, checked = { checked.await() }){
            setup(this)
        }
    }
}