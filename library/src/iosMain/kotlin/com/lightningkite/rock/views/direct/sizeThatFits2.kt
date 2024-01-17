package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.SizeConstraints
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIImageView
import platform.UIKit.UIScreen
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun UIView.sizeThatFits2(size: CValue<CGSize>, sizeConstraints: SizeConstraints?): CValue<CGSize> {
    val newSize = sizeConstraints?.let {
        var w = size.useContents { width }
        var h = size.useContents { height }
        it.maxWidth?.let { w = w.coerceAtMost(it.value) }
        it.maxHeight?.let { h = h.coerceAtMost(it.value) }
        it.minWidth?.let { w = w.coerceAtLeast(it.value) }
        it.minHeight?.let { h = h.coerceAtLeast(it.value) }
        it.width?.let { w = it.value }
        it.height?.let { h = it.value }
        CGSizeMake(w, h)
    } ?: size
    return if(this is UIImageView) {
        this.image?.size?.useContents {
            val original = this
            newSize.useContents {
                val max = this
                val smallerRatio = (max.width / original.width)
                    .coerceAtMost(max.height / original.height)
                CGSizeMake(
                    (original.width * smallerRatio.coerceAtMost(1 / UIScreen.mainScreen.scale)),
                    (original.height * smallerRatio.coerceAtMost(1 / UIScreen.mainScreen.scale))
                )
            }
        } ?: CGSizeMake(0.0, 0.0)
    } else sizeThatFits(newSize)
}