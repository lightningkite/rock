package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.toggleButtonActual(setup: ToggleButton.() -> Unit): Unit = element(FrameLayoutToggleButton()) {
    handleThemeControl(this, { checkedWritable.await() }) {
        setup(ToggleButton(this))
    }
}

actual inline var ToggleButton.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }
actual val ToggleButton.checked: Writable<Boolean> get() = native.checkedWritable