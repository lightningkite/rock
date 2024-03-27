@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.objc.UIViewWithSpacingRulesProtocol
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.ReadableState
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
class FrameLayoutToggleButton: UIButton(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol, UIViewWithSpacingRulesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    val spacingOverride: Property<Dimension?> = Property<Dimension?>(null)
    override fun getSpacingOverrideProperty() = spacingOverride
    private val childSizeCache: ArrayList<HashMap<Size, Size>> = ArrayList()
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size, childSizeCache)
    override fun layoutSubviews() = frameLayoutLayoutSubviews(childSizeCache)
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(view, childSizeCache)
    override fun didAddSubview(subview: UIView) {
        super.didAddSubview(subview)
        frameLayoutDidAddSubview(subview, childSizeCache)
    }
    override fun willRemoveSubview(subview: UIView) {
        if(this != null) frameLayoutWillRemoveSubview(subview, childSizeCache)
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
        override val state: ReadableState<Boolean> get() = ReadableState(on)
        override fun addListener(listener: () -> Unit): () -> Unit {
            return onEvent(UIControlEventValueChanged) { listener() }
        }

        override suspend fun set(value: Boolean) {
            on = value
        }
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return frameLayoutHitTest(point, withEvent)
    }
}