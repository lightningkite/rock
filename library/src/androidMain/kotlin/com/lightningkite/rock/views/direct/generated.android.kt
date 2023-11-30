package com.lightningkite.rock.views.direct

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Button as AndroidButton
import android.widget.TextView as AndroidTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.updateLayoutParams
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = ViewGroup

actual typealias NTextView = android.widget.TextView
actual typealias NLabel = android.widget.TextView
actual typealias NLink = android.widget.TextView
actual typealias NExternalLink = android.widget.TextView
actual typealias NImage = ImageView
actual typealias NActivityIndicator = ProgressBar
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = android.widget.Button
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = android.widget.CheckBox
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = android.widget.RadioButton
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = SwitchCompat
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = DatePicker
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = TimePicker
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = CalendarView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = EditText
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = android.widget.TextView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = Spinner
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = EditText
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = AndroidSwapView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = android.webkit.WebView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = androidx.recyclerview.widget.RecyclerView
actual class NDismissBackground(c: Context) : NView(c)

actual fun ViewWriter.stack(setup: ContainingView.() -> Unit) =  element(::FrameLayout, ::ContainingView, setup)
@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit) {
    element(::LinearLayout, ::ContainingView) {
        val l = this.native as LinearLayout
        l.orientation = LinearLayout.VERTICAL
        setup(ContainingView(l))
    }

}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
    element(::LinearLayout, ::ContainingView) {
        val l = this.native as LinearLayout
        l.orientation = LinearLayout.HORIZONTAL
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit) = element(::AndroidButton, ::Button, setup)

fun ViewWriter.textElement(textSize: Float, setup: TextView.() -> Unit) = element(::AndroidTextView, ::TextView) {
    val androidText = this.native
    androidText.textSize = textSize
    setup(TextView(androidText))
}
fun ViewWriter.header(textSize: Float, setup: TextView.() -> Unit) = element(::AndroidTextView, ::TextView) {
    val androidText = this.native
    androidText.textSize = textSize
    androidText.setTypeface(androidText.typeface, Typeface.BOLD)
    setup(TextView(androidText))
}

object TextSizes {
    var h1 = 26f
    var h2 = 24f
    var h3 = 22f
    var h4 = 20f
    var h5 = 18f
    var h6 = 16f
    var defaultHeader = 20f
    var body = 16f
    var subtext = 14f
}


@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = header(TextSizes.h1, setup)
@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = header(TextSizes.h2, setup)
@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = header(TextSizes.h3, setup)
@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = header(TextSizes.h4, setup)
@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = header(TextSizes.h5, setup)
@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = header(TextSizes.h6, setup)
@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = textElement(TextSizes.body, setup)

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = textElement(TextSizes.subtext, setup)


actual var Link.to: RockScreen
    get() = TODO("Implement")
    set(value) { TODO("Implement") }
actual var Link.newTab: Boolean
    get() {
        TODO()
    }
    set(value) {}


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


actual var Image.source: ImageSource
    get() {
        return this.source
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
actual var TextView.content: String
    get() {
        return this.native.text.toString()
    }
    set(value) {
        this.native.text = value
    }
actual var TextView.align: Align
    get() {
        return when(this.native.gravity) {
            Gravity.START -> Align.Start
            Gravity.END -> Align.End
            Gravity.CENTER -> Align.Center
            Gravity.CENTER_VERTICAL -> Align.Start
            Gravity.CENTER_HORIZONTAL -> Align.Center
            else -> Align.Start
        }
    }
    set(value) {
        when (value) {
            Align.Start -> this.native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
            Align.End -> this.native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_END
            Align.Center -> this.native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_CENTER
            Align.Stretch -> {
                this.native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
                this.native.updateLayoutParams<ViewGroup.LayoutParams> {
                    this.width = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }
        }
    }
actual var TextView.textSize: Dimension
    get() {
        return Dimension(this.native.textSize.toInt())
    }
    set(value) {
        this.native.textSize = value.value.toFloat()
    }
actual var Label.content: String
    get() {
        return this.native.text.toString()
    }
    set(value) {
        this.native.text = value
    }

actual fun DismissBackground.onClick(action: suspend () -> Unit) {}

actual fun Button.onClick(action: suspend () -> Unit) {
    this.native.setOnClickListener({ view ->
        launch { action() }
    })
}
actual var Button.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }


//fun View.doggy() {
//    setOnTouchListener { v, event ->
//        event.action
//    }
//}

//fun <T : View, V> T.vprop(
//    eventName: String,
//    get: T.() -> V,
//    set: T.(V) -> Unit
//): Writable<V> {
//    return object : Writable<V> {
//        override suspend fun awaitRaw(): V = get(this@vprop)
//        override suspend fun set(value: V) {
//            set(this@vprop, value)
//        }
//        private var block = false
//
//        override fun addListener(listener: () -> Unit): () -> Unit {
//
//        }
//    }
//}

actual var Checkbox.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }

object NativeListeners {
    val listeners = ViewListeners<NView>()
}

fun NView.removeListener(listener: () -> Unit): () -> Unit = {
    val key = this
    NativeListeners.listeners.removeListener(key, listener)
    if (NativeListeners.listeners.get(key)?.isEmpty() == true) {
        NativeListeners.listeners.removeKey(key)
    }
}

val CompoundButton.checked: Writable<Boolean>
    get() {
        return object : Writable<Boolean> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                this@checked.setOnCheckedChangeListener { _, _ ->
                    NativeListeners.listeners.get(this@checked)?.forEach { action -> action() }
                }
                return this@checked.removeListener(listener)
            }

            override suspend fun awaitRaw(): Boolean {
                return this@checked.isChecked
            }

            override suspend fun set(value: Boolean) {
                this@checked.isChecked = value
            }
        }
    }
actual val Checkbox.checked: Writable<Boolean>
    get() {
        return this.native.checked
    }

actual var RadioButton.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }
actual val RadioButton.checked: Writable<Boolean>
    get() {
        return this.native.checked
    }


actual var Switch.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }
actual val Switch.checked: Writable<Boolean>
    get() {
        return this.native.checked
    }


actual var ToggleButton.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() {
        TODO("IMPLEMENT TOGGLE BUTTON")
    }


actual var RadioToggleButton.enabled: Boolean
    get() {
        return this.native.isEnabled
    }
    set(value) {
        this.native.isEnabled = value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        TODO("IMPLEMENT RADIO TOGGLE BUTTON")
    }


actual val LocalDateField.content: Writable<LocalDate?>
    get() {
        TODO()
    }
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalDateField.range: ClosedRange<LocalDate>?
    get() {
        TODO()
    }
    set(value) {}


actual val LocalTimeField.content: Writable<LocalTime?>
    get() {
        TODO()
    }
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        TODO()
    }
    set(value) {}


actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() {
        TODO()
    }
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() {
        TODO()
    }
    set(value) {}


actual val TextField.content: Writable<String>
    get() {
        TODO()
    }
actual var TextField.keyboardHints: KeyboardHints
    get() {
        TODO()
    }
    set(value) {}
actual var TextField.action: Action?
    get() = TODO()
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


actual val Select.selected: Writable<String?>
    get() {
        TODO()
    }
actual var Select.options: List<WidgetOption>
    get() {
        TODO()
    }
    set(value) {}


actual val AutoCompleteTextField.content: Writable<String>
    get() {
        TODO()
    }
actual var AutoCompleteTextField.keyboardHints: KeyboardHints
    get() {
        TODO()
    }
    set(value) {}
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {}
actual var AutoCompleteTextField.suggestions: List<String>
    get() {
        TODO()
    }
    set(value) {}



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
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit) {
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



@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) {
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
actual fun ViewWriter.webView(setup: WebView.() -> Unit) {}

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
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit,
): ViewWrapper {
    TODO("Not yet implemented")
}

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit) {
    TODO("Implement")
}

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit) {
}

actual class NSeparator(c: Context) : NView(c)