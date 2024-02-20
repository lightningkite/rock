package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.times
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.extensionSizeConstraints
import com.lightningkite.rock.views.handleTheme
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSpace(): UIView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = CGSizeMake(0.0, 0.0)
}

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit): Unit = element(NSpace()) {
    handleTheme(this) {
        extensionSizeConstraints = SizeConstraints(
            minHeight = it.defaultSpacing,
            minWidth = it.defaultSpacing
        )
    }
    setup(Space(this))
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element(NSpace()) {
    handleTheme(this) {
        extensionSizeConstraints = SizeConstraints(
            minHeight = it.defaultSpacing * multiplier,
            minWidth = it.defaultSpacing * multiplier
        )
    }
    setup(Space(this))
}