package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.*
import platform.UIKit.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = UILabel

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 2)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.6)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.4)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.3)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.2)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.1)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.0)
    numberOfLines = 0
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.body
        it.body.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 0.8)
    numberOfLines = 0
    handleTheme(this) {
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