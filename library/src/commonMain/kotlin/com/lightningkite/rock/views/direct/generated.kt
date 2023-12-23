package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.*
import kotlinx.datetime.*
import kotlin.jvm.JvmInline

expect class NSeparator : NView
@JvmInline
value class Separator(override val native: NSeparator) : RView<NSeparator>
@ViewDsl expect fun ViewWriter.separator(setup: Separator.() -> Unit = {}): Unit

expect class NContainingView : NView
@JvmInline
value class ContainingView(override val native: NContainingView) : RView<NContainingView>
@ViewDsl expect fun ViewWriter.stack(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.col(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.row(setup: ContainingView.() -> Unit = {}): Unit

expect class NLink : NView
@JvmInline
value class Link(override val native: NLink) : RView<NLink>
@ViewDsl expect fun ViewWriter.link(setup: Link.() -> Unit = {}): Unit
expect var Link.to: RockScreen
expect var Link.newTab: Boolean

expect class NExternalLink : NView
@JvmInline
value class ExternalLink(override val native: NExternalLink) : RView<NExternalLink>
@ViewDsl expect fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
expect var ExternalLink.to: String
expect var ExternalLink.newTab: Boolean

expect class NImage : NView
@JvmInline
value class Image(override val native: NImage) : RView<NImage>
@ViewDsl expect fun ViewWriter.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource
expect var Image.scaleType: ImageScaleType
expect var Image.description: String?

expect class NTextView : NView
@JvmInline
value class TextView(override val native: NTextView) : RView<NTextView>
@ViewDsl expect fun ViewWriter.h1(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.h2(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.h3(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.h4(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.h5(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.h6(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.text(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.subtext(setup: TextView.() -> Unit = {}): Unit

//expect val LocalDateField.content: Writable<LocalDate?>

expect var TextView.content: String
expect var TextView.align: Align
expect var TextView.textSize: Dimension

expect class NLabel : NView
@JvmInline
value class Label(override val native: NLabel) : RView<NLabel>
@ViewDsl expect fun ViewWriter.label(setup: Label.() -> Unit = {}): Unit
expect var Label.content: String

expect class NActivityIndicator : NView
@JvmInline
value class ActivityIndicator(override val native: NActivityIndicator) : RView<NActivityIndicator>
@ViewDsl expect fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit

expect class NSpace : NView
@JvmInline
value class Space(override val native: NSpace) : RView<NSpace>
@ViewDsl expect fun ViewWriter.space(setup: Space.() -> Unit = {}): Unit
expect fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit = {})

expect class NDismissBackground : NView
@JvmInline
value class DismissBackground(override val native: NDismissBackground) : RView<NDismissBackground>
@ViewDsl expect fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit = {}): Unit
expect fun DismissBackground.onClick(action: suspend () -> Unit)

expect class NButton : NView
@JvmInline
value class Button(override val native: NButton) : RView<NButton>
@ViewDsl expect fun ViewWriter.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: suspend () -> Unit)
expect var Button.enabled: Boolean

expect class NCheckbox : NView
@JvmInline
value class Checkbox(override val native: NCheckbox) : RView<NCheckbox>
@ViewDsl expect fun ViewWriter.checkbox(setup: Checkbox.() -> Unit = {}): Unit
expect var Checkbox.enabled: Boolean
expect val Checkbox.checked: Writable<Boolean>

expect class NRadioButton : NView
@JvmInline
value class RadioButton(override val native: NRadioButton) : RView<NRadioButton>
@ViewDsl expect fun ViewWriter.radioButton(setup: RadioButton.() -> Unit = {}): Unit
expect var RadioButton.enabled: Boolean
expect val RadioButton.checked: Writable<Boolean>

expect class NSwitch : NView
@JvmInline
value class Switch(override val native: NSwitch) : RView<NSwitch>
@ViewDsl expect fun ViewWriter.switch(setup: Switch.() -> Unit = {}): Unit
expect var Switch.enabled: Boolean
expect val Switch.checked: Writable<Boolean>

expect class NToggleButton : NView
@JvmInline
value class ToggleButton(override val native: NToggleButton) : RView<NToggleButton>
@ViewDsl expect fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
expect var ToggleButton.enabled: Boolean
expect val ToggleButton.checked: Writable<Boolean>

expect class NRadioToggleButton : NView
@JvmInline
value class RadioToggleButton(override val native: NRadioToggleButton) : RView<NRadioToggleButton>
@ViewDsl expect fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit = {}): Unit
expect var RadioToggleButton.enabled: Boolean
expect val RadioToggleButton.checked: Writable<Boolean>

expect class NLocalDateField : NView
@JvmInline
value class LocalDateField(override val native: NLocalDateField) : RView<NLocalDateField>
@ViewDsl expect fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit = {}): Unit
expect val LocalDateField.content: Writable<LocalDate?>
expect var LocalDateField.action: Action?
expect var LocalDateField.range: ClosedRange<LocalDate>?

expect class NLocalTimeField : NView
@JvmInline
value class LocalTimeField(override val native: NLocalTimeField) : RView<NLocalTimeField>
@ViewDsl expect fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit = {}): Unit
expect val LocalTimeField.content: Writable<LocalTime?>
expect var LocalTimeField.action: Action?
expect var LocalTimeField.range: ClosedRange<LocalTime>?

expect class NLocalDateTimeField : NView
@JvmInline
value class LocalDateTimeField(override val native: NLocalDateTimeField) : RView<NLocalDateTimeField>
@ViewDsl expect fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit = {}): Unit
expect val LocalDateTimeField.content: Writable<LocalDateTime?>
expect var LocalDateTimeField.action: Action?
expect var LocalDateTimeField.range: ClosedRange<LocalDateTime>?

expect class NTextField : NView
@JvmInline
value class TextField(override val native: NTextField) : RView<NTextField>
@ViewDsl expect fun ViewWriter.textField(setup: TextField.() -> Unit = {}): Unit
expect val TextField.content: Writable<String>
expect var TextField.keyboardHints: KeyboardHints
expect var TextField.action: Action?
expect var TextField.hint: String
expect var TextField.range: ClosedRange<Double>?

expect class NTextArea : NView
@JvmInline
value class TextArea(override val native: NTextArea) : RView<NTextArea>
@ViewDsl expect fun ViewWriter.textArea(setup: TextArea.() -> Unit = {}): Unit
expect val TextArea.content: Writable<String>
expect var TextArea.keyboardHints: KeyboardHints
expect var TextArea.hint: String

expect class NSelect : NView
@JvmInline
value class Select(override val native: NSelect) : RView<NSelect>
@ViewDsl expect fun ViewWriter.select(setup: Select.() -> Unit = {}): Unit
expect fun <T> Select.bind(edits: Writable<T>, data: Readable<List<T>>, render: (T)->String)

expect class NAutoCompleteTextField : NView
@JvmInline
value class AutoCompleteTextField(override val native: NAutoCompleteTextField) : RView<NAutoCompleteTextField>
@ViewDsl expect fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit = {}): Unit
expect val AutoCompleteTextField.content: Writable<String>
expect var AutoCompleteTextField.keyboardHints: KeyboardHints
expect var AutoCompleteTextField.action: Action?
expect var AutoCompleteTextField.suggestions: List<String>

expect class NSwapView : NView
@JvmInline
value class SwapView(override val native: NSwapView) : RView<NSwapView>
@ViewDsl expect fun ViewWriter.swapView(setup: SwapView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit = {}): Unit
expect fun SwapView.swap(transition: ScreenTransition = ScreenTransition.Fade, createNewView: ()->Unit): Unit

expect class NWebView : NView
@JvmInline
value class WebView(override val native: NWebView) : RView<NWebView>
@ViewDsl expect fun ViewWriter.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String
expect var WebView.permitJs: Boolean
expect var WebView.content: String

expect class NCanvas : NView
@JvmInline
value class Canvas(override val native: NCanvas) : RView<NCanvas>
@ViewDsl expect fun ViewWriter.canvas(setup: Canvas.() -> Unit = {}): Unit
expect fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit
expect val Canvas.width: Readable<Double>
expect val Canvas.height: Readable<Double>
expect fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit
expect fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit

expect class NRecyclerView : NView

@JvmInline
value class RecyclerView(override val native: NRecyclerView) : RView<NRecyclerView>
@ViewDsl expect fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
expect var RecyclerView.columns: Int
expect fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>)->Unit): Unit
@ViewModifierDsl3 expect fun ViewWriter.hasPopover(requireClick: Boolean = false, preferredDirection: PopoverPreferredDirection = PopoverPreferredDirection.belowRight, setup: ViewWriter.()->Unit): ViewWrapper
@ViewModifierDsl3 expect fun ViewWriter.weight(amount: Float): ViewWrapper
@ViewModifierDsl3 expect fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper
@ViewModifierDsl3 expect val ViewWriter.scrolls: ViewWrapper
@ViewModifierDsl3 expect val ViewWriter.scrollsHorizontally: ViewWrapper
@ViewModifierDsl3 expect fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper
@ViewModifierDsl3 expect val ViewWriter.marginless: ViewWrapper
@ViewModifierDsl3 expect val ViewWriter.withDefaultPadding: ViewWrapper
