package com.lightningkite.rock.views.direct

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.l2.icon
import com.lightningkite.rock.views.visible

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit): Unit {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    radioToggleButton {
        icon(Icon.done, "") {
            ::visible.invoke { checked.await() }
        } in marginless
        setup(RadioButton(this.native))
    }
}

actual inline var RadioButton.enabled: Boolean
    get() = native.enabled
    set(value) { native.enabled = value }
actual val RadioButton.checked: Writable<Boolean> get() =  native.checkedWritable