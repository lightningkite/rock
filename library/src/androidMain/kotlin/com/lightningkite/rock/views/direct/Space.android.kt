package com.lightningkite.rock.views.direct

import android.view.View
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.lparams

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = View

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit) {
    return viewElement(factory = ::NSpace, wrapper = ::Space) {
        handleTheme(native)  { it, native ->
            native.lparams.width = it.defaultSpacing.value.toInt()
            native.lparams.height = it.defaultSpacing.value.toInt()
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
            native.lparams.width = it.defaultSpacing.value.times(multiplier).toInt()
            native.lparams.height = it.defaultSpacing.value.times(multiplier).toInt()
        }
        setup(this)
    }
}