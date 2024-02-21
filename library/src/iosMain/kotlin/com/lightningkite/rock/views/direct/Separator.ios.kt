package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.px
import com.lightningkite.rock.views.*
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSeparator = UIView

@ViewDsl
actual fun ViewWriter.separatorActual(setup: Separator.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        backgroundColor = it.foreground.closestColor().toUiColor()
        alpha = 0.25
    }
    extensionSizeConstraints = SizeConstraints(minWidth = 1.px, minHeight = 1.px)
    setup(Separator(this))
}