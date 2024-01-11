package com.lightningkite.rock.views

import platform.UIKit.*

actual fun NView.removeNView(child: NView) {
    child.removeFromSuperview()
    child.shutdown()
}

actual fun NView.listNViews(): List<NView> {
    return subview.map { it as UIView }
}