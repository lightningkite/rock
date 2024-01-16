package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.*
import platform.UIKit.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = UILabel

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 2)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 2.0 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 2.0).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.6)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.6 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.6).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.4)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.4 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.4).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.3)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.3 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.3).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.2)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.2 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.2).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.1)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.1 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.1).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.0)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 1.0 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 1.0).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.body
        it.body.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 0.8)
//    extensionSizeConstraints = SizeConstraints(
//        minWidth = (UIFont.systemFontSize * 0.8 * 4).dp,
//        minHeight = (UIFont.systemFontSize * 0.8).dp,
//    )
    numberOfLines = 0
    handleTheme(this, viewLoads = true) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.body
        it.body.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

actual inline var TextView.content: String
    get() = native.text ?: ""
    set(value) {
        native.text = value
        native.informParentOfSizeChange()
    }
actual inline var TextView.align: Align
    get() = when (native.textAlignment) {
        NSTextAlignmentLeft -> Align.Start
        NSTextAlignmentCenter -> Align.Center
        NSTextAlignmentRight -> Align.End
        NSTextAlignmentJustified -> Align.Stretch
        else -> Align.Start
    }
    set(value) {
        native.contentMode = when (value) {
            Align.Start -> UIViewContentMode.UIViewContentModeLeft
            Align.Center -> UIViewContentMode.UIViewContentModeCenter
            Align.End -> UIViewContentMode.UIViewContentModeRight
            Align.Stretch -> UIViewContentMode.UIViewContentModeScaleAspectFit
        }
        native.textAlignment = when (value) {
            Align.Start -> NSTextAlignmentLeft
            Align.Center -> NSTextAlignmentCenter
            Align.End -> NSTextAlignmentRight
            Align.Stretch -> NSTextAlignmentJustified
        }
    }
actual inline var TextView.textSize: Dimension
    get() = Dimension(native.font.pointSize)
    set(value) {
        native.extensionFontAndStyle?.let {
            native.font = it.font.get(value.value, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic)
        }
    }