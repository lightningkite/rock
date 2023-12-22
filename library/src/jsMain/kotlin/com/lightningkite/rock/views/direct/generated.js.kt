package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.toWeb
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import org.w3c.dom.*
import org.w3c.dom.url.URL
import kotlin.random.Random
import kotlinx.datetime.*
import org.w3c.dom.events.Event
import kotlin.js.Date


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSeparator = HTMLElement

@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit = themedElement<HTMLDivElement>("div") {
    classList.add("rock-separator")
    setup(Separator(this))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = HTMLElement

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit =
    themedElementBackIfChanged<HTMLDivElement>("div") {
        classList.add("rock-stack")
        setup(ContainingView(this))
    }

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-col")
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    classList.add("rock-row")
    setup(ContainingView(this))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = HTMLAnchorElement

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit): Unit = themedElementClickable<NLink>("a") {
    this.asDynamic().__ROCK__navigator = navigator
    setup(Link(this))
}

actual inline var Link.to: RockScreen
    get() = this.native.asDynamic().__ROCK__screen as RockScreen
    set(value) {
        this.native.asDynamic().__ROCK__screen = value
        val navigator = (this.native.asDynamic().__ROCK__navigator as RockNavigator)
        navigator.routes.render(value)?.let {
            native.href = it.urlLikePath.render()
        }
        native.onclick = {
            it.preventDefault()
            navigator.navigate(value)
        }
    }
actual inline var Link.newTab: Boolean
    get() = native.target == "_blank"
    set(value) {
        native.target = if (value) "_blank" else "_self"
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = HTMLAnchorElement

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit) =
    themedElementClickable<NExternalLink>("a") { setup(ExternalLink(this)) }

actual inline var ExternalLink.to: String
    get() = native.href
    set(value) {
        native.href = value
    }
actual inline var ExternalLink.newTab: Boolean
    get() = native.target == "_blank"
    set(value) {
        native.target = if (value) "_blank" else "_self"
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImage = HTMLImageElement

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit): Unit =
    themedElement<NImage>("img") { classList.add("tooltip"); setup(Image(this)) }

actual inline var Image.source: ImageSource
    get() = TODO()
    set(value) {
        when (value) {
            is ImageRemote -> native.src = value.url
            is ImageRaw -> native.src = URL.Companion.createObjectURL(Blob(arrayOf(value.data)))
            is ImageResource -> native.src = value.relativeUrl
            is ImageLocal -> native.src = URL.createObjectURL(value.file)
            is ImageVector -> {
                native.src = value.toWeb()
                native.style.width = value.width.value
                native.style.height = value.height.value
            }

            else -> {}
        }
    }
actual inline var Image.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        native.style.objectFit = when (value) {
            ImageScaleType.Fit -> "contain"
            ImageScaleType.Crop -> "cover"
            ImageScaleType.Stretch -> "fill"
            ImageScaleType.NoScale -> "none"
        }
    }
actual inline var Image.description: String?
    get() = native.alt
    set(value) {
        native.alt = value ?: ""
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = HTMLElement

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = headerElement("h1", setup)

@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = headerElement("h2", setup)

@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = headerElement("h3", setup)

@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = headerElement("h4", setup)

@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = headerElement("h5", setup)

@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = headerElement("h6", setup)

@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = textElement("p", setup)

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = textElement("span") {
    native.classList.add("subtext")
    setup()
}

actual inline var TextView.content: String
    get() = native.innerText
    set(value) {
        native.innerText = value
    }
actual inline var TextView.align: Align
    get() = when (window.getComputedStyle(native).textAlign) {
        "start" -> Align.Start
        "center" -> Align.Center
        "end" -> Align.End
        "justify" -> Align.Stretch
        else -> Align.Start
    }
    set(value) {
        native.style.textAlign = when (value) {
            Align.Start -> "start"
            Align.Center -> "center"
            Align.End -> "end"
            Align.Stretch -> "justify"
        }
    }
actual inline var TextView.textSize: Dimension
    get() = Dimension(window.getComputedStyle(native).fontSize)
    set(value) {
        native.style.fontSize = value.value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = HTMLElement

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit): Unit = themedElementBackIfChanged<HTMLDivElement>("div") {
    textElement("span") {
        classList.add("rock-label")
    }
    setup(Label(this))
}

actual inline var Label.content: String
    get() = (native.firstElementChild as? HTMLElement)?.innerText ?: ""
    set(value) {
        (native.firstElementChild as? HTMLElement)?.innerText = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NActivityIndicator = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit =
    themedElement<HTMLSpanElement>("span") {
        addClass("spinner")
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = HTMLElement

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit): Unit = element<NSpace>("span") {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        style.width = (getter().spacing * 4).value
        style.height = (getter().spacing * 4).value
    }
    setup(s)
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element<NSpace>("span") {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        style.width = (getter().spacing * multiplier).value
        style.height = (getter().spacing * multiplier).value
    }
    setup(s)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = HTMLDivElement

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit): Unit =
    themedElementPrivateMeta<NDismissBackground>(
        name = "span",
        themeLogic = { _, _, virtualClasses ->
            virtualClasses.add("dismissBackground")
            virtualClasses.add("inclBack")
        },
        setup = {
            setup(DismissBackground(this))
        }
    )

actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    native.onclick = { launch { action() } }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = HTMLButtonElement

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit): Unit =
    themedElementClickable<NButton>("button") { setup(Button(this)) }

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    native.onclick = { launch { action() } }
}

actual inline var Button.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = HTMLInputElement

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit): Unit = themedElementClickable<HTMLInputElement>("input") {
    this.type = "checkbox"
    setup(Checkbox(this))
}

actual inline var Checkbox.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }
actual val Checkbox.checked: Writable<Boolean> get() = native.vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = HTMLInputElement

fun <T : HTMLElement, V> T.vprop(
    eventName: String,
    get: T.() -> V,
    set: T.(V) -> Unit
): Writable<V> {
    return object : Writable<V> {
        override suspend fun awaitRaw(): V = get(this@vprop)
        override suspend fun set(value: V) {
            set(this@vprop, value)
        }

        private var block = false

        override fun addListener(listener: () -> Unit): () -> Unit {
            val callback: (Event) -> Unit = { listener() }
            this@vprop.addEventListener(eventName, callback)
            return { this@vprop.removeEventListener(eventName, callback) }
        }

    }
}

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit): Unit =
    themedElementClickable<HTMLInputElement>("input") {
        this.type = "radio"
        setup(RadioButton(this))
    }

actual inline var RadioButton.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }
actual val RadioButton.checked: Writable<Boolean> get() = native.vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = HTMLInputElement

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit): Unit = themedElementClickable<HTMLInputElement>("input") {
    this.type = "checkbox"
    this.classList.add("switch")
    setup(Switch(this))
}

actual inline var Switch.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }
actual val Switch.checked: Writable<Boolean> get() = native.vprop("input", { checked }, { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit): Unit = element<HTMLLabelElement>("label") {
    classList.add("toggle-button")
    element<HTMLInputElement>("input") {
        this.type = "checkbox"
        this.hidden = true
    }
    themedElementClickable<HTMLSpanElement>("span") {
        classList.add("checkResponsive")
        setup(ToggleButton(this))
    }
}

actual inline var ToggleButton.enabled: Boolean
    get() = !(this.native.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() = (this.native.previousElementSibling as HTMLInputElement).vprop(
        "input",
        { checked },
        { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = HTMLSpanElement

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit =
    element<HTMLLabelElement>("label") {
        classList.add("toggle-button")
        element<HTMLInputElement>("input") {
            this.type = "radio"
            this.hidden = true
        }
        themedElementClickable<HTMLSpanElement>("span") {
            classList.add("checkResponsive")
            setup(RadioToggleButton(this))
        }
    }

actual inline var RadioToggleButton.enabled: Boolean
    get() = !(this.native.previousElementSibling as HTMLInputElement).disabled
    set(value) {
        (this.native.previousElementSibling as HTMLInputElement).disabled = !value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() = (this.native.previousElementSibling as HTMLInputElement).vprop(
        "input",
        { checked },
        { checked = it })

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = HTMLInputElement

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit): Unit =
    themedElementEditable<HTMLInputElement>("input") {
        type = "date"
        setup(LocalDateField(this))
    }

actual val LocalDateField.content: Writable<LocalDate?>
    get() = native.vprop(
        "input",
        {
            (native.valueAsDate as? Date)?.toKotlinInstant()?.toLocalDateTime(
                TimeZone.currentSystemDefault()
            )?.date
        },
        {
            valueAsDate = it?.let { LocalDateTime(it, LocalTime(12, 0, 0)).toInstant(TimeZone.currentSystemDefault()) }
        }
    )
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalDateField.range: ClosedRange<LocalDate>?
    get() = TODO()
    set(value) {
        value?.let {
            native.min = it.start.toString()
            native.max = it.endInclusive.toString()
        } ?: run {
            native.removeAttribute("min")
            native.removeAttribute("max")
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = HTMLInputElement

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit): Unit =
    themedElementEditable<HTMLInputElement>("input") {
        type = "time"
        setup(LocalTimeField(this))
    }

actual val LocalTimeField.content: Writable<LocalTime?>
    get() = native.vprop(
        "input",
        {
            (native.valueAsDate as? Date)?.toKotlinInstant()?.toLocalDateTime(
                TimeZone.currentSystemDefault()
            )?.time
        },
        {
            valueAsDate =
                it?.let { LocalDateTime(LocalDate(1970, 1, 1), it).toInstant(TimeZone.currentSystemDefault()) }
        }
    )
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
    get() = TODO()
    set(value) {
        value?.let {
            native.min = it.start.toString().take(5)
            native.max = it.endInclusive.toString().take(5)
        } ?: run {
            native.removeAttribute("min")
            native.removeAttribute("max")
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = HTMLInputElement

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit): Unit =
    themedElementEditable<HTMLInputElement>("input") {
        type = "datetime-local"
        setup(LocalDateTimeField(this))
    }

actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() = native.vprop(
        "input",
        {
            (native.valueAsDate as? Date)?.toKotlinInstant()?.toLocalDateTime(
                TimeZone.currentSystemDefault()
            )
        },
        {
            valueAsDate = it?.let { it.toInstant(TimeZone.currentSystemDefault()) }
        }
    )
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() = TODO()
    set(value) {
        value?.let {
            native.min = it.start.toString()
            native.max = it.endInclusive.toString()
        } ?: run {
            native.removeAttribute("min")
            native.removeAttribute("max")
        }
    }


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = HTMLInputElement

@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit): Unit =
    themedElementEditable<HTMLInputElement>("input") {
        setup(TextField(this))
    }

actual val TextField.content: Writable<String> get() = native.vprop("input", { value }, { value = it })
actual inline var TextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        native.type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "text"
        }
        native.inputMode = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "decimal"
            KeyboardType.Integer -> "numeric"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "email"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.type = "email"
                native.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.type = "password"
                native.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                native.type = "password"
                native.autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                native.autocomplete = "tel"
            }

            null -> {
                native.autocomplete = "off"
            }
        }
    }
actual var TextField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var TextField.hint: String
    get() = native.placeholder
    set(value) {
        native.placeholder = value
    }
actual inline var TextField.range: ClosedRange<Double>?
    get() {
        if (native.min.isBlank()) return null
        if (native.max.isBlank()) return null
        return native.min.toDouble()..native.max.toDouble()
    }
    set(value) {
        value?.let {
            native.min = it.start.toString()
            native.max = it.endInclusive.toString()
        } ?: run {
            native.removeAttribute("min")
            native.removeAttribute("max")
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = HTMLTextAreaElement

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit): Unit =
    themedElementEditable<NTextArea>("textarea") { setup(TextArea(this)) }

actual val TextArea.content: Writable<String> get() = native.vprop("input", { value }, { value = it })
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                native.autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                native.autocomplete = "tel"
            }

            null -> {
                native.autocomplete = "off"
            }
        }
    }
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = HTMLSelectElement

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit): Unit =
    themedElementClickable<NSelect>("select") { setup(Select(this)) }

actual fun <T> Select.bind(
    edits: Writable<T>,
    data: suspend () -> List<T>,
    render: (T) -> String
) {
    var list: List<T> = listOf()
    reactiveScope {
        list = data()
        native.__resetContentToOptionList(list.mapIndexed { index, t ->
            WidgetOption(index.toString(), render(t))
        }, list.indexOf(edits.awaitRaw()).toString())
    }
    native.onchange = {
        launch { native.value.toIntOrNull()?.let { list.getOrNull(it) }?.let { edits set it } }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = HTMLInputElement

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit): Unit =
    themedElementEditable<NAutoCompleteTextField>("input") { setup(AutoCompleteTextField(this)) }

actual val AutoCompleteTextField.content: Writable<String>
    get() = native.vprop("input", { value }, {
        value = it
    })
actual inline var AutoCompleteTextField.suggestions: List<String>
    get() = TODO()
    set(value) {
        val listId = native.getAttribute("list") ?: run {
            val newId = "datalist" + Random.nextInt(0, Int.MAX_VALUE)
            document.body!!.appendChild((document.createElement("datalist") as HTMLDataListElement).apply {
                id = newId
            })
            native.setAttribute("list", newId)
            newId
        }
        document.getElementById(listId)?.let { it as? HTMLElement }?.apply {
            __resetContentToOptionList(value.map { WidgetOption(it, it) }, this@suggestions.native.value)
        }
    }
actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        native.type = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "number"
            KeyboardType.Integer -> "number"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "text"
        }
        native.inputMode = when (value.type) {
            KeyboardType.Text -> "text"
            KeyboardType.Decimal -> "decimal"
            KeyboardType.Integer -> "numeric"
            KeyboardType.Phone -> "tel"
            KeyboardType.Email -> "email"
        }

        when (value.autocomplete) {
            AutoComplete.Email -> {
                native.type = "email"
                native.autocomplete = "email"
            }

            AutoComplete.Password -> {
                native.type = "password"
                native.autocomplete = "current-password"
            }

            AutoComplete.NewPassword -> {
                native.type = "password"
                native.autocomplete = "new-password"
            }

            AutoComplete.Phone -> {
                native.autocomplete = "tel"
            }

            null -> {
                native.autocomplete = "off"
            }
        }
    }
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {
        native.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit): Unit = themedElement<NSwapView>("div") {
    classList.add("rock-swap")
    setup(SwapView(this))
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = themedElement<NSwapView>("div") {
    classList.add("rock-swap")
    classList.add("dialog")
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: () -> Unit): Unit {
    native.asDynamic().__ROCK__next = null
    val alreadyChanging = native.asDynamic().__ROCK__swapping as? Boolean
    if (alreadyChanging == true) {
        native.asDynamic().__ROCK__next = transition to createNewView
        return
    }
    native.asDynamic().__ROCK__swapping = true
    val keyframeName = DynamicCSS.transition(transition)
    val previousLast = native.lastElementChild
    native.children.let { (0 until it.length).map { i -> it.get(i) } }.filterIsInstance<HTMLElement>()
        .forEach { view ->
            if (view.asDynamic().__ROCK__removing) return@forEach
            view.asDynamic().__ROCK__removing = true
            view.style.animation = "${keyframeName}-exit 0.25s"
            val parent = view.parentElement
            window.setTimeout({
                if (view.parentElement == parent) {
                    native.removeChild(view)
                }
            }, 250)
        }
    createNewView()
    (native.lastElementChild as? HTMLElement).takeUnless { it == previousLast }?.let { newView ->
        native.hidden = false
        newView.style.animation = "${keyframeName}-enter 0.25s"
        newView.style.marginLeft = "auto"
        newView.style.marginRight = "auto"
    } ?: run {
        native.hidden = true
    }
    native.asDynamic().__ROCK__swapping = false
    (native.asDynamic().__ROCK__next as? Pair<ScreenTransition, () -> Unit>)?.let {
        swap(it.first, it.second)
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = HTMLIFrameElement

@ViewDsl
actual fun ViewWriter.webView(setup: WebView.() -> Unit): Unit =
    themedElement<NWebView>("iframe") { setup(WebView(this)) }

actual inline var WebView.url: String
    get() = native.src
    set(value) {
        native.src = value
    }
actual inline var WebView.permitJs: Boolean
    get() = TODO()
    set(value) {
        TODO()
    }
actual inline var WebView.content: String
    get() = TODO()
    set(value) {
        TODO()
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = HTMLCanvasElement

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = element<HTMLCanvasElement>("canvas") {
    setup(Canvas(this))
}

actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {
    if (native.width != native.scrollWidth || native.height != native.scrollHeight) {
        native.width = native.scrollWidth
        native.height = native.scrollHeight
    }
    native.getContext("2d").apply {
        this as DrawingContext2D
        this.lineCap = CanvasLineCap.ROUND
        this.lineJoin = CanvasLineJoin.ROUND
        action(this)
    }
}

actual val Canvas.width: Readable<Double> get() = SizeReader(native, "scrollWidth")
actual val Canvas.height: Readable<Double> get() = SizeReader(native, "scrollHeight")
actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointerdown", pointerListenerHandler(action))
}

actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointermove", pointerListenerHandler(action))
}

actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    val l = pointerListenerHandler(action)
    native.addEventListener("pointercancel", l)
    native.addEventListener("pointerleave", l)
}

actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.addEventListener("pointerup", pointerListenerHandler(action))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler-horz")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit =
    themedElement<NRecyclerView>("div") {
        classList.add("recycler-grid")
        this.asDynamic().__viewWriter = split()
        setup(RecyclerView(this))
    }

actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
        TODO()
    }

actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
): Unit {
    val writer = this.native.asDynamic().__viewWriter as ViewWriter
    writer.forEachUpdating(items, render)
}

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit
): ViewWrapper {
    containsNext<HTMLDivElement>("div") {
        onclick = {
            // TODO
        }
        style.position = "relative"
        element<HTMLDivElement>("div") {
            if (!requireClick) classList.add("visibleOnParentHover")
            style.position = "absolute"
            style.zIndex = "9999"
            if (preferredDirection.horizontal) {
                if (preferredDirection.after) {
                    style.left = "100%"
                } else {
                    style.right = "0"
                }
                when (preferredDirection.align) {
                    Align.Start -> style.bottom = "0"
                    Align.End -> style.top = "0"
                    else -> style.top = "calc(50% - 0)"
                }
            } else {
                if (preferredDirection.after) {
                    style.top = "100%"
                } else {
                    style.bottom = "0"
                }
                when (preferredDirection.align) {
                    Align.Start -> style.right = "0"
                    Align.End -> style.left = "0"
                    else -> style.left = "calc(50% - 0)"
                }
            }
            setup()
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    beforeNextElementSetup {
        style.flexGrow = "$amount"
        style.flexShrink = "$amount"
        style.flexBasis = "0"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        classList.add("h${horizontal}", "v${vertical}")
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("scroll-vertical")
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("scroll-horizontal")
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {

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
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.marginless: ViewWrapper
    get() {
        beforeNextElementSetup {
            style.margin = 0.px.value
            style.borderRadius = 0.px.value
            style.borderWidth = 0.px.value
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("addPadding")
        }
        return ViewWrapper
    }
// End