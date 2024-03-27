package com.lightningkite.kiteui.views

import com.lightningkite.kiteui.objc.UIViewWithSizeOverridesProtocol
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun UIView.informParentOfSizeChange() {
    (superview as? UIViewWithSizeOverridesProtocol)?.subviewDidChangeSizing(this)
    setNeedsLayout()
}