package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.events.Event

fun ViewContext.todo(name: String) = element<HTMLSpanElement>("span") {
    innerText = name
}

inline fun <T: HTMLElement> ViewContext.themedElement(name: String, setup: T.()->Unit) = element<T>(name) {
    setup()
}

inline fun <T: HTMLElement, V> T.vprop(eventName: String, crossinline get: T.()->V, crossinline set: T.(V)->Unit): Writable<V> {
    return object: Writable<V> {
        override fun set(value: V): Unit = set(value)
        override val once: V get() = get()

        override fun addListener(listener: () -> Unit): () -> Unit {
            val callback: (Event)->Unit = { listener() }
            this@vprop.addEventListener(eventName, callback)
            return { this@vprop.removeEventListener(eventName, callback) }
        }

    }
}
inline val ToggleButton.ToggleButton_inputElement: HTMLInputElement get() = this.previousElementSibling as HTMLInputElement

//
//actual var NView.animationId: AnimationId?
//    get() = null
//    set(value) { }
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ContainingView = HTMLDivElement
//actual val ContainingView.childViews: List<NView> get() = (0 until children.length).mapNotNull { children.get(it) as? HTMLElement }
//actual fun ContainingView.removeChild(child: NView) { this.removeChild(child) }
//actual fun ContainingView.addChild(child: NView, position: Int) {
//    if(position == children.length) appendChild(child)
//    else insertBefore(childViews[position], child)
//}
//
//@ViewModifierDsl3 actual fun ViewContext.weight(amount: Float): ViewWrapper {
//    elementToDoList.add {
//        style.flexGrow = "$amount"
//        style.flexShrink = "$amount"
//    }
//    return ViewWrapper
//}
//@ViewModifierDsl3 actual fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper = TODO()
//@ViewModifierDsl3 actual fun ViewContext.scrolls(): ViewWrapper{
//    elementToDoList.add {
//        style.overflowY = "scroll"
//    }
//    return ViewWrapper
//}
//@ViewModifierDsl3 actual fun ViewContext.scrollsHorizontally(): ViewWrapper{
//    elementToDoList.add {
//        style.display = "flex"
//        style.flexDirection = "row"
//        style.overflowX = "scroll"
//    }
//    return ViewWrapper
//}
//@ViewModifierDsl3 actual fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper {
//    elementToDoList.add {
//        if (constraints.minHeight == null) style.removeProperty("minHeight")
//        else style.minHeight = constraints.minHeight.value
//
//        if (constraints.maxHeight == null) style.removeProperty("maxHeight")
//        else style.maxHeight = constraints.maxHeight.value
//
//        if (constraints.minWidth == null) style.removeProperty("minWidth")
//        else style.minWidth = constraints.minWidth.value
//
//        if (constraints.maxWidth == null) style.removeProperty("maxWidth")
//        else style.maxWidth = constraints.maxWidth.value
//
//        if (constraints.width == null) style.removeProperty("width")
//        else style.width = constraints.width.value
//
//        if (constraints.height == null) style.removeProperty("height")
//        else style.height = constraints.height.value
//
//        style.overflowX = "hidden"
//        style.overflowY = "hidden"
//
//    }
//    return ViewWrapper
//}
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Link  = HTMLAnchorElement
//actual var Link.toScreen: RockScreen
//    get() = TODO()
//    set(value) { href = value.createPath() }
//@ViewDsl actual fun ViewContext.link(setup: Link.() -> Unit): Unit = element("a", setup)
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ExternalLink  = HTMLAnchorElement
//actual var ExternalLink.toUrl: String
//    get() = href
//    set(value) { href = value }
//@ViewDsl actual fun ViewContext.externalLink(setup: ExternalLink.() -> Unit): Unit = element("a", setup)
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Image  = HTMLImageElement
//actual var Image.source: ImageSource
//    get() = throw NotImplementedError()
//    set(value) {
//        if (value is ImageRemote)
//            src = value.url
//        else if (value is ImageRaw)
//            src = value.data.toBase64()
//        else if (value is ImageResource)
//            throw NotImplementedError()
//        else if (value is ImageVector) {
//            src = value.toWeb()
//            style.width = value.width.value
//            style.height = value.height.value
//        }
//    }
//actual var Image.scaleType: ImageMode
//    get() = throw NotImplementedError()
//    set(value) {
//        style.objectFit = when (value) {
//            ImageMode.Fit -> "contain"
//            ImageMode.Crop -> "cover"
//            ImageMode.Stretch -> "fill"
//            ImageMode.NoScale -> "none"
//        }
//    }
//actual var Image.maxWidth: Dimension?
//    get() = style.maxWidth.takeIf { it.isNotEmpty() && it[0].isDigit() }?.let { Dimension(it) }
//    set(value) { style.maxWidth = value?.value ?: "unset" }
//actual var Image.maxHeight: Dimension?
//    get() = style.maxHeight.takeIf { it.isNotEmpty() && it[0].isDigit() }?.let { Dimension(it) }
//    set(value) { style.maxHeight = value?.value ?: "unset" }
//@ViewDsl actual fun ViewContext.image(setup: Image.() -> Unit): Unit = element<HTMLImageElement>("img") {
//    scaleType = ImageMode.Fit
//    setup()
//}
//
//@ViewDsl actual fun ViewContext.activityIndicator(scale: Float, setup: NView.() -> Unit): Unit = element<HTMLDivElement>("div") {
//    className = "lds-ring"
//    reactiveScope {
//        style.width = getTheme().spacing.times(scale).value
//        style.height = getTheme().spacing.times(scale).value
//    }
//    setup()
//}
//@ViewDsl actual fun ViewContext.space(scale: Float): Unit = element<HTMLDivElement>("div") {
//    reactiveScope { style.width = getTheme().spacing.times(scale).value }
//}
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextView = HTMLElement
//actual var TextView.text: String
//    get() = innerText
//    set(value) { innerText = value }
@ViewDsl internal fun ViewContext.textElement(elementBase: String, setup: TextView.() -> Unit): Unit = element<HTMLDivElement>(elementBase) {
    setup()
    style.whiteSpace = "pre-wrap"
}
//@ViewDsl actual fun ViewContext.h1(setup: TextView.() -> Unit): Unit = textElement("h1", setup)
//@ViewDsl actual fun ViewContext.h2(setup: TextView.() -> Unit): Unit = textElement("h2", setup)
//@ViewDsl actual fun ViewContext.h3(setup: TextView.() -> Unit): Unit = textElement("h3", setup)
//@ViewDsl actual fun ViewContext.h4(setup: TextView.() -> Unit): Unit = textElement("h4", setup)
//@ViewDsl actual fun ViewContext.h5(setup: TextView.() -> Unit): Unit = textElement("h5", setup)
//@ViewDsl actual fun ViewContext.h6(setup: TextView.() -> Unit): Unit = textElement("h6", setup)
//@ViewDsl actual fun ViewContext.text(setup: TextView.() -> Unit): Unit = textElement("p", setup)
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Button  = HTMLButtonElement
//actual fun Button.onClick(action: () -> Unit) { this.onclick = { action() } }
//actual var Button.enabled: Boolean
//    get() = !this.disabled
//    set(value) { this.disabled = !value }
//@ViewDsl actual fun ViewContext.button(setup: Button.() -> Unit): Unit = element<HTMLButtonElement>("button") {
//    this.type = "button"
//    setup()
//}
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Checkbox  = HTMLInputElement
//actual val Checkbox.Checkboxchecked: Writable<Boolean> get() = object: Writable<Boolean> {
//    override fun set(value: Boolean) { this@checked.checked = value }
//    override val once: Boolean get() = this@checked.checked
//    override fun addListener(listener: () -> Unit): () -> Unit {
//        val callback: (Event)->Unit = { listener() }
//        this@checked.addEventListener("input", callback)
//        return { this@checked.removeEventListener("input", callback) }
//    }
//}
//actual var HTMLInputElement.enabled: Boolean
//    get() = !this.disabled
//    set(value) { this.disabled = !value }
//@ViewDsl actual fun ViewContext.checkbox(setup: Checkbox.() -> Unit): Unit = element<HTMLInputElement>("input") {
//    type = "checkbox"
//    setup()
//}
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RadioButton  = HTMLInputElement
//actual val RadioButton.RadioButtonchecked: Writable<Boolean>
//@ViewDsl actual fun ViewContext.radioButton(setup: RadioButton.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Switch  = HTMLElement
//actual val Switch.checked: Writable<Boolean>
//actual var Switch.enabled: Boolean
//@ViewDsl actual fun ViewContext.switch(setup: Switch.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ToggleButton  = HTMLElement
//actual val ToggleButton.checked: Writable<Boolean>
//actual var ToggleButton.enabled: Boolean
//@ViewDsl actual fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RadioToggleButton  = HTMLElement
//actual val RadioToggleButton.checked: Writable<Boolean>
//actual var RadioToggleButton.enabled: Boolean
//@ViewDsl actual fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextField = HTMLElement
//actual val TextField.text: Writable<String>
//actual var TextField.keyboardHints: KeyboardHints
//actual var TextField.hint: String
//actual var TextField.range: ClosedRange<Double>
//@ViewDsl actual fun ViewContext.textField(setup: TextField.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextArea = HTMLElement
//actual val TextArea.text: Writable<String>
//actual var TextArea.keyboardHints: KeyboardHints
//actual var TextArea.hint: String
//@ViewDsl actual fun ViewContext.textField(setup: TextArea.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias AutoCompleteTextField = HTMLInputElement
//actual var AutoCompleteTextField.options: List<WidgetOption>
//actual fun AutoCompleteTextField.onSelected(action: (WidgetOption)->Unit)
//actual val AutoCompleteTextField.text: Writable<String>
//@ViewDsl actual fun ViewContext.autoComplete(setup: AutoCompleteTextField.() -> Unit): Unit = element<HTMLInputElement>("input", setup)
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias DropDown  = HTMLElement
//actual var DropDown.options: List<WidgetOption>
//actual val DropDown.selected: Writable<WidgetOption>
//@ViewDsl actual fun ViewContext.dropDown(setup: DropDown.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias SwapView = HTMLElement
//actual var SwapView.currentView: ViewContext.()->Unit
//actual fun SwapView.setCurrentViewWithTransition(view: ViewContext.()->Unit, animation: ScreenTransition)
//@ViewDsl actual fun ViewContext.swapView(setup: SwapView.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias WebView  = HTMLElement
//actual var WebView.url: String
//actual var WebView.permitJs: String
//actual var WebView.content: String
//@ViewDsl actual fun ViewContext.webView(setup: WebView.() -> Unit): Unit
//
//@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RecyclerView = HTMLElement
//actual var RecyclerView.renderer: ListRenderer<*>
//@ViewDsl actual fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit): Unit
//@ViewDsl actual fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit
//@ViewDsl actual fun ViewContext.gridRecyclerView(columns: Int, setup: RecyclerView.() -> Unit): Unit

fun HTMLElement.__resetContentToOptionList(options: List<WidgetOption>, selected: String) {
    innerHTML = ""
    for(item in options) appendChild((document.createElement("option") as HTMLOptionElement).apply {
        this.value = item.key
        this.innerText = item.display
        this.selected = item.key == selected
    })
}