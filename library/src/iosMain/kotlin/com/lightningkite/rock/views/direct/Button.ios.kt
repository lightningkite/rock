package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch
import platform.UIKit.UIControlEventTouchUpInside

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = FrameLayoutButton

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit): Unit = element(FrameLayoutButton()) {
    handleThemeControl(this) {
        setup(Button(this))
    }
}

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    native.onEvent(UIControlEventTouchUpInside) { launch(action) }
}

actual inline var Button.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }