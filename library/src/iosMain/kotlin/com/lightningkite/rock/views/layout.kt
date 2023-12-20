package com.lightningkite.rock.views

import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun UIView.informParentOfSizeChange() {
    (superview as? UIViewWithSizeOverridesProtocol)?.subviewDidChangeSizing(this)
}