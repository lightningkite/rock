package com.lightningkite.rock.views.direct

import android.content.Context
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewModifierDsl3
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

actual class NSeparator(context: Context) : NView(context)
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {}
actual class NContainingView(context: Context) : NView(context)
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit { TODO("") }
actual class NLink(context: Context) : NView(context)
actual var Link.to: RockScreen
    get() {
        TODO()
    }
    set(value) {}
actual var Link.newTab: Boolean
    get() {
        TODO()
    }
    set(value) {}

actual class NExternalLink(context: Context) : NView(context)
actual var ExternalLink.to: String
    get() {
        TODO()
    }
    set(value) {}
actual var ExternalLink.newTab: Boolean
    get() {
        TODO()
    }
    set(value) {}

actual class NImage(context: Context) : NView(context)
actual var Image.source: ImageSource
    get() {
        TODO()
    }
    set(value) {}
actual var Image.scaleType: ImageScaleType
    get() {
        TODO()
    }
    set(value) {}
actual var Image.description: String?
    get() {
        TODO()
    }
    set(value) {}

actual class NTextView(context: Context) : NView(context)
actual var TextView.content: String
    get() {
        TODO()
    }
    set(value) {}
actual var TextView.align: Align
    get() {
        TODO()
    }
    set(value) {}
actual var TextView.textSize: Dimension
    get() {
        TODO()
    }
    set(value) {}

actual class NLabel(context: Context) : NView(context)
actual var Label.content: String
    get() {
        TODO()
    }
    set(value) {}

actual class NActivityIndicator(context: Context) : NView(context)
actual class NSpace(context: Context) : NView(context)
actual fun DismissBackground.onClick(action: suspend () -> Unit) {}
actual class NButton(context: Context) : NView(context)
actual fun Button.onClick(action: suspend () -> Unit) {}
actual var Button.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}

actual class NCheckbox(context: Context) : NView(context)
actual var Checkbox.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual val Checkbox.checked: Writable<Boolean>
    get() {
        TODO()
    }

actual class NRadioButton(context: Context) : NView(context)
actual var RadioButton.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual val RadioButton.checked: Writable<Boolean>
    get() {
        TODO()
    }

actual class NSwitch(context: Context) : NView(context)
actual var Switch.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual val Switch.checked: Writable<Boolean>
    get() {
        TODO()
    }

actual class NToggleButton(context: Context) : NView(context)
actual var ToggleButton.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual val ToggleButton.checked: Writable<Boolean>
    get() {
        TODO()
    }

actual class NRadioToggleButton(context: Context) : NView(context)
actual var RadioToggleButton.enabled: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        TODO()
    }

actual class NLocalDateField(context: Context) : NView(context)
actual val LocalDateField.content: Writable<LocalDate?>
    get() {
        TODO()
    }
actual var LocalDateField.range: ClosedRange<LocalDate>?
    get() {
        TODO()
    }
    set(value) {}

actual class NLocalTimeField(context: Context) : NView(context)
actual val LocalTimeField.content: Writable<LocalTime?>
    get() {
        TODO()
    }
actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        TODO()
    }
    set(value) {}

actual class NLocalDateTimeField(context: Context) : NView(context)
actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() {
        TODO()
    }
actual var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() {
        TODO()
    }
    set(value) {}

actual class NTextField(context: Context) : NView(context)
actual val TextField.content: Writable<String>
    get() {
        TODO()
    }
actual var TextField.keyboardHints: KeyboardHints
    get() {
        TODO()
    }
    set(value) {}
actual var TextField.hint: String
    get() {
        TODO()
    }
    set(value) {}
actual var TextField.range: ClosedRange<Double>?
    get() {
        TODO()
    }
    set(value) {}

actual class NTextArea(context: Context) : NView(context)
actual val TextArea.content: Writable<String>
    get() {
        TODO()
    }
actual var TextArea.keyboardHints: KeyboardHints
    get() {
        TODO()
    }
    set(value) {}
actual var TextArea.hint: String
    get() {
        TODO()
    }
    set(value) {}

actual class NSelect(context: Context) : NView(context)
actual val Select.selected: Writable<String?>
    get() {
        TODO()
    }
actual var Select.options: List<WidgetOption>
    get() {
        TODO()
    }
    set(value) {}

actual class NAutoCompleteTextField(context: Context) : NView(context)
actual val AutoCompleteTextField.content: Writable<String>
    get() {
        TODO()
    }
actual var AutoCompleteTextField.suggestions: List<String>
    get() {
        TODO()
    }
    set(value) {}

actual class NSwapView(context: Context) : NView(context)
actual class NWebView(context: Context) : NView(context)
actual var WebView.url: String
    get() {
        TODO()
    }
    set(value) {}
actual var WebView.permitJs: Boolean
    get() {
        TODO()
    }
    set(value) {}
actual var WebView.content: String
    get() {
        TODO()
    }
    set(value) {}

actual class NCanvas(context: Context) : NView(context)
actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {}
actual val Canvas.width: Readable<Double>
    get() {
        TODO()
    }
actual val Canvas.height: Readable<Double>
    get() {
        TODO()
    }

actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual class NRecyclerView(context: Context) : NView(context)
actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>)->Unit): Unit {}
actual fun ViewWriter.weight(amount: Float): ViewWrapper { TODO("Implement") }
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper { TODO("Implement") }
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        TODO()
    }
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        TODO()
    }

actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper { TODO("Implement") }
actual val ViewWriter.marginless: ViewWrapper
    get() {
        TODO()
    }
actual val ViewWriter.withPadding: ViewWrapper
    get() {
        TODO()
    }

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.header(setup: TextView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit) {
}

actual fun ViewWriter.space(
    multiplier: Double,
    setup: Space.() -> Unit,
) {
}

actual class NDismissBackground(c: Context) : NView(c)

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit) {
}

actual fun SwapView.swap(
    transition: ScreenTransition,
    createNewView: () -> Unit,
) {
}

@ViewDsl
actual fun ViewWriter.webView(setup: WebView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit) {
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit) {
}

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit,
): ViewWrapper {
    TODO("Not yet implemented")
}

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit) {
    TODO("Implement")
}