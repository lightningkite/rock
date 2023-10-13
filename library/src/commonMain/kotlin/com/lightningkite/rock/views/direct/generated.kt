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
inline var Link.to: RockScreen
    get() = this.Link_to
    set(value) { this.Link_to = value }

expect class ExternalLink : NView
@ViewDsl expect fun ViewContext.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
expect var ExternalLink.ExternalLink_to: String
inline var ExternalLink.to: String
    get() = this.ExternalLink_to
    set(value) { this.ExternalLink_to = value }

expect class Image : NView
@ViewDsl expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.Image_source: ImageSource
inline var Image.source: ImageSource
    get() = this.Image_source
    set(value) { this.Image_source = value }
expect var Image.Image_scaleType: ImageMode
inline var Image.scaleType: ImageMode
    get() = this.Image_scaleType
    set(value) { this.Image_scaleType = value }

expect class TextView : NView
@ViewDsl expect fun ViewContext.h1(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h2(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h3(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h4(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h5(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.h6(setup: TextView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.text(setup: TextView.() -> Unit = {}): Unit
expect var TextView.TextView_content: String
inline var TextView.content: String
    get() = this.TextView_content
    set(value) { this.TextView_content = value }

expect class ActivityIndicator : NView
@ViewDsl expect fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit

expect class Space : NView
@ViewDsl expect fun ViewContext.space(setup: Space.() -> Unit = {}): Unit

expect class Button : NView
@ViewDsl expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
expect fun Button.onClick(action: () -> Unit)
expect var Button.Button_enabled: Boolean
inline var Button.enabled: Boolean
    get() = this.Button_enabled
    set(value) { this.Button_enabled = value }

expect class Checkbox : NView
@ViewDsl expect fun ViewContext.checkbox(setup: Checkbox.() -> Unit = {}): Unit
expect var Checkbox.Checkbox_enabled: Boolean
inline var Checkbox.enabled: Boolean
    get() = this.Checkbox_enabled
    set(value) { this.Checkbox_enabled = value }
expect val Checkbox.Checkbox_checked: Writable<Boolean>
inline val Checkbox.checked: Writable<Boolean> get() = this.Checkbox_checked

expect class RadioButton : NView
@ViewDsl expect fun ViewContext.radioButton(setup: RadioButton.() -> Unit = {}): Unit
expect var RadioButton.RadioButton_enabled: Boolean
inline var RadioButton.enabled: Boolean
    get() = this.RadioButton_enabled
    set(value) { this.RadioButton_enabled = value }
expect val RadioButton.RadioButton_checked: Writable<Boolean>
inline val RadioButton.checked: Writable<Boolean> get() = this.RadioButton_checked

expect class Switch : NView
@ViewDsl expect fun ViewContext.switch(setup: Switch.() -> Unit = {}): Unit
expect var Switch.Switch_enabled: Boolean
inline var Switch.enabled: Boolean
    get() = this.Switch_enabled
    set(value) { this.Switch_enabled = value }
expect val Switch.Switch_checked: Writable<Boolean>
inline val Switch.checked: Writable<Boolean> get() = this.Switch_checked

expect class ToggleButton : NView
@ViewDsl expect fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
expect var ToggleButton.ToggleButton_enabled: Boolean
inline var ToggleButton.enabled: Boolean
    get() = this.ToggleButton_enabled
    set(value) { this.ToggleButton_enabled = value }
expect val ToggleButton.ToggleButton_checked: Writable<Boolean>
inline val ToggleButton.checked: Writable<Boolean> get() = this.ToggleButton_checked

expect class RadioToggleButton : NView
@ViewDsl expect fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit = {}): Unit
expect var RadioToggleButton.RadioToggleButton_enabled: Boolean
inline var RadioToggleButton.enabled: Boolean
    get() = this.RadioToggleButton_enabled
    set(value) { this.RadioToggleButton_enabled = value }
expect val RadioToggleButton.RadioToggleButton_checked: Writable<Boolean>
inline val RadioToggleButton.checked: Writable<Boolean> get() = this.RadioToggleButton_checked

expect class TextField : NView
@ViewDsl expect fun ViewContext.textField(setup: TextField.() -> Unit = {}): Unit
expect val TextField.TextField_content: Writable<String>
inline val TextField.content: Writable<String> get() = this.TextField_content
expect var TextField.TextField_keyboardHints: KeyboardHints
inline var TextField.keyboardHints: KeyboardHints
    get() = this.TextField_keyboardHints
    set(value) { this.TextField_keyboardHints = value }
expect var TextField.TextField_hint: String
inline var TextField.hint: String
    get() = this.TextField_hint
    set(value) { this.TextField_hint = value }
expect var TextField.TextField_range: ClosedRange<Double>?
inline var TextField.range: ClosedRange<Double>?
    get() = this.TextField_range
    set(value) { this.TextField_range = value }

expect class TextArea : NView
@ViewDsl expect fun ViewContext.textArea(setup: TextArea.() -> Unit = {}): Unit
expect val TextArea.TextArea_content: Writable<String>
inline val TextArea.content: Writable<String> get() = this.TextArea_content
expect var TextArea.TextArea_keyboardHints: KeyboardHints
inline var TextArea.keyboardHints: KeyboardHints
    get() = this.TextArea_keyboardHints
    set(value) { this.TextArea_keyboardHints = value }
expect var TextArea.TextArea_hint: String
inline var TextArea.hint: String
    get() = this.TextArea_hint
    set(value) { this.TextArea_hint = value }

expect class DropDown : NView
@ViewDsl expect fun ViewContext.dropDown(setup: DropDown.() -> Unit = {}): Unit
expect val DropDown.DropDown_selected: Writable<String?>
inline val DropDown.selected: Writable<String?> get() = this.DropDown_selected
expect var DropDown.DropDown_options: List<WidgetOption>
inline var DropDown.options: List<WidgetOption>
    get() = this.DropDown_options
    set(value) { this.DropDown_options = value }

expect class AutoCompleteTextField : NView
@ViewDsl expect fun ViewContext.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit = {}): Unit
expect val AutoCompleteTextField.AutoCompleteTextField_content: Writable<String>
inline val AutoCompleteTextField.content: Writable<String> get() = this.AutoCompleteTextField_content
expect var AutoCompleteTextField.AutoCompleteTextField_suggestions: List<WidgetOption>
inline var AutoCompleteTextField.suggestions: List<WidgetOption>
    get() = this.AutoCompleteTextField_suggestions
    set(value) { this.AutoCompleteTextField_suggestions = value }

expect class SwapView : NView
@ViewDsl expect fun ViewContext.swapView(setup: SwapView.() -> Unit = {}): Unit
expect var SwapView.SwapView_currentView: NView
inline var SwapView.currentView: NView
    get() = this.SwapView_currentView
    set(noinline value) { this.SwapView_currentView = value }
expect fun SwapView.setCurrentViewWithTransition(view: NView, animation: ScreenTransition): Unit

expect class WebView : NView
@ViewDsl expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
expect var WebView.WebView_url: String
inline var WebView.url: String
    get() = this.WebView_url
    set(value) { this.WebView_url = value }
expect var WebView.WebView_permitJs: Boolean
inline var WebView.permitJs: Boolean
    get() = this.WebView_permitJs
    set(value) { this.WebView_permitJs = value }
expect var WebView.WebView_content: String
inline var WebView.content: String
    get() = this.WebView_content
    set(value) { this.WebView_content = value }

expect class RecyclerView : NView
@ViewDsl expect fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
@ViewDsl expect fun ViewContext.gridRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
expect var RecyclerView.RecyclerView_renderer: ListRenderer<*>
inline var RecyclerView.renderer: ListRenderer<*>
    get() = this.RecyclerView_renderer
    set(value) { this.RecyclerView_renderer = value }
@ViewModifierDsl3 expect fun ViewContext.weight(amount: Float): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrolls(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.scrollsHorizontally(): ViewWrapper
@ViewModifierDsl3 expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper
