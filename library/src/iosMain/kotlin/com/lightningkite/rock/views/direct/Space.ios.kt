package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.times
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.extensionSizeConstraints
import com.lightningkite.rock.views.handleTheme
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = UIView

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        extensionSizeConstraints = SizeConstraints(
            minHeight = it.spacing,
            minWidth = it.spacing
        )
    }
    setup(Space(this))
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        extensionSizeConstraints = SizeConstraints(
            minHeight = it.spacing * multiplier,
            minWidth = it.spacing * multiplier
        )
    }
    setup(Space(this))
}