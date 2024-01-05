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
import com.lightningkite.rock.reactive.Property

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class FrameLayoutInputButton: UIButton(CGRectZero.readValue()), UIResponderWithOverridesProtocol,
    UIViewWithSizeOverridesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size)
    override fun layoutSubviews() = frameLayoutLayoutSubviews()
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(view)

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
}
