package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlinx.datetime.*


@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NSeparator = HTMLElement
@ViewDsl actual fun ViewWriter.separatorActual(setup: Separator.() -> Unit): Unit = todo("separator")

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NContainingView = HTMLElement
@ViewDsl actual fun ViewWriter.stackActual(setup: ContainingView.() -> Unit): Unit = todo("stack")
@ViewDsl actual fun ViewWriter.colActual(setup: ContainingView.() -> Unit): Unit = todo("col")
@ViewDsl actual fun ViewWriter.rowActual(setup: ContainingView.() -> Unit): Unit = todo("row")

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NLink = HTMLElement
@ViewDsl actual fun ViewWriter.linkActual(setup: Link.() -> Unit): Unit = todo("link")
actual inline var Link.to: RockScreen
    get() = TODO()
    set(value) { }
actual inline var Link.newTab: Boolean
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NExternalLink = HTMLElement
@ViewDsl actual fun ViewWriter.externalLinkActual(setup: ExternalLink.() -> Unit): Unit = todo("externalLink")
actual inline var ExternalLink.to: String
    get() = TODO()
    set(value) { }
actual inline var ExternalLink.newTab: Boolean
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NImageView = HTMLElement
@ViewDsl actual fun ViewWriter.imageActual(setup: ImageView.() -> Unit): Unit = todo("image")
actual inline var ImageView.source: ImageSource?
    get() = TODO()
    set(value) { }
actual inline var ImageView.scaleType: ImageScaleType
    get() = TODO()
    set(value) { }
actual inline var ImageView.description: String?
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NTextView = HTMLElement
@ViewDsl actual fun ViewWriter.h1Actual(setup: TextView.() -> Unit): Unit = todo("h1")
@ViewDsl actual fun ViewWriter.h2Actual(setup: TextView.() -> Unit): Unit = todo("h2")
@ViewDsl actual fun ViewWriter.h3Actual(setup: TextView.() -> Unit): Unit = todo("h3")
@ViewDsl actual fun ViewWriter.h4Actual(setup: TextView.() -> Unit): Unit = todo("h4")
@ViewDsl actual fun ViewWriter.h5Actual(setup: TextView.() -> Unit): Unit = todo("h5")
@ViewDsl actual fun ViewWriter.h6Actual(setup: TextView.() -> Unit): Unit = todo("h6")
@ViewDsl actual fun ViewWriter.textActual(setup: TextView.() -> Unit): Unit = todo("text")
@ViewDsl actual fun ViewWriter.subtextActual(setup: TextView.() -> Unit): Unit = todo("subtext")
actual inline var TextView.content: String
    get() = TODO()
    set(value) { }
actual inline var TextView.align: Align
    get() = TODO()
    set(value) { }
actual inline var TextView.textSize: Dimension
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NLabel = HTMLElement
@ViewDsl actual fun ViewWriter.labelActual(setup: Label.() -> Unit): Unit = todo("label")
actual inline var Label.content: String
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NActivityIndicator = HTMLElement
@ViewDsl actual fun ViewWriter.activityIndicatorActual(setup: ActivityIndicator.() -> Unit): Unit = todo("activityIndicator")

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NSpace = HTMLElement
@ViewDsl actual fun ViewWriter.spaceActual(setup: Space.() -> Unit): Unit = todo("space")
actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NDismissBackground = HTMLElement
@ViewDsl actual fun ViewWriter.dismissBackgroundActual(setup: DismissBackground.() -> Unit): Unit = todo("dismissBackground")
actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NButton = HTMLElement
@ViewDsl actual fun ViewWriter.buttonActual(setup: Button.() -> Unit): Unit = todo("button")
actual fun Button.onClick(action: suspend () -> Unit): Unit = TODO()
actual inline var Button.enabled: Boolean
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NCheckbox = HTMLElement
@ViewDsl actual fun ViewWriter.checkboxActual(setup: Checkbox.() -> Unit): Unit = todo("checkbox")
actual inline var Checkbox.enabled: Boolean
    get() = TODO()
    set(value) { }
actual val Checkbox.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NRadioButton = HTMLElement
@ViewDsl actual fun ViewWriter.radioButtonActual(setup: RadioButton.() -> Unit): Unit = todo("radioButton")
actual inline var RadioButton.enabled: Boolean
    get() = TODO()
    set(value) { }
actual val RadioButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NSwitch = HTMLElement
@ViewDsl actual fun ViewWriter.switchActual(setup: Switch.() -> Unit): Unit = todo("switch")
actual inline var Switch.enabled: Boolean
    get() = TODO()
    set(value) { }
actual val Switch.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NToggleButton = HTMLElement
@ViewDsl actual fun ViewWriter.toggleButtonActual(setup: ToggleButton.() -> Unit): Unit = todo("toggleButton")
actual inline var ToggleButton.enabled: Boolean
    get() = TODO()
    set(value) { }
actual val ToggleButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NRadioToggleButton = HTMLElement
@ViewDsl actual fun ViewWriter.radioToggleButtonActual(setup: RadioToggleButton.() -> Unit): Unit = todo("radioToggleButton")
actual inline var RadioToggleButton.enabled: Boolean
    get() = TODO()
    set(value) { }
actual val RadioToggleButton.checked: Writable<Boolean> get() = Property(false)

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NLocalDateField = HTMLElement
@ViewDsl actual fun ViewWriter.localDateFieldActual(setup: LocalDateField.() -> Unit): Unit = todo("localDateField")
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalDateField.content: Writable<LocalDate?> get() = Property(null)
actual inline var LocalDateField.range: ClosedRange<LocalDate>?
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NLocalTimeField = HTMLElement
@ViewDsl actual fun ViewWriter.localTimeFieldActual(setup: LocalTimeField.() -> Unit): Unit = todo("localTimeField")
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalTimeField.content: Writable<LocalTime?> get() = Property(null)
actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NLocalDateTimeField = HTMLElement
@ViewDsl actual fun ViewWriter.localDateTimeFieldActual(setup: LocalDateTimeField.() -> Unit): Unit = todo("localDateTimeField")
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual val LocalDateTimeField.content: Writable<LocalDateTime?> get() = Property(null)
actual inline var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NTextField = HTMLElement
@ViewDsl actual fun ViewWriter.textFieldActual(setup: TextField.() -> Unit): Unit = todo("textField")
actual val TextField.content: Writable<String> get() = Property("")
actual inline var TextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) { }
actual var TextField.action: Action?
    get() = TODO()
    set(value) {}
actual inline var TextField.hint: String
    get() = TODO()
    set(value) { }
actual inline var TextField.range: ClosedRange<Double>?
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NTextArea = HTMLElement
@ViewDsl actual fun ViewWriter.textAreaActual(setup: TextArea.() -> Unit): Unit = todo("textArea")
actual val TextArea.content: Writable<String> get() = Property("")
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) { }
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NSelect = HTMLElement
@ViewDsl actual fun ViewWriter.selectActual(setup: Select.() -> Unit): Unit = todo("select")
actual fun <T> Select.bind(
    edits: Writable<T>,
    data: Readable<List<T>>,
    render: (T) -> String
) {
}

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NAutoCompleteTextField = HTMLElement
@ViewDsl actual fun ViewWriter.autoCompleteTextFieldActual(setup: AutoCompleteTextField.() -> Unit): Unit = todo("autoCompleteTextField")
actual val AutoCompleteTextField.content: Writable<String> get() = Property("")
actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) { }
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {}
actual inline var AutoCompleteTextField.suggestions: List<String>
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NSwapView = HTMLElement
@ViewDsl actual fun ViewWriter.swapViewActual(setup: SwapView.() -> Unit): Unit = todo("swapView")
@ViewDsl actual fun ViewWriter.swapViewDialogActual(setup: SwapView.() -> Unit): Unit = todo("swapViewDialog")
actual fun SwapView.swap(transition: ScreenTransition, createNewView: ViewWriter.()->Unit): Unit = TODO()

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NWebView = HTMLElement
@ViewDsl actual fun ViewWriter.webViewActual(setup: WebView.() -> Unit): Unit = todo("webView")
actual inline var WebView.url: String
    get() = TODO()
    set(value) { }
actual inline var WebView.permitJs: Boolean
    get() = TODO()
    set(value) { }
actual inline var WebView.content: String
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NCanvas = HTMLElement
@ViewDsl actual fun ViewWriter.canvasActual(setup: Canvas.() -> Unit): Unit = todo("canvas")
@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NRecyclerView = HTMLElement
@ViewDsl actual fun ViewWriter.recyclerViewActual(setup: RecyclerView.() -> Unit): Unit = todo("recyclerView")
@ViewDsl actual fun ViewWriter.horizontalRecyclerViewActual(setup: RecyclerView.() -> Unit): Unit = todo("horizontalRecyclerView")
actual var RecyclerView.columns: Int
    get() = 1
    set(value) { TODO() }
actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>)->Unit): Unit = TODO()
@ViewModifierDsl3 actual fun ViewWriter.hasPopover(requireClick: Boolean, preferredDirection: PopoverPreferredDirection, setup: ViewWriter.()->Unit): ViewWrapper = TODO()
@ViewModifierDsl3 actual fun ViewWriter.weight(amount: Float): ViewWrapper = TODO()
@ViewModifierDsl3 actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper = TODO()
@ViewModifierDsl3 actual val ViewWriter.scrolls: ViewWrapper get() = TODO()
@ViewModifierDsl3 actual val ViewWriter.scrollsHorizontally: ViewWrapper get() = TODO()
@ViewModifierDsl3 actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper = TODO()
@ViewModifierDsl3 actual val ViewWriter.marginless: ViewWrapper get() = TODO()
@ViewModifierDsl3 actual val ViewWriter.padded: ViewWrapper get() = TODO()
// End