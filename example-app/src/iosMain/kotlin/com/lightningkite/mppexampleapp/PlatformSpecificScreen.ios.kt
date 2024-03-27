package com.lightningkite.mppexampleapp

import com.lightningkite.kiteui.contains
import com.lightningkite.kiteui.models.Align
import com.lightningkite.kiteui.models.Color
import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.models.rem
import com.lightningkite.kiteui.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.kiteui.objc.UIViewWithSpacingRulesProtocol
import com.lightningkite.kiteui.objc.toObjcId
import com.lightningkite.kiteui.printStackTrace2
import com.lightningkite.kiteui.reactive.*
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*
import com.lightningkite.kiteui.views.l2.lazyExpanding
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.UIScrollView
import platform.UIKit.UIScrollViewDelegateProtocol
import platform.UIKit.UIView
import kotlin.math.absoluteValue

@OptIn(ExperimentalForeignApi::class)
actual fun ViewWriter.platformSpecific() {
    col {
        text("TEST")
    }
}
