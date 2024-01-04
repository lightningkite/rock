@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import platform.objc.sel_registerName
import com.lightningkite.rock.reactive.Property

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class TextFieldInput: UITextField(CGRectZero.readValue()) {

    val toolbar = UIToolbar().apply {
        barStyle = UIBarStyleDefault
        setTranslucent(true)
        sizeToFit()
        setItems(listOf(
            UIBarButtonItem(barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemFlexibleSpace, target = null, action = null),
            UIBarButtonItem(title = "Done", style = UIBarButtonItemStyle.UIBarButtonItemStylePlain, target = this@TextFieldInput, action = sel_registerName("done")),
        ), animated = false)
    }
    init {
        inputAccessoryView = toolbar
        onEvent(UIControlEventTouchUpInside) {
            becomeFirstResponder()
            println("I am a first responder! $isFirstResponder $canBecomeFirstResponder ${this.canBecomeFirstResponder()} $canResignFirstResponder")
        }
    }

    @ObjCAction
    fun done() {
        resignFirstResponder()
        calculationContext.launch { action?.onSelect?.invoke() }
    }

    var currentValue: Property<*>? = null
    var valueRange: ClosedRange<*>? = null
    var action: Action? = null
        set(value) {
            field = value
            toolbar.setItems(listOf(
                UIBarButtonItem(barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemFlexibleSpace, target = null, action = null),
                UIBarButtonItem(title = value?.title ?: "Done", style = UIBarButtonItemStyle.UIBarButtonItemStylePlain, target = this@TextFieldInput, action = sel_registerName("done")),
            ), animated = false)
        }

    init {
        setUserInteractionEnabled(true)
    }

    override fun caretRectForPosition(position: UITextPosition): CValue<CGRect> = CGRectMake(0.0, 0.0, 0.0, 0.0)
}
