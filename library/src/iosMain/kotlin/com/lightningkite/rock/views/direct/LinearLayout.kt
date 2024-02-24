@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.QuartzCore.CATextLayer
import platform.UIKit.*
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class LinearLayout: UIView(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    private val sizeCache = HashMap<Pair<Size, Boolean>, List<Size>>()
    var horizontal: Boolean = true
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }
    var gap: Double = 0.0
        set(value) {
            field = value
            debugDescriptionInfo2 = "(gap=$field)"
            setNeedsLayout()
            informParentOfSizeChange()
        }
    val spacingOverride: Property<Dimension?> = Property<Dimension?>(null).apply {
        addListener {
            value?.value?.let {
                gap = it
            }
        }
    }

//    init { setUserInteractionEnabled(false) }

//    val debugLayer = CATextLayer().apply {
//        layer.addSublayer(this)
//        frame = CGRectMake(0.0, 0.0, 200.0, 20.0)
//        fontSize = 8.0
//        foregroundColor = UIColor.redColor.CGColor
//    }
    var debugDescriptionInfo: String = ""
    var debugDescriptionInfo2: String = ""
    override fun debugDescription(): String? = "${super.debugDescription()} $debugDescriptionInfo $debugDescriptionInfo2"

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
        sizeCache.clear()
        informParentOfSizeChange()
    }

    data class Size(var primary: Double = 0.0, var secondary: Double = 0.0) {
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
        val sizeLocal = size.local
        val measuredSize = Size()

        val sizes = calcSizes(sizeLocal, false)
        measuredSize.primary += padding
        var first = true
        subviews.zip(sizes) { view, size ->
            view as UIView
            if(view.hidden) return@zip
            if(first) {
                first = false
            } else {
                measuredSize.primary += gap
            }
            measuredSize.primary += size.primary
            measuredSize.secondary = max(measuredSize.secondary, size.secondary + padding * 2)
        }
        measuredSize.primary += padding

        debugDescriptionInfo = size.useContents { "${width.toInt()} x ${height.toInt()}" } + " -> " + measuredSize.objc.useContents { "${width.toInt()} x ${height.toInt()} from ${sizes.joinToString { it.primary.toInt().toString() }}" }
        return measuredSize.objc
    }

    override fun didAddSubview(subview: UIView) {
        super.didAddSubview(subview)
        sizeCache.clear()
    }

    override fun willRemoveSubview(subview: UIView) {
        // Fixes a really cursed crash where "this" is null
        this?.sizeCache?.clear()
        super.willRemoveSubview(subview)
    }

    fun calcSizes(size: Size, includeWeighted: Boolean): List<Size> = sizeCache.getOrPut(size to includeWeighted) {
//        let size = padding.shrinkSize(size)
        val remaining = size.copy()
        remaining.primary -= padding * 2
        remaining.secondary -= padding * 2

        var totalWeight = 0f

        val out = arrayOfNulls<Size?>(subviews.size)

        var first = true
        subviews.forEachIndexed { index, it ->
            it as UIView
            if(it.hidden) {
                out[index] = Size(0.0, 0.0)
                return@forEachIndexed
            }
            if(first) {
                first = false
            } else {
                remaining.primary -= gap
            }
            it.extensionWeight?.let {
                totalWeight += it
                return@forEachIndexed
            }
            val required = it.sizeThatFits2(Size(remaining.primary, remaining.secondary).objc, it.extensionSizeConstraints).local
            it.extensionSizeConstraints?.let {
                it.primaryMax?.let { required.primary = required.primary.coerceAtMost(it.value) }
                it.secondaryMax?.let { required.secondary = required.secondary.coerceAtMost(it.value) }
                it.primaryMin?.let { required.primary = required.primary.coerceAtLeast(it.value) }
                it.secondaryMin?.let { required.secondary = required.secondary.coerceAtLeast(it.value) }
                it.primary?.let { required.primary = it.value }
                it.secondary?.let { required.secondary = it.value.coerceAtMost(remaining.secondary) }
            }
            required.primary = required.primary.coerceAtLeast(0.0)
            required.secondary = required.secondary.coerceAtLeast(0.0)
            remaining.primary -= required.primary
            out[index] = required
        }

        subviews.forEachIndexed { index, it ->
            it as UIView
            if(out[index] != null) return@forEachIndexed
            val w = it.extensionWeight!!.toDouble()
            val available = ((w / totalWeight) * remaining.primary).coerceAtLeast(0.0)
            val required = it.sizeThatFits2(Size(available, remaining.secondary).objc, it.extensionSizeConstraints).local
//            val required = it.sizeThatFits2(Size(1000.0, remaining.secondary - m * 2).objc, it.extensionSizeConstraints).local
            it.extensionSizeConstraints?.let {
                it.secondaryMax?.let { required.secondary = required.secondary.coerceAtMost(it.value) }
                it.secondaryMin?.let { required.secondary = required.secondary.coerceAtLeast(it.value) }
                it.secondary?.let { required.secondary = it.value }
            }
//            required.primary = if(includeWeighted) available else 0.0
            required.primary = if(includeWeighted) available else required.primary
            required.secondary = required.secondary.coerceAtLeast(0.0)
            out[index] = required
        }

        return out.map { it!! }
    }

    override fun layoutSubviews() {
        val mySize = bounds.useContents { size.local }
        var primary = padding
        val sizes = calcSizes(frame.useContents { size.local }, true)
        var first = true
        subviews.zip(sizes) { view, size ->
            view as UIView
            if(view.hidden) return@zip
            if(first) {
                first = false
            } else {
                primary += gap
            }
            val ps = primary
            val a = view.secondaryAlign ?: Align.Stretch
            val offset = when(a) {
                Align.Start -> padding
                Align.Stretch -> padding
                Align.End -> mySize.secondary - padding - size.secondary
                Align.Center -> (mySize.secondary - size.secondary) / 2
            }
            val secondarySize = if(a == Align.Stretch) mySize.secondary - padding * 2 else size.secondary
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
            primary += size.primary
        }
        primary += padding
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }
}
