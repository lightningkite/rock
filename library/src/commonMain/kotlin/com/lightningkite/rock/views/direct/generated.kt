package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*

expect class NSeparator : NView
value class Separator(override val native: NSeparator) : RView<NSeparator>
@ViewDsl expect fun ViewContext.separator(setup: Separator.() -> Unit = {}): Unit

expect class NContainingView : NView
value class ContainingView(override val native: NContainingView) : RView<NContainingView>
@ViewDsl expect fun ViewContext.stack(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.col(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.row(setup: ContainingView.() -> Unit = {}): Unit

expect class NLink : NView
value class Link(override val native: NLink) : RView<NLink>
@ViewDsl expect fun ViewContext.link(setup: Link.() -> Unit = {}): Unit
expect var Link.to: RockScreen

expect class NExternalLink : NView
value class ExternalLink(override val native: NExternalLink) : RView<NExternalLink>
@ViewDsl expect fun ViewContext.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
expect var ExternalLink.to: String

expect class NImage : NView
value class Image(override val native: NImage) : RView<NImage>
@ViewDsl expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource
expect var Image.scaleType: ImageMode

expect class NTextView : NView
value class TextView(override val native: NTextView) : RView<NTextView>
@ViewDsl expect fun ViewContext.h1(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h2(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h3(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h4(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h5(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h6(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.text(setup: TextView.() -> Unit = {}): Unit
expect var TextView.content: String

expect class NLabel : NView
value class Label(override val native: NLabel) : RView<NLabel>
@ViewDsl expect fun ViewContext.label(setup: Label.() -> Unit = {}): Unit
expect var Label.content: String

expect class NActivityIndicator : NView
value class ActivityIndicator(override val native: NActivityIndicator) : RView<NActivityIndicator>
@ViewDsl expect fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit

expect class NSpace : NView
value class Space(override val native: NSpace) : RView<NSpace>
@ViewDsl expect fun ViewContext.space(setup: Space.() -> Unit = {}): Unit

expect class NButton : NView
value class Button(override val native: NButton) : RView<NButton>
@ViewDsl expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: () -> Unit)
expect var Button.enabled: Boolean

expect class NCheckbox : NView
value class Checkbox(override val native: NCheckbox) : RView<NCheckbox>
@ViewDsl expect fun ViewContext.checkbox(setup: Checkbox.() -> Unit = {}): Unit
expect var Checkbox.enabled: Boolean
expect val Checkbox.checked: Writable<Boolean>

expect class NRadioButton : NView
value class RadioButton(override val native: NRadioButton) : RView<NRadioButton>
@ViewDsl expect fun ViewContext.radioButton(setup: RadioButton.() -> Unit = {}): Unit
expect var RadioButton.enabled: Boolean
expect val RadioButton.checked: Writable<Boolean>

expect class NSwitch : NView
value class Switch(override val native: NSwitch) : RView<NSwitch>
@ViewDsl expect fun ViewContext.switch(setup: Switch.() -> Unit = {}): Unit
expect var Switch.enabled: Boolean
expect val Switch.checked: Writable<Boolean>

expect class NToggleButton : NView
value class ToggleButton(override val native: NToggleButton) : RView<NToggleButton>
@ViewDsl expect fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
expect var ToggleButton.enabled: Boolean
expect val ToggleButton.checked: Writable<Boolean>

expect class NRadioToggleButton : NView
value class RadioToggleButton(override val native: NRadioToggleButton) : RView<NRadioToggleButton>
@ViewDsl expect fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit = {}): Unit
expect var RadioToggleButton.enabled: Boolean
expect val RadioToggleButton.checked: Writable<Boolean>

expect class NTextField : NView
value class TextField(override val native: NTextField) : RView<NTextField>
@ViewDsl expect fun ViewContext.textField(setup: TextField.() -> Unit = {}): Unit
expect val TextField.content: Writable<String>
expect var TextField.keyboardHints: KeyboardHints
expect var TextField.hint: String
expect var TextField.range: ClosedRange<Double>?

expect class NTextArea : NView
value class TextArea(override val native: NTextArea) : RView<NTextArea>
@ViewDsl expect fun ViewContext.textArea(setup: TextArea.() -> Unit = {}): Unit
expect val TextArea.content: Writable<String>
expect var TextArea.keyboardHints: KeyboardHints
expect var TextArea.hint: String

expect class NDropDown : NView
value class DropDown(override val native: NDropDown) : RView<NDropDown>
@ViewDsl expect fun ViewContext.dropDown(setup: DropDown.() -> Unit = {}): Unit
expect val DropDown.selected: Writable<String?>
expect var DropDown.options: List<WidgetOption>

expect class NAutoCompleteTextField : NView
value class AutoCompleteTextField(override val native: NAutoCompleteTextField) : RView<NAutoCompleteTextField>
@ViewDsl expect fun ViewContext.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit = {}): Unit
expect val AutoCompleteTextField.content: Writable<String>
expect var AutoCompleteTextField.suggestions: List<String>

expect class NSwapView : NView
value class SwapView(override val native: NSwapView) : RView<NSwapView>
@ViewDsl expect fun ViewContext.swapView(setup: SwapView.() -> Unit = {}): Unit
expect var SwapView.currentView: NView
expect fun SwapView.setCurrentViewWithTransition(view: NView, transition: ScreenTransition): Unit

expect class NWebView : NView
value class WebView(override val native: NWebView) : RView<NWebView>
@ViewDsl expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.url: String
expect var WebView.permitJs: Boolean
expect var WebView.content: String

expect class NRecyclerView : NView
value class RecyclerView(override val native: NRecyclerView) : RView<NRecyclerView>
@ViewDsl expect fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.gridRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
expect fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewContext.(value: Readable<T>)->Unit): Unit
@ViewModifierDsl3 expect fun ViewContext.weight(amount: Float): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrolls(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrollsHorizontally(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper
@ViewModifierDsl3 expect val ViewContext.bordering: ViewWrapper
@ViewModifierDsl3 expect val ViewContext.withPadding: ViewWrapper
@ViewModifierDsl3 expect val ViewContext.crowd: ViewWrapper
