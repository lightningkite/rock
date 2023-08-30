package com.lightningkite.mppexample


@ViewModifierDsl3
expect fun ViewContext.alignLeft(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignRight(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignCenter(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignTop(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.alignBottom(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.weight(amount: Float): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.padding(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Insets = Insets.zero()): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.margin(insets: Dimension): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.withBackground(background: Background): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrolls(): ViewWrapper

@ViewModifierDsl3
expect fun ViewContext.scrollsHorizontally(): ViewWrapper

@ViewDsl
expect fun ViewContext.column(setup: Column.() -> Unit = {}): Unit
expect class Column : NView

@ViewDsl
expect fun ViewContext.row(setup: Row.() -> Unit = {}): Unit
expect class Row : NView

@ViewDsl
expect fun ViewContext.text(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h1(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h2(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h3(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h4(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h5(setup: Text.() -> Unit = {}): Unit

@ViewDsl
expect fun ViewContext.h6(setup: Text.() -> Unit = {}): Unit

expect class Text : NViewWithTextStyle

expect var Text.content: String
expect var Text.textStyle: TextStyle
expect var Text.gravity: TextGravity


expect var NView.rotation: Angle
expect var NView.alpha: Double
expect var NView.elevation: Dimension
expect var NView.exists: Boolean
expect var NView.visible: Boolean

expect class Button : NView
expect var Button.variant: ButtonVariant
expect var Button.palette: ButtonPalette
expect var Button.size: ButtonSize

expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: () -> Unit)

expect class Image : NView

expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource
expect var Image.scaleType: ImageMode


//expect class Video: NView
//expect fun ViewContext.videoView(setup: Video.()->Unit = {}): Video

expect class ActivityIndicator : NView
typealias LoadingSpinner = ActivityIndicator

//typealias ProgressBar = ActivityIndicator
expect fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit
expect var ActivityIndicator.color: Color

expect class Space : NView

expect fun ViewContext.space(setup: Space.() -> Unit = {}): Unit
expect var Space.size: SizeConstraints

expect class TextField : NViewWithTextStyle
typealias TextInput = TextField

expect fun ViewContext.textField(setup: TextField.() -> Unit = {}): Unit
expect fun TextField.bind(text: Writable<String>): Unit

expect var TextField.textStyle: TextStyle
expect var TextField.keyboardHints: KeyboardHints
expect var TextField.hint: String
expect var TextField.validation: InputValidation
expect var TextField.key: String
expect var TextField.variant: TextFieldVariant

expect class DropDown : NView
typealias Spinner = DropDown

expect fun ViewContext.dropDown(setup: DropDown.() -> Unit = {}): Unit
expect fun <T> DropDown.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
): Unit

expect class RadioButton : NView

expect fun ViewContext.radioButton(setup: RadioButton.() -> Unit = {}): Unit
expect fun <T> RadioButton.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T>,
): Unit

//
//expect class SeekBar: NView
//expect fun ViewContext.seekBar(setup: SeekBar.()->Unit = {}): Unit
//expect var SeekBar.thumbTint: Color
//expect var SeekBar.progressTint: Color
//expect var SeekBar.max: Double
//expect var SeekBar.min: Double
//expect var SeekBar.progressBackgroundTint: Color
//expect val SeekBar.value: Writable<Double>
//
//expect class TabLayout: NView
//expect fun ViewContext.tabLayout(setup: TabLayout.()->Unit = {}): Unit
//expect var TabLayout.scrolls: Boolean
//expect var TabLayout.tabs: List<Tab>
//
//expect class ToggleButton: NView
//expect fun ViewContext.toggleButton(setup: ToggleButton.()->Unit = {}): Unit
//expect var ToggleButton.text: String
//expect var ToggleButton.textStyle: TextStyle
//expect var ToggleButton.gravity: String
//expect val ToggleButton.checked: Writable<Boolean>
//

expect class Switch: NView
expect fun ViewContext.switch(setup: Switch.()->Unit = {}): Unit
expect fun Switch.bind(checked: Writable<Boolean>): Unit

expect class CheckBox : NView

expect fun ViewContext.checkBox(setup: CheckBox.() -> Unit = {}): Unit
expect fun CheckBox.bind(checked: Writable<Boolean>): Unit

expect class Box : NView

expect fun ViewContext.box(setup: Box.() -> Unit = {}): Unit

//expect class RecyclerView: NView
//expect fun ViewContext.recyclerView(setup: RecyclerView.()->Unit = {}): Unit
//
//expect class Pager: NView
//expect fun ViewContext.pager(setup: Pager.()->Unit = {}): Unit
//
expect class WebView : NView

expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String

expect class AutoCompleteTextView : NView

expect fun ViewContext.autoCompleteTextView(setup: AutoCompleteTextView.() -> Unit = {}): Unit

expect fun <T> AutoCompleteTextView.bind(
    options: ReactiveScope.() -> List<T>,
    getLabel: (T) -> String,
    getKey: (T) -> String,
    prop: Writable<T?>,
): Unit

expect var AutoCompleteTextView.label: String
expect var AutoCompleteTextView.textStyle: TextStyle
expect var AutoCompleteTextView.labelStyle: TextStyle

expect class Form : NView

expect fun ViewContext.form(setup: Form.() -> Unit = {}): Unit
expect fun <T : MutableMap<String, Any>> Form.bind(
    prop: Writable<T>, onSubmit: (T) -> Unit
): Unit
