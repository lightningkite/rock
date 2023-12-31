@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.NSURL
import platform.UIKit.*
import platform.objc.sel_registerName
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class NativeLink: UIButton(CGRectZero.readValue()) {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> {
        return frameLayoutSizeThatFits(size)
    }

    override fun layoutSubviews() {
        frameLayoutLayoutSubviews()
    }

    var toScreen: RockScreen? = null
    var onNavigator: RockNavigator? = null
    var toUrl: String? = null
    var newTab: Boolean = false

    init {
        addTarget(this, sel_registerName("clicked"), UIControlEventTouchUpInside)
    }

    @ObjCAction fun clicked() {
        toScreen?.let { onNavigator?.navigate(it) }
        toUrl?.let { UIApplication.sharedApplication.openURL(NSURL(string = it)) }
    }

}