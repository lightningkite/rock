package com.lightningkite.rock.views.direct

import android.widget.FrameLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = SlightlyModifiedFrameLayout

@ViewDsl
actual inline fun ViewWriter.buttonActual(crossinline setup: Button.() -> Unit) {
    viewElement(factory = ::SlightlyModifiedFrameLayout, wrapper = ::ContainingView) {
        val frame = native as SlightlyModifiedFrameLayout
        native.minimumWidth = 2.rem.value.toInt()
        native.minimumHeight = 2.rem.value.toInt()
        native.isClickable = true
        val l = native.androidCalculationContext.loading
        handleThemeControl(frame) {
            setup(Button(frame))
//            LinearProgressIndicator(context).apply {
//                this.colo
//            }
            activityIndicator {
                ::exists.invoke { l.await() }
                native.minimumWidth = 0
                native.minimumHeight = 0
            }
        }
    }
}

actual fun Button.onClick(action: suspend () -> Unit) {
    var virtualDisable: Boolean = false
    native.setOnClickListener { view ->
        view.calculationContext.launchManualCancel {
            try {
                virtualDisable = true
                action()
            } finally {
                virtualDisable = false
            }
        }
    }
}

actual var Button.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }