@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.datetime.*
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSeparator = UIView

@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit = todo("separator")

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = UIView

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = false
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = true
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = UIView

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit): Unit = todo("link")
actual inline var Link.to: RockScreen
    get() = TODO()
    set(value) {}
actual inline var Link.newTab: Boolean
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = UIView

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit): Unit = todo("externalLink")
actual inline var ExternalLink.to: String
    get() = TODO()
    set(value) {}
actual inline var ExternalLink.newTab: Boolean
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImage = UIView

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit): Unit = todo("image")
actual inline var Image.source: ImageSource
    get() = TODO()
    set(value) {}
actual inline var Image.scaleType: ImageScaleType
    get() = TODO()
    set(value) {}
actual inline var Image.description: String?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = UILabel

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = element(StyledUILabel()) {
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
    }
    setup(TextView(this))
}
actual inline var TextView.content: String
    get() = native.text ?: ""
    set(value) {
        native.text = value
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
        native.font = UIFont.systemFontOfSize(value.value)
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = UIView

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit): Unit = todo("label")
actual inline var Label.content: String
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NActivityIndicator = UIView

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit = todo("activityIndicator")

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = UIView

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit): Unit = todo("space")
actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = UIView

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit): Unit = todo("dismissBackground")
actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = UIView

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit): Unit = todo("button")
actual fun Button.onClick(action: suspend () -> Unit): Unit = TODO()
actual inline var Button.enabled: Boolean
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = UIView

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit): Unit = todo("checkbox")
actual inline var Checkbox.enabled: Boolean
    get() = TODO()
    set(value) {}
actual val Checkbox.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = UIView

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit): Unit = todo("radioButton")
actual inline var RadioButton.enabled: Boolean
    get() = TODO()
    set(value) {}
actual val RadioButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = UIView

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit): Unit = todo("switch")
actual inline var Switch.enabled: Boolean
    get() = TODO()
    set(value) {}
actual val Switch.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = UIView

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit): Unit = todo("toggleButton")
actual inline var ToggleButton.enabled: Boolean
    get() = TODO()
    set(value) {}
actual val ToggleButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = UIView

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit = todo("radioToggleButton")
actual inline var RadioToggleButton.enabled: Boolean
    get() = TODO()
    set(value) {}
actual val RadioToggleButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = UIView

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit): Unit = todo("localDateField")
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalDateField.content: Writable<LocalDate?> get() = Property(null)
actual inline var LocalDateField.range: ClosedRange<LocalDate>?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = UIView

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit): Unit = todo("localTimeField")
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalTimeField.content: Writable<LocalTime?> get() = Property(null)
actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = UIView

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit): Unit = todo("localDateTimeField")
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalDateTimeField.content: Writable<LocalDateTime?> get() = Property(null)
actual inline var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = UIView

@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit): Unit = todo("textField")
actual val TextField.content: Writable<String> get() = Property("")
actual inline var TextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {}
actual var TextField.action: Action?
    get() = TODO()
    set(value) {}
actual inline var TextField.hint: String
    get() = TODO()
    set(value) {}
actual inline var TextField.range: ClosedRange<Double>?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = UIView

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit): Unit = todo("textArea")
actual val TextArea.content: Writable<String> get() = Property("")
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {}
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = UIView

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit): Unit = todo("select")
actual val Select.selected: Writable<String?> get() = Property(null)
actual inline var Select.options: List<WidgetOption>
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = UIView

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit): Unit =
    todo("autoCompleteTextField")

actual val AutoCompleteTextField.content: Writable<String> get() = Property("")
actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {}
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {}
actual inline var AutoCompleteTextField.suggestions: List<String>
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = UIView

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit): Unit = todo("swapView")
@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = todo("swapViewDialog")
actual fun SwapView.swap(transition: ScreenTransition, createNewView: () -> Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = UIView

@ViewDsl
actual fun ViewWriter.webView(setup: WebView.() -> Unit): Unit = todo("webView")
actual inline var WebView.url: String
    get() = TODO()
    set(value) {}
actual inline var WebView.permitJs: Boolean
    get() = TODO()
    set(value) {}
actual inline var WebView.content: String
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = UIView

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = todo("canvas")
actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit = TODO()
actual val Canvas.width: Readable<Double> get() = Property(0.0)
actual val Canvas.height: Readable<Double> get() = Property(0.0)
actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit =
    TODO()

actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit =
    TODO()

actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit =
    TODO()

actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit =
    TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = UIView

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit = todo("recyclerView")
@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit = todo("horizontalRecyclerView")
@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit = todo("gridRecyclerView")
actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
        TODO()
    }

actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>) -> Unit): Unit =
    TODO()

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit
): ViewWrapper = TODO()

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    val parent = this.currentView
    this.beforeNextElementSetup {
        this.extensionWeight = amount
    }
    return ViewWrapper
}
@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        extensionHorizontalAlign = horizontal
        extensionVerticalAlign = vertical
    }
    return ViewWrapper
}
@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper get() = ViewWrapper
@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper get() = ViewWrapper
@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {
        extensionSizeConstraints = constraints
    }
    return ViewWrapper
}
@ViewModifierDsl3
actual val ViewWriter.marginless: ViewWrapper get() = ViewWrapper
@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper get() = ViewWrapper
// End
