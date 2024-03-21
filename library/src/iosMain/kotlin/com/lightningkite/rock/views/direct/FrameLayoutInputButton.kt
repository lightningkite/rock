@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import platform.objc.sel_registerName
import com.lightningkite.rock.objc.UIResponderWithOverridesProtocol
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.objc.UIViewWithSpacingRulesProtocol
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.reactive.Property

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class FrameLayoutInputButton: UIButton(CGRectZero.readValue()), UIResponderWithOverridesProtocol,
    UIViewWithSizeOverridesProtocol, UIViewWithSpacingRulesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    val spacingOverride: Property<Dimension?> = Property<Dimension?>(null)
    override fun getSpacingOverrideProperty() = spacingOverride
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

    val toolbar = UIToolbar().apply {
        barStyle = UIBarStyleDefault
        setTranslucent(true)
        sizeToFit()
        setItems(listOf(
            UIBarButtonItem(barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemFlexibleSpace, target = null, action = null),
            UIBarButtonItem(title = "Done", style = UIBarButtonItemStyle.UIBarButtonItemStylePlain, target = this, action = sel_registerName("done")),
        ), animated = false)
        onEvent(UIControlEventTouchUpInside) {
            becomeFirstResponder()
        }
    }

    @ObjCAction
    fun done() {
        resignFirstResponder()
        calculationContext.launch { action?.onSelect?.invoke() }
    }

    override fun inputAccessoryView(): UIView? = null
    var _inputView: UIView? = null
    override fun inputView(): UIView? = _inputView
    override fun canBecomeFirstResponder(): Boolean = true
    var currentValue: Property<*>? = null
    var valueRange: ClosedRange<*>? = null
    var action: Action? = null
        set(value) {
            field = value
            toolbar.setItems(listOf(
                UIBarButtonItem(barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemFlexibleSpace, target = null, action = null),
                UIBarButtonItem(title = value?.title ?: "Done", style = UIBarButtonItemStyle.UIBarButtonItemStylePlain, target = this, action = sel_registerName("done")),
            ), animated = false)
        }

    init {
        setUserInteractionEnabled(true)
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return frameLayoutHitTest(point, withEvent)
    }
}
