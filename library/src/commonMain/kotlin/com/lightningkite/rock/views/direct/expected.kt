//package com.lightningkite.rock.views.direct
//
//import com.lightningkite.rock.*
//import com.lightningkite.rock.models.*
//import com.lightningkite.rock.navigation.RockScreen
//import com.lightningkite.rock.reactive.Writable
//import com.lightningkite.rock.views.NView
//import com.lightningkite.rock.views.ViewContext
//import com.lightningkite.rock.views.ViewDsl
//import com.lightningkite.rock.views.ViewModifierDsl3
//
//internal expect var NView.animationId: AnimationId?
//expect var NView.animationId: AnimationId?
//
//expect class ContainingView: NView
//expect val ContainingView.childViews: List<NView>
//expect fun ContainingView.removeChild(child: NView)
//expect fun ContainingView.addChild(child: NView, position: Int = childViews.size)
//@ViewDsl
//expect fun ViewContext.stack(setup: ContainingView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.col(setup: ContainingView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.row(setup: ContainingView.() -> Unit = {}): Unit
//
//@ViewModifierDsl3
//expect fun ViewContext.weight(amount: Float): ViewWrapper
//@ViewModifierDsl3
//expect fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper
//@ViewModifierDsl3
//expect fun ViewContext.scrolls(): ViewWrapper
//@ViewModifierDsl3
//expect fun ViewContext.scrollsHorizontally(): ViewWrapper
//@ViewModifierDsl3
//expect fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper
//
//expect class Link : NView
//internal expect var Link.to: RockScreen
//expect var Link.to: RockScreen
//@ViewDsl
//expect fun ViewContext.link(setup: Link.() -> Unit = {}): Unit
//
//expect class ExternalLink : NView
//internal expect var ExternalLink.to: String
//expect var ExternalLink.to: String
//@ViewDsl
//expect fun ViewContext.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
//
//expect class Image : NView
//internal expect var Image.source: ImageSource
//expect var Image.source: ImageSource
//internal expect var Image.scaleType: ImageMode
//expect var Image.scaleType: ImageMode
//internal expect var Image.maxWidth: Dimension?
//expect var Image.maxWidth: Dimension?
//internal expect var Image.maxHeight: Dimension?
//expect var Image.maxHeight: Dimension?
//@ViewDsl
//expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
//
//@ViewDsl
//expect fun ViewContext.activityIndicator(scale: Float = 1f, setup: NView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.space(scale: Float = 1f): Unit
//
//expect class TextView: NView
//internal expect var TextView.text: String
//expect var TextView.text: String
//@ViewDsl
//expect fun ViewContext.h1(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.h2(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.h3(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.h4(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.h5(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.h6(setup: TextView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.text(setup: TextView.() -> Unit = {}): Unit
//
//expect class Button : NView
//expect fun Button.onClick(action: () -> Unit)
//internal expect var Button.enabled: Boolean
//expect var Button.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.button(setup: Button.() -> Unit = {}): Unit
//
//expect class Checkbox : NView
//expect val Checkbox.Checkboxchecked: Writable<Boolean>
//inline val Checkbox.checked: Writable<Boolean> get() = Checkboxchecked
//internal expect var Checkbox.enabled: Boolean
//expect var Checkbox.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.checkbox(setup: Checkbox.() -> Unit = {}): Unit
//
//expect class RadioButton : NView
//expect val RadioButton.RadioButtonchecked: Writable<Boolean>
//inline val RadioButton.checked: Writable<Boolean> get() = RadioButtonchecked
//internal expect var RadioButton.enabled: Boolean
//expect var RadioButton.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.radioButton(setup: RadioButton.() -> Unit = {}): Unit
//
//expect class Switch : NView
//expect val Switch.checked: Writable<Boolean>
//internal expect var Switch.enabled: Boolean
//expect var Switch.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.switch(setup: Switch.() -> Unit = {}): Unit
//
//expect class ToggleButton : NView
//expect val ToggleButton.checked: Writable<Boolean>
//internal expect var ToggleButton.enabled: Boolean
//expect var ToggleButton.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit = {}): Unit
//
//expect class RadioToggleButton : NView
//expect val RadioToggleButton.checked: Writable<Boolean>
//internal expect var RadioToggleButton.enabled: Boolean
//expect var RadioToggleButton.enabled: Boolean
//@ViewDsl
//expect fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit = {}): Unit
//
//expect class TextField: NView
//expect val TextField.text: Writable<String>
//internal expect var TextField.keyboardHints: KeyboardHints
//expect var TextField.keyboardHints: KeyboardHints
//internal expect var TextField.hint: String
//expect var TextField.hint: String
//internal expect var TextField.range: ClosedRange<Double>
//expect var TextField.range: ClosedRange<Double>
//@ViewDsl
//expect fun ViewContext.textField(setup: TextField.() -> Unit = {}): Unit
//
//expect class TextArea: NView
//expect val TextArea.text: Writable<String>
//internal expect var TextArea.keyboardHints: KeyboardHints
//expect var TextArea.keyboardHints: KeyboardHints
//internal expect var TextArea.hint: String
//expect var TextArea.hint: String
//@ViewDsl
//expect fun ViewContext.textField(setup: TextArea.() -> Unit = {}): Unit
//
//expect class AutoCompleteTextField : NView
//internal expect var AutoCompleteTextField.options: List<WidgetOption>
//expect var AutoCompleteTextField.options: List<WidgetOption>
//expect fun AutoCompleteTextField.onSelected(action: (WidgetOption)->Unit)
//expect val AutoCompleteTextField.text: Writable<String>
//@ViewDsl
//expect fun ViewContext.autoComplete(setup: AutoCompleteTextField.() -> Unit = {}): Unit
//
//expect class DropDown : NView
//internal expect var DropDown.options: List<WidgetOption>
//expect var DropDown.options: List<WidgetOption>
//expect val DropDown.selected: Writable<WidgetOption>
//@ViewDsl
//expect fun ViewContext.dropDown(setup: DropDown.() -> Unit = {}): Unit
//
//expect class SwapView: NView
//internal expect var SwapView.currentView: ViewContext.()->Unit
//expect var SwapView.currentView: ViewContext.()->Unit
//expect fun SwapView.setCurrentViewWithTransition(view: ViewContext.()->Unit, animation: ScreenTransition)
//@ViewDsl
//expect fun ViewContext.swapView(setup: SwapView.() -> Unit = {}): Unit
//
//expect class WebView : NView
//internal expect var WebView.url: String
//expect var WebView.url: String
//internal expect var WebView.permitJs: String
//expect var WebView.permitJs: String
//internal expect var WebView.content: String
//expect var WebView.content: String
//@ViewDsl
//expect fun ViewContext.webView(setup: WebView.() -> Unit = {}): Unit
//
//expect class RecyclerView: NView
//internal expect var RecyclerView.renderer: ListRenderer<*>
//expect var RecyclerView.renderer: ListRenderer<*>
//@ViewDsl
//expect fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit = {}): Unit
//@ViewDsl
//expect fun ViewContext.gridRecyclerView(columns: Int, setup: RecyclerView.() -> Unit = {}): Unit