package com.lightningkite.rock.views

import com.lightningkite.rock.models.Align
import platform.UIKit.*

actual fun NView.removeNView(child: NView) {
    child.removeFromSuperview()
    child.shutdown()
}

actual fun NView.listNViews(): List<NView> {
    return subviews.map { it as UIView }
}
