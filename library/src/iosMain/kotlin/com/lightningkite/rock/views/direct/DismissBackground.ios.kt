package com.lightningkite.rock.views.direct

import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.views.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UITapGestureRecognizer
import platform.darwin.NSObject
import platform.objc.sel_registerName

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = FrameLayoutButton

@ViewDsl
actual inline fun ViewWriter.dismissBackgroundActual(crossinline setup: DismissBackground.() -> Unit): Unit = element(FrameLayoutButton()) {
    handleTheme(this) {
        backgroundColor = it.background.closestColor().copy(alpha = 0.5f).toUiColor()
    }
    val d = DismissBackground(this)
    __dismissBackgroundOtherSetupX(navigator)
    setup(d)
    __dismissBackgroundOtherSetup()

}

fun FrameLayoutButton.__dismissBackgroundOtherSetupX(navigator: RockNavigator) {
    DismissBackground(this).onClick {
        navigator.dismiss()
    }
}
fun FrameLayoutButton.__dismissBackgroundOtherSetup() {
    listNViews().forEach {
        it.userInteractionEnabled = true
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    var virtualDisable: Boolean = false
    native.onEvent(UIControlEventTouchUpInside) {
        if(!virtualDisable) {
            native.calculationContext.launchManualCancel {
                try {
                    virtualDisable = true
                    action()
                } finally {
                    virtualDisable = false
                }
            }
        }
    }
}