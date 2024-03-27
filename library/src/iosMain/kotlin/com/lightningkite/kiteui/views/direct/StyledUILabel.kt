@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Align
import com.lightningkite.kiteui.models.SizeConstraints
import com.lightningkite.kiteui.views.*
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.UIKit.*
import kotlin.math.max

//private val UIViewLayoutParams = ExtensionProperty<UIView, LayoutParams>()
//val UIView.layoutParams: LayoutParams by UIViewLayoutParams
//
//class LayoutParams()

@OptIn(ExperimentalForeignApi::class)
class StyledUILabel: UILabel(CGRectZero.readValue()) {
    init { numberOfLines = -1 }
}