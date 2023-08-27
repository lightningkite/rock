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

@ViewDsl
expect fun ViewContext.column(setup: Column.() -> Unit = {}): Unit
expect class Column : NView

@ViewDsl
expect fun ViewContext.row(setup: Row.() -> Unit = {}): Unit
expect class Row : NView

@ViewDsl
expect fun ViewContext.text(setup: Text.() -> Unit = {}): Unit
expect class Text : NView

expect var Text.text: String
expect var Text.textStyle: TextStyle
expect var Text.gravity: TextGravity
//expect var Text.maxLines: Double? // this isn't easy to do in CSS

//
//expect var NView.rotation: Double
//expect var NView.alpha: Double
//expect var NView.elevation: Dimension
//expect var NView.exists: Boolean
expect var NView.visible: Boolean

//
//expect class ImageButton: NView
//expect fun ViewContext.imageButton(setup: ImageButton.()->Unit = {}): ImageButton
//expect var ImageButton.src: ImageSource
//expect var ImageButton.scaleType: String
//expect var ImageButton.gravity: String
//
expect class Button : NView

expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
expect var Button.text: String

//expect var Button.textStyle: TextStyle
//expect var Button.gravity: String
expect fun Button.onClick(action: () -> Unit)

//
//
expect class Image : NView

expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource
expect var Image.scaleType: ImageMode

//
//expect class Video: NView
//expect fun ViewContext.videoView(setup: Video.()->Unit = {}): Video
//
//expect class ActivityIndicator: NView
//typealias ProgressBar = ActivityIndicator
//expect fun ViewContext.activityIndicator(setup: ActivityIndicator.()->Unit = {}): ActivityIndicator
//expect var ActivityIndicator.indeterminateTint: Color
//
//expect class Space: NView
//expect fun ViewContext.space(setup: Space.()->Unit = {}): Space
//
expect class EditText : NView
typealias TextInput = EditText

expect fun ViewContext.editText(setup: EditText.() -> Unit = {}): Unit
expect fun EditText.bind(text: Writable<String>): Unit

//expect var EditText.textStyle: TextStyle
//expect var EditText.keyboardHints: KeyboardHints
expect var EditText.hint: String

//
//expect class AutoCompleteTextView: NView
//expect fun ViewContext.autoCompleteTextView(setup: AutoCompleteTextView.()->Unit = {}): AutoCompleteTextView
//expect val AutoCompleteTextView.text: Writable<String>
//expect var AutoCompleteTextView.textStyle: TextStyle
//expect var AutoCompleteTextView.keyboardHints: KeyboardHints
//expect var AutoCompleteTextView.hint: String
//
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
//expect fun ViewContext.seekBar(setup: SeekBar.()->Unit = {}): SeekBar
//expect var SeekBar.thumbTint: Color
//expect var SeekBar.progressTint: Color
//expect var SeekBar.max: Double
//expect var SeekBar.min: Double
//expect var SeekBar.progressBackgroundTint: Color
//expect val SeekBar.value: Writable<Double>
//
//expect class TabLayout: NView
//expect fun ViewContext.tabLayout(setup: TabLayout.()->Unit = {}): TabLayout
//expect var TabLayout.scrolls: Boolean
//expect var TabLayout.tabs: List<Tab>
//
//expect class ToggleButton: NView
//expect fun ViewContext.toggleButton(setup: ToggleButton.()->Unit = {}): ToggleButton
//expect var ToggleButton.text: String
//expect var ToggleButton.textStyle: TextStyle
//expect var ToggleButton.gravity: String
//expect val ToggleButton.checked: Writable<Boolean>
//
//
//expect class Switch: NView
//expect fun ViewContext.switch(setup: Switch.()->Unit = {}): Switch
//expect val Switch.checked: Writable<Boolean>
//
expect class CheckBox : NView

expect fun ViewContext.checkBox(setup: CheckBox.() -> Unit = {}): Unit
expect fun CheckBox.bind(checked: Writable<Boolean>): Unit

expect class ScrollView : NView

expect fun ViewContext.scrollView(setup: ScrollView.() -> Unit = {}): Unit

expect class HorizontalScrollView : NView

expect fun ViewContext.horizontalScrollView(setup: HorizontalScrollView.() -> Unit = {}): Unit

expect class Box : NView

expect fun ViewContext.box(setup: Box.() -> Unit = {}): Unit

//expect var Box.children: List<NView>
//
//expect class RecyclerView: NView
//expect fun ViewContext.recyclerView(setup: RecyclerView.()->Unit = {}): RecyclerView
//
//expect class Pager: NView
//expect fun ViewContext.pager(setup: Pager.()->Unit = {}): Pager
//
expect class WebView : NView

expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String
