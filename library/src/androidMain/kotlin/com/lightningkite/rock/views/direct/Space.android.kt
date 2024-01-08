package com.lightningkite.rock.views.direct

import android.view.View
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = View

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit) {
    return viewElement(factory = ::NSpace, wrapper = ::Space) {
        handleTheme(native)  { it, native ->
            native.minimumWidth = it.spacing.value.toInt()
            native.minimumHeight = it.spacing.value.toInt()
        }
        setup(this)
    }
}

actual fun ViewWriter.space(
    multiplier: Double,
    setup: Space.() -> Unit,
) {
    return viewElement(factory = ::View, wrapper = ::Space) {
        handleTheme(native)  { it, native ->
            native.minimumWidth = it.spacing.value.times(multiplier).toInt()
            native.minimumHeight = it.spacing.value.times(multiplier).toInt()
        }
        setup(this)
    }
}