package com.lightningkite.rock.reactive

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
actual object SoftInputOpen : Readable<Boolean>, Writable<Boolean> {
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

    private val listeners = ArrayList<() -> Unit>()
    var value: Boolean = false
        set(value) {
            field = value
            listeners.toList().forEach { it() }
        }

    override suspend infix fun set(value: Boolean) {
        this.value = value
    }

    override suspend fun awaitRaw(): Boolean = value

    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return {
            val pos = listeners.indexOfFirst { it === listener }
            if(pos != -1) {
                listeners.removeAt(pos)
            }
        }
    }
}