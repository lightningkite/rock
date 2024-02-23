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
actual typealias NCheckbox = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.checkboxActual(setup: Checkbox.() -> Unit): Unit {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    toggleButton {
        icon(Icon.done, "") {
            ::visible.invoke { checked.await() }
        } 
        setup(Checkbox(this.native))
    }
}

actual inline var Checkbox.enabled: Boolean
    get() = native.enabled
    set(value) { native.enabled = value }
actual val Checkbox.checked: Writable<Boolean> get() =  native.checkedWritable