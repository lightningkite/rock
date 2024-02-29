package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatToggleButton
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NRadioToggleButton(context: Context): SlightlyModifiedFrameLayout(context), View.OnClickListener {
    val checked = Property(false)
    init { setOnClickListener(this) }

    override fun onClick(v: View?) {
        if(!checked.value) checked.value = true
    }
}

actual var RadioToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() = native.checked

@ViewDsl
actual inline fun ViewWriter.radioToggleButtonActual(crossinline setup: RadioToggleButton.() -> Unit) {
    return viewElement(factory = ::NRadioToggleButton, wrapper = ::RadioToggleButton) {
        handleThemeControl(native, checked = { checked.await() }){
            setup(this)
        }
    }
}