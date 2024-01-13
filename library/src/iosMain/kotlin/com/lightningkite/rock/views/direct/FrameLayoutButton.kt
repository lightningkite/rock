@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.QuartzCore.CATextLayer
import platform.UIKit.*
import platform.darwin.sel_registerName
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class FrameLayoutButton: UIButton(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }
//    val debugLayer = CATextLayer().apply {
//        layer.addSublayer(this)
//        frame = CGRectMake(0.0, 0.0, 200.0, 20.0)
//        fontSize = 8.0
//        foregroundColor = UIColor.redColor.CGColor
//    }
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size)/*.also {
        if(debugMeasuring) debugLayer.string = size.useContents { "${width.toInt()} x ${height.toInt()}" } + " -> " + it.useContents { "${width.toInt()} x ${height.toInt()}" }
    }*/
    override fun layoutSubviews() = frameLayoutLayoutSubviews()
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(view)

//    init {
//        addTarget(this, sel_registerName("test"), UIControlEventTouchUpInside)
//    }
//
//    @ObjCAction
//    fun test() {
//        println("test")
//    }
}