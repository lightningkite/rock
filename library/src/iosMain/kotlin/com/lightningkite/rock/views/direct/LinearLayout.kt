@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import objcnames.classes.Protocol
import platform.CoreGraphics.*
import platform.Foundation.NSCoder
import platform.QuartzCore.CALayer
import platform.QuartzCore.CATransform3D
import platform.UIKit.*
import platform.darwin.NSInteger
import platform.darwin.NSUInteger
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class LinearLayout: UIView(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    var horizontal: Boolean = true
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

//    init { setUserInteractionEnabled(false) }

    override fun subviewDidChangeSizing(view: UIView?) {
        val it = view ?: return
//        if(it.hidden) return
//        it.extensionSizeConstraints?.takeIf { it.primary != null && it.secondary != null }?.let {
//            return
//        }
//        val m = it.extensionMargin ?: 0.0
//        if(it.extensionWeight != null && it.extensionWeight!! > 0.0 && it.secondaryAlign.let { it == null || it == Align.Stretch }) {
//            return
//        }
        informParentOfSizeChange()
    }

    data class Size(var primary: Double = 0.0, var secondary: Double = 0.0, var margin: Double = 0.0) {
    }
    val Size.objc get() = CGSizeMake(if(horizontal) primary else secondary, if(horizontal) secondary else primary)
    val CGSize.local get() = Size(if(horizontal) width else height, if(horizontal) height else width)
    val CValue<CGSize>.local get() = useContents { local }
    val SizeConstraints.primaryMax get() = if(horizontal) maxWidth else maxHeight
    val SizeConstraints.secondaryMax get() = if(horizontal) maxHeight else maxWidth
    val SizeConstraints.primaryMin get() = if(horizontal) minWidth else minHeight
    val SizeConstraints.secondaryMin get() = if(horizontal) minHeight else minWidth
    val SizeConstraints.primary get() = if(horizontal) width else height
    val SizeConstraints.secondary get() = if(horizontal) height else width
    val UIView.secondaryAlign get() = if(horizontal) extensionVerticalAlign else extensionHorizontalAlign

    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> {
        val size = size.local
        val measuredSize = Size()

        val sizes = calcSizes(size)
        measuredSize.primary += padding
        for (size in sizes) {
            measuredSize.primary += size.primary + size.margin * 2
            measuredSize.secondary = max(measuredSize.secondary, size.secondary + padding * 2 + size.margin * 2)
        }
        measuredSize.primary += padding

        return measuredSize.objc
    }

    fun calcSizes(size: Size): List<Size> {
//        let size = padding.shrinkSize(size)
        val remaining = size.copy()
        remaining.primary -= padding * 2
        remaining.secondary -= padding * 2

        var totalWeight = 0f
        return subviews.map {
            it as UIView
            if(it.hidden) return@map Size(0.0, 0.0)
//            it.extensionSizeConstraints?.takeIf { it.primary != null && it.secondary != null }?.let {
//                remaining.primary -= it.primary!!.value
//                return@map Size(primary = it.primary!!.value, secondary = it.secondary!!.value)
//            }
            val m = it.extensionMargin ?: 0.0
//            it.extensionWeight?.let { w ->
//                if(w > 0.0 && it.secondaryAlign.let { it == null || it == Align.Stretch }) {
//                    totalWeight += w
//                    return@map Size(primary = (-w).toDouble(), secondary = remaining.secondary, margin = m)
//                }
//            }
            val required = it.sizeThatFits(remaining.objc).local
            it.extensionSizeConstraints?.let {
                it.primaryMax?.let { required.primary = required.primary.coerceAtMost(it.value) }
                it.secondaryMax?.let { required.secondary = required.secondary.coerceAtMost(it.value) }
                it.primaryMin?.let { required.primary = required.primary.coerceAtLeast(it.value) }
                it.secondaryMin?.let { required.secondary = required.secondary.coerceAtLeast(it.value) }
                it.primary?.let { required.primary = it.value }
                it.secondary?.let { required.secondary = it.value }
            }
            required.margin = m
            required.primary = required.primary.coerceAtLeast(0.0)
            required.secondary = required.secondary.coerceAtLeast(0.0)

            remaining.secondary = remaining.secondary
            it.extensionWeight?.let { w ->
                totalWeight += w
                required.primary = (-w).toDouble()
            } ?: run {
                remaining.primary -= required.primary
            }
            remaining.primary -= m * 2
            required
        }.map {
            if(it.primary < -0.001) {
                if(totalWeight == 0f) throw IllegalStateException("View in LL has weight ${-it.primary} but total weight is ${totalWeight}")
                it.primary = (-it.primary / totalWeight) * remaining.primary
            }
            it
        }
    }

    override fun layoutSubviews() {
        val mySize = bounds.useContents { size.local }
        var primary = padding
        subviews.zip(calcSizes(frame.useContents { size.local })) { view, size ->
            view as UIView
            if(view.hidden) return@zip
            val m = view.extensionMargin ?: 0.0
            val ps = primary + m
            val a = view.secondaryAlign ?: Align.Stretch
            val offset = when(a) {
                Align.Start -> m + padding
                Align.Stretch -> m + padding
                Align.End -> mySize.secondary - m - padding - size.secondary
                Align.Center -> (mySize.secondary - size.secondary) / 2
            }
            val secondarySize = if(a == Align.Stretch) mySize.secondary - m * 2 - padding * 2 else size.secondary
            val oldSize = view.bounds.useContents { this.size.width to this.size.height }
            val widthSize = if(horizontal) size.primary else secondarySize
            val heightSize = if(horizontal) secondarySize else size.primary
            view.setFrame(
                CGRectMake(
                    if(horizontal) ps else offset,
                    if(horizontal) offset else ps,
                    widthSize,
                    heightSize,
                )
            )
            if(oldSize.first != widthSize || oldSize.second != heightSize) {
                view.layoutSubviews()
            }
            primary += size.primary + 2 * m
        }
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }

}