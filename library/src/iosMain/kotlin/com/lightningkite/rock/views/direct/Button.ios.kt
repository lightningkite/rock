package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.*
import platform.UIKit.UIControlEventTouchUpInside

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = FrameLayoutButton

@ViewDsl
actual fun ViewWriter.buttonActual(setup: Button.() -> Unit): Unit = element(FrameLayoutButton()) {
    val l = iosCalculationContext.loading
    handleThemeControl(this) {
        setup(Button(this))
        activityIndicator {
            ::exists.invoke { l.await() }
            native.extensionSizeConstraints = SizeConstraints(minWidth = null, minHeight = null)
        }
    }
}

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    var virtualDisable: Boolean = false
    native.onEvent(UIControlEventTouchUpInside) {
        if(!virtualDisable) {
            native.calculationContext.launchManualCancel {
                try {
                    virtualDisable = true
                    action()
                } finally {
                    virtualDisable = false
                }
            }
        }
    }
}

actual inline var Button.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }