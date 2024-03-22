package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.objc.UIViewWithSpacingRulesProtocol
import com.lightningkite.rock.objc.toObjcId
import com.lightningkite.rock.printStackTrace2
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.lazyExpanding
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
