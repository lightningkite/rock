@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.NSURL
import platform.QuartzCore.CATextLayer
import platform.UIKit.*
import platform.objc.sel_registerName
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class NativeLink: UIButton(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    private val sizeCache: MutableMap<Size, List<Size>> = HashMap()
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size, sizeCache)
    override fun layoutSubviews() = frameLayoutLayoutSubviews(sizeCache)
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(view, sizeCache)
    override fun didAddSubview(subview: UIView) {
        super.didAddSubview(subview)
        sizeCache.clear()
    }
    override fun willRemoveSubview(subview: UIView) {
        // Fixes a really cursed crash where "this" is null
        this?.sizeCache?.clear()
        super.willRemoveSubview(subview)
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