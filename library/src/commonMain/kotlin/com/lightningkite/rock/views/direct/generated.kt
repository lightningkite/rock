package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewModifierDsl3

expect class ContainingView : NView
@ViewDsl expect fun ViewContext.stack(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.col(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.row(setup: ContainingView.() -> Unit = {}): Unit

expect class Link : NView
@ViewDsl expect fun ViewContext.link(setup: Link.() -> Unit = {}): Unit
expect var Link.Link_to: RockScreen

expect class ExternalLink : NView
@ViewDsl expect fun ViewContext.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
expect var ExternalLink.ExternalLink_to: String

expect class Image : NView
@ViewDsl expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.Image_source: ImageSource
expect var Image.Image_scaleType: ImageMode

expect class TextView : NView
@ViewDsl expect fun ViewContext.h1(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h2(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h3(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h4(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h5(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h6(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.text(setup: TextView.() -> Unit = {}): Unit
expect var TextView.TextView_content: String

expect class ActivityIndicator : NView
@ViewDsl expect fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit

expect class Space : NView
@ViewDsl expect fun ViewContext.space(setup: Space.() -> Unit = {}): Unit

expect class Button : NView
@ViewDsl expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: () -> Unit)
expect var Button.Button_enabled: Boolean

expect class Checkbox : NView
@ViewDsl expect fun ViewContext.checkbox(setup: Checkbox.() -> Unit = {}): Unit
expect var Checkbox.Checkbox_enabled: Boolean
expect val Checkbox.Checkbox_checked: Writable<Boolean>

expect class RadioButton : NView
@ViewDsl expect fun ViewContext.radioButton(setup: RadioButton.() -> Unit = {}): Unit
expect var RadioButton.RadioButton_enabled: Boolean
expect val RadioButton.RadioButton_checked: Writable<Boolean>

expect class Switch : NView
@ViewDsl expect fun ViewContext.switch(setup: Switch.() -> Unit = {}): Unit
expect var Switch.Switch_enabled: Boolean
expect val Switch.Switch_checked: Writable<Boolean>

expect class ToggleButton : NView
@ViewDsl expect fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
expect var ToggleButton.ToggleButton_enabled: Boolean
expect val ToggleButton.ToggleButton_checked: Writable<Boolean>

expect class RadioToggleButton : NView
@ViewDsl expect fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit = {}): Unit
expect var RadioToggleButton.RadioToggleButton_enabled: Boolean
expect val RadioToggleButton.RadioToggleButton_checked: Writable<Boolean>

expect class TextField : NView
@ViewDsl expect fun ViewContext.textField(setup: TextField.() -> Unit = {}): Unit
expect val TextField.TextField_content: Writable<String>
expect var TextField.TextField_keyboardHints: KeyboardHints
expect var TextField.TextField_hint: String
expect var TextField.TextField_range: ClosedRange<Double>?

expect class TextArea : NView
@ViewDsl expect fun ViewContext.textArea(setup: TextArea.() -> Unit = {}): Unit
expect val TextArea.TextArea_content: Writable<String>
expect var TextArea.TextArea_keyboardHints: KeyboardHints
expect var TextArea.TextArea_hint: String

expect class DropDown : NView
@ViewDsl expect fun ViewContext.dropDown(setup: DropDown.() -> Unit = {}): Unit
expect val DropDown.DropDown_selected: Writable<String?>
expect var DropDown.DropDown_options: List<WidgetOption>

expect class AutoCompleteTextField : NView
@ViewDsl expect fun ViewContext.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit = {}): Unit
expect val AutoCompleteTextField.AutoCompleteTextField_content: Writable<String>
expect var AutoCompleteTextField.AutoCompleteTextField_suggestions: List<String>

expect class SwapView : NView
@ViewDsl expect fun ViewContext.swapView(setup: SwapView.() -> Unit = {}): Unit
expect var SwapView.SwapView_currentView: NView
expect fun SwapView.setCurrentViewWithTransition(view: NView, transition: ScreenTransition): Unit

expect class WebView : NView
@ViewDsl expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.WebView_url: String
expect var WebView.WebView_permitJs: Boolean
expect var WebView.WebView_content: String

expect class RecyclerView : NView
@ViewDsl expect fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.gridRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
expect var RecyclerView.RecyclerView_renderer: ListRenderer<*>
@ViewModifierDsl3 expect fun ViewContext.weight(amount: Float): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrolls(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrollsHorizontally(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper
@ViewModifierDsl3 expect val ViewContext.bordering: ViewWrapper
@ViewModifierDsl3 expect val ViewContext.addPadding: ViewWrapper
