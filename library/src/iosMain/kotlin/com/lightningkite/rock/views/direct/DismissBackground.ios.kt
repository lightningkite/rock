package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.objc.sel_registerName

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = FrameLayout

@ViewDsl
actual inline fun ViewWriter.dismissBackgroundActual(crossinline setup: DismissBackground.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this) {
        backgroundColor = it.background.closestColor().copy(alpha = 0.5f).toUiColor()
    }
    val d = DismissBackground(this)
    d.onClick {
        navigator.dismiss()
    }
    setup(d)
    listNViews().forEach {
        it.userInteractionEnabled = true
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    val actionHolder = object : NSObject() {
        @ObjCAction
        fun eventHandler() = launch(action)
    }
    val rec = UITapGestureRecognizer(actionHolder, sel_registerName("eventHandler"))
    native.addGestureRecognizer(rec)
    calculationContext.onRemove {
        // Retain the sleeve until disposed
        rec.enabled
        actionHolder.description
    }
}