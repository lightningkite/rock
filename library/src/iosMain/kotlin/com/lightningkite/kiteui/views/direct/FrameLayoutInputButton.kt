@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.launch
import com.lightningkite.kiteui.models.Action
import com.lightningkite.kiteui.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import platform.objc.sel_registerName
import com.lightningkite.kiteui.objc.UIResponderWithOverridesProtocol
import com.lightningkite.kiteui.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.kiteui.objc.UIViewWithSpacingRulesProtocol
import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.reactive.Property

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class FrameLayoutInputButton: UIButton(CGRectZero.readValue()), UIResponderWithOverridesProtocol,
    UIViewWithSizeOverridesProtocol, UIViewWithSpacingRulesProtocol {
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
