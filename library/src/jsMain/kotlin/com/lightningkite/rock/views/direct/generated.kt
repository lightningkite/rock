package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.data.toWeb
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.old.toBase64
import kotlinx.browser.document
import org.w3c.dom.*
import kotlin.random.Random


@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ContainingView = HTMLElement
@ViewDsl actual fun ViewContext.stack(setup: ContainingView.() -> Unit): Unit = element<HTMLDivElement>("div") {
    classList.add("rock-stack")
    setup()
}
@ViewDsl actual fun ViewContext.col(setup: ContainingView.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}
@ViewDsl actual fun ViewContext.row(setup: ContainingView.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Link = HTMLAnchorElement
@ViewDsl actual fun ViewContext.link(setup: Link.() -> Unit): Unit = themedElement<HTMLAnchorElement>("a", setup)
actual inline var Link.Link_to: RockScreen
    get() = TODO()
    set(value) { href = value.createPath() }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ExternalLink = HTMLAnchorElement
@ViewDsl actual fun ViewContext.externalLink(setup: ExternalLink.() -> Unit): Unit = themedElement<HTMLAnchorElement>("a", setup)
actual inline var ExternalLink.ExternalLink_to: String
    get() = href
    set(value) { href = value }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Image = HTMLImageElement
@ViewDsl actual fun ViewContext.image(setup: Image.() -> Unit): Unit = themedElement<HTMLImageElement>("img", setup)
actual inline var Image.Image_source: ImageSource
    get() = TODO()
    set(value) {
        when (value) {
            is ImageRemote -> src = value.url
            is ImageRaw -> src = value.data.toBase64()
            is ImageResource -> throw NotImplementedError()
            is ImageVector -> {
                src = value.toWeb()
                style.width = value.width.value
                style.height = value.height.value
            }
            else -> {}
        }
    }
actual inline var Image.Image_scaleType: ImageMode
    get() = TODO()
    set(value) {
        style.objectFit = when (value) {
            ImageMode.Fit -> "contain"
            ImageMode.Crop -> "cover"
            ImageMode.Stretch -> "fill"
            ImageMode.NoScale -> "none"
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextView = HTMLElement
@ViewDsl actual fun ViewContext.h1(setup: TextView.() -> Unit): Unit = textElement("h1", setup)
@ViewDsl actual fun ViewContext.h2(setup: TextView.() -> Unit): Unit = textElement("h2", setup)
@ViewDsl actual fun ViewContext.h3(setup: TextView.() -> Unit): Unit = textElement("h3", setup)
@ViewDsl actual fun ViewContext.h4(setup: TextView.() -> Unit): Unit = textElement("h4", setup)
@ViewDsl actual fun ViewContext.h5(setup: TextView.() -> Unit): Unit = textElement("h5", setup)
@ViewDsl actual fun ViewContext.h6(setup: TextView.() -> Unit): Unit = textElement("h6", setup)
@ViewDsl actual fun ViewContext.text(setup: TextView.() -> Unit): Unit = textElement("p", setup)
actual inline var TextView.TextView_content: String
    get() = innerText
    set(value) { innerText = value }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ActivityIndicator = HTMLElement
@ViewDsl actual fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit = todo("activityIndicator")

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Space = HTMLElement
@ViewDsl actual fun ViewContext.space(setup: Space.() -> Unit): Unit = todo("space")

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Button = HTMLButtonElement
@ViewDsl actual fun ViewContext.button(setup: Button.() -> Unit): Unit = themedElement<HTMLButtonElement>("button", setup)
actual fun Button.onClick(action: () -> Unit): Unit { onclick = { action() } }
actual inline var Button.Button_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Checkbox = HTMLInputElement
@ViewDsl actual fun ViewContext.checkbox(setup: Checkbox.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    this.type = "checkbox"
    setup()
}
actual inline var Checkbox.Checkbox_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }
actual val Checkbox.Checkbox_checked: Writable<Boolean> get() = vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RadioButton = HTMLInputElement
@ViewDsl actual fun ViewContext.radioButton(setup: RadioButton.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    this.type = "radio"
    setup()
}
actual inline var RadioButton.RadioButton_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }
actual val RadioButton.RadioButton_checked: Writable<Boolean> get() = vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias Switch = HTMLInputElement
@ViewDsl actual fun ViewContext.switch(setup: Switch.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    this.type = "checkbox"
    setup()
}
actual inline var Switch.Switch_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }
actual val Switch.Switch_checked: Writable<Boolean> get() = vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias ToggleButton = HTMLInputElement
@ViewDsl actual fun ViewContext.toggleButton(setup: ToggleButton.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    this.type = "checkbox"
    setup()
}
actual inline var ToggleButton.ToggleButton_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }
actual val ToggleButton.ToggleButton_checked: Writable<Boolean> get() = vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RadioToggleButton = HTMLInputElement
@ViewDsl actual fun ViewContext.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    this.type = "checkbox"
    setup()
}
actual inline var RadioToggleButton.RadioToggleButton_enabled: Boolean
    get() = !disabled
    set(value) { disabled = !value }
actual val RadioToggleButton.RadioToggleButton_checked: Writable<Boolean> get() = vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextField = HTMLInputElement
@ViewDsl actual fun ViewContext.textField(setup: TextField.() -> Unit): Unit = themedElement<HTMLInputElement>("input") {
    setup()
}
actual val TextField.TextField_content: Writable<String> get() = vprop("input", { value }, { value = it })
actual inline var TextField.TextField_keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                type = "email"
                autocomplete = "email"
            }

            AutoComplete.Password -> {
                type = "password"
                autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                type = "password"
                autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                autocomplete = "tel"
            }

            null -> {
                autocomplete = "off"
            }
        }
    }
actual inline var TextField.TextField_hint: String
    get() = placeholder
    set(value) { placeholder = value }
actual inline var TextField.TextField_range: ClosedRange<Double>?
    get() {
        if(min.isBlank()) return null
        if(max.isBlank()) return null
        return min.toDouble()..max.toDouble()
    }
    set(value) {
        value?.let {
            min = it.start.toString()
            max = it.endInclusive.toString()
        } ?: run {
            removeAttribute("min")
            removeAttribute("max")
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias TextArea = HTMLTextAreaElement
@ViewDsl actual fun ViewContext.textArea(setup: TextArea.() -> Unit): Unit = themedElement("textarea", setup)
actual val TextArea.TextArea_content: Writable<String> get() = vprop("input", { value }, { value = it })
actual inline var TextArea.TextArea_keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        when (value.autocomplete) {
            AutoComplete.Email -> {
                autocomplete = "email"
            }

            AutoComplete.Password -> {
                autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                autocomplete = "tel"
            }

            null -> {
                autocomplete = "off"
            }
        }
    }
actual inline var TextArea.TextArea_hint: String
    get() = TODO()
    set(value) { }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias DropDown = HTMLSelectElement
@ViewDsl actual fun ViewContext.dropDown(setup: DropDown.() -> Unit): Unit = themedElement("select", setup)
actual val DropDown.DropDown_selected: Writable<String?> get() = vprop("change", { value }, { value = it ?: "" })
actual inline var DropDown.DropDown_options: List<WidgetOption>
    get() = TODO()
    set(value) {
        val v = this.value
        innerHTML = ""
        __resetContentToOptionList(value, v)
    }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias AutoCompleteTextField = HTMLInputElement
@ViewDsl actual fun ViewContext.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit): Unit = themedElement("input", setup)
actual val AutoCompleteTextField.AutoCompleteTextField_content: Writable<String> get() = vprop("input", { value }, { value = it })
actual inline var AutoCompleteTextField.AutoCompleteTextField_suggestions: List<WidgetOption>
    get() = TODO()
    set(value) {
        val listId = getAttribute("list") ?: run {
            val newId = "datalist" + Random.nextInt(0, Int.MAX_VALUE)
            document.body!!.appendChild((document.createElement("datalist") as HTMLDataListElement).apply {
                id = newId
            })
            setAttribute("list", newId)
            newId
        }
        document.getElementById(listId).apply {
            __resetContentToOptionList(value, this@AutoCompleteTextField_suggestions.value)
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias SwapView = HTMLDivElement
@ViewDsl actual fun ViewContext.swapView(setup: SwapView.() -> Unit): Unit = themedElement("div", setup)
actual inline var SwapView.SwapView_currentView: NView
    get() = TODO()
    set(value) {
        val oldView = lastElementChild as? HTMLElement
        appendChild(value)
        oldView?.let { removeChild(it) }
    }
actual fun SwapView.setCurrentViewWithTransition(view: NView, animation: ScreenTransition): Unit {
    // TODO use a transition
    SwapView_currentView = view
}

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias WebView = HTMLIFrameElement
@ViewDsl actual fun ViewContext.webView(setup: WebView.() -> Unit): Unit = themedElement("iframe", setup)
actual inline var WebView.WebView_url: String
    get() = src
    set(value) { src = value }
actual inline var WebView.WebView_permitJs: Boolean
    get() = TODO()
    set(value) { TODO() }
actual inline var WebView.WebView_content: String
    get() = TODO()
    set(value) { TODO() }

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias RecyclerView = HTMLDivElement
@ViewDsl actual fun ViewContext.recyclerView(setup: RecyclerView.() -> Unit): Unit = themedElement("div", setup)
@ViewDsl actual fun ViewContext.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit = themedElement("div", setup)
@ViewDsl actual fun ViewContext.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit = themedElement("div", setup)
actual inline var RecyclerView.RecyclerView_renderer: ListRenderer<*>
    get() = TODO()
    set(value) { }
@ViewModifierDsl3 actual fun ViewContext.weight(amount: Float): ViewWrapper {
    elementToDoList.add {
        style.flexGrow = "$amount"
        style.flexShrink = "$amount"
    }
    return ViewWrapper
}
@ViewModifierDsl3 actual fun ViewContext.gravity(horizontal: Align, vertical: Align): ViewWrapper = ViewWrapper
@ViewModifierDsl3 actual fun ViewContext.scrolls(): ViewWrapper{
    elementToDoList.add {
        style.overflowY = "scroll"
    }
    return ViewWrapper
}
@ViewModifierDsl3 actual fun ViewContext.scrollsHorizontally(): ViewWrapper{
    elementToDoList.add {
        style.display = "flex"
        style.flexDirection = "row"
        style.overflowX = "scroll"
    }
    return ViewWrapper
}
@ViewModifierDsl3 actual fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper {
    elementToDoList.add {
        if (constraints.minHeight == null) style.removeProperty("minHeight")
        else style.minHeight = constraints.minHeight.value

        if (constraints.maxHeight == null) style.removeProperty("maxHeight")
        else style.maxHeight = constraints.maxHeight.value

        if (constraints.minWidth == null) style.removeProperty("minWidth")
        else style.minWidth = constraints.minWidth.value

        if (constraints.maxWidth == null) style.removeProperty("maxWidth")
        else style.maxWidth = constraints.maxWidth.value

        if (constraints.width == null) style.removeProperty("width")
        else style.width = constraints.width.value

        if (constraints.height == null) style.removeProperty("height")
        else style.height = constraints.height.value

        style.overflowX = "hidden"
        style.overflowY = "hidden"

    }
    return ViewWrapper
}
// End
