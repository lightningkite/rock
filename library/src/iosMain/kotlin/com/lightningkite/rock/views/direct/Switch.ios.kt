package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.handleTheme
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = UISwitch

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit): Unit = element(UISwitch()) {
    handleTheme(this) {

    }
    setup(Switch(this))
}

actual inline var Switch.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }
actual val Switch.checked: Writable<Boolean>
    get() {
        return object : Writable<Boolean> {
            override suspend fun awaitRaw(): Boolean = native.on
            override fun addListener(listener: () -> Unit): () -> Unit {
                return native.onEvent(UIControlEventValueChanged) { listener() }
            }

            override suspend fun set(value: Boolean) {
                native.on = value
            }
        }
    }