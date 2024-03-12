package com.lightningkite.mppexampleapp

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.objc.toObjcId
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import platform.CoreGraphics.CGColorGetComponents
import platform.CoreGraphics.CGColorGetNumberOfComponents
import platform.CoreGraphics.CGColorRef
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSNumber
import platform.Foundation.numberWithFloat
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.UIColor
import platform.Foundation.NSArray
import platform.Foundation.NSMutableArray

var makeMeAGradient: ()->CAGradientLayer = { throw Exception() }

@OptIn(ExperimentalForeignApi::class)
actual fun ViewWriter.platformSpecific() {
    col {
        stack {
            text("This is a test of gradients")
            native.layer.addSublayer(makeMeAGradient())
        }
        stack {
            text("This is a test of gradients 2")
            native.layer.addSublayer(CAGradientLayer.layer().apply {
//                val x = NSMutableArray()
//                x.addObject(Color.red.toUiColor().CGColor)
//                x.addObject(Color.blue.toUiColor().CGColor)
//                colors = x as List<*>
                colors = listOf(
                    Color.red.toUiColor().CGColor!!.toObjcId(),
                    Color.blue.toUiColor().CGColor!!.toObjcId(),
                )
//                gradientLayerAddColor(this, Color.red.toUiColor().CGColor)
//                gradientLayerAddColor(this, Color.blue.toUiColor().CGColor)
//                colors = listOf(
//                    UIColor.redColor.CGColor,
//                    UIColor.blueColor.CGColor,
//                )
//                locations = listOf(
//                    NSNumber.numberWithFloat(0f),
//                    NSNumber.numberWithFloat(1f),
//                )
                frame = CGRectMake(0.0, 0.0, 100.0, 100.0)
            })
        }
    }
}