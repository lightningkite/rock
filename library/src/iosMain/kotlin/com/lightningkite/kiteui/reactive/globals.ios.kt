package com.lightningkite.kiteui.reactive

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotificationCenter
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.UIKit.UIKeyboardWillChangeFrameNotification
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification
import platform.UIKit.UIWindow
import platform.darwin.NSObject
import platform.darwin.sel_registerName

@OptIn(ExperimentalForeignApi::class)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual object SoftInputOpen : BaseImmediateReadable<Boolean>(false), Writable<Boolean> {
    @OptIn(BetaInteropApi::class)
    val observer: NSObject = object: NSObject() {
        @ObjCAction fun keyboardWillShowNotification() {
            value = true
        }
        @ObjCAction fun keyboardWillHideNotification() {
            value = false
        }
    }
    init {
        NSNotificationCenter.defaultCenter.addObserver(
            observer = observer,
            selector = sel_registerName("keyboardWillShowNotification"),
            name = UIKeyboardWillShowNotification,
            `object` = null
        )
        NSNotificationCenter.defaultCenter.addObserver(
            observer = observer,
            selector = sel_registerName("keyboardWillHideNotification"),
            name = UIKeyboardWillHideNotification,
            `object` = null
        )
    }
    override suspend infix fun set(value: Boolean) {
        this.value = value
    }
}