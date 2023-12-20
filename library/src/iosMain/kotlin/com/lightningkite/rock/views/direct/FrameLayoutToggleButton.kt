@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class FrameLayoutToggleButton: UIButton(CGRectZero.readValue()) {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> {
        return frameLayoutSizeThatFits(size)
    }

    override fun layoutSubviews() {
        frameLayoutLayoutSubviews()
    }

    var allowUnselect = true
    var on: Boolean = false
    init {
        onEvent(UIControlEventTouchUpInside) {
            if(!on || allowUnselect) {
                on = !on
                selected = on
                sendActionsForControlEvents(UIControlEventValueChanged)
            }
        }
    }
}