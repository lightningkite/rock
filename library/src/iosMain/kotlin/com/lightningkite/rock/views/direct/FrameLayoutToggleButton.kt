@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class FrameLayoutToggleButton: UIButton(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    var spacingMultiplier: Double = 1.0
        set(value) {
            field = value
            sizeCache.clear()
            informParentOfSizeChange()
        }

    private val sizeCache: MutableMap<Size, List<Size>> = HashMap()
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(spacingMultiplier, size, sizeCache)
    override fun layoutSubviews() = frameLayoutLayoutSubviews(spacingMultiplier, sizeCache)
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(spacingMultiplier, view, sizeCache)
    override fun didAddSubview(subview: UIView) {
        super.didAddSubview(subview)
        sizeCache.clear()
    }
    override fun willRemoveSubview(subview: UIView) {
        // Fixes a really cursed crash where "this" is null
        this?.sizeCache?.clear()
        super.willRemoveSubview(subview)
    }

    var allowUnselect = true
    var on: Boolean = false
        set(value) {
            field = value
            sendActionsForControlEvents(UIControlEventValueChanged)
        }
    init {
        onEvent(UIControlEventTouchUpInside) {
            if(!on || allowUnselect) {
                on = !on
                selected = on
                sendActionsForControlEvents(UIControlEventValueChanged)
            }
        }
    }

    val checkedWritable: Writable<Boolean> = object : Writable<Boolean> {
        override suspend fun awaitRaw(): Boolean = on
        override fun addListener(listener: () -> Unit): () -> Unit {
            return onEvent(UIControlEventValueChanged) { listener() }
        }

        override suspend fun set(value: Boolean) {
            on = value
        }
    }
}