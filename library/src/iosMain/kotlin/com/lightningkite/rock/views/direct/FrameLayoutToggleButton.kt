@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.Writable
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
class FrameLayoutToggleButton: UIButton(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }
    var debugDescriptionInfo: String = ""
    var debugDescriptionInfo2: String = ""
    override fun debugDescription(): String? = "${super.debugDescription()} $debugDescriptionInfo $debugDescriptionInfo2"
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size).also {
        debugDescriptionInfo = size.useContents { "${width.toInt()} x ${height.toInt()}" } + " -> " + it.useContents { "${width.toInt()} x ${height.toInt()}" }
    }
    override fun layoutSubviews() = frameLayoutLayoutSubviews()
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(view)

    var allowUnselect = true
    var on: Boolean = false
        set(value) {
            field = value
            sendActionsForControlEvents(UIControlEventValueChanged)
        }
    init {
        onEvent(UIControlEventTouchUpInside) {
            if(!on || allowUnselect) {
                on = !on
                selected = on
                sendActionsForControlEvents(UIControlEventValueChanged)
            }
        }
    }

    val checkedWritable: Writable<Boolean> = object : Writable<Boolean> {
        override suspend fun awaitRaw(): Boolean = on
        override fun addListener(listener: () -> Unit): () -> Unit {
            return onEvent(UIControlEventValueChanged) { listener() }
        }

        override suspend fun set(value: Boolean) {
            on = value
        }
    }
}