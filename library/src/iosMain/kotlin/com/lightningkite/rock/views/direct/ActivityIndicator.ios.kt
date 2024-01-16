package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.views.*
import platform.UIKit.UIActivityIndicatorView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NActivityIndicator = UIActivityIndicatorView

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit =
    element(UIActivityIndicatorView()) {
        hidden = false
        startAnimating()
        handleTheme(this) {
            this.color = it.foreground.closestColor().toUiColor()
        }
        extensionSizeConstraints = SizeConstraints(minWidth = 1.rem, minHeight = 1.rem)
        setup(ActivityIndicator(this))
    }