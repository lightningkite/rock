package com.lightningkite.rock.views.direct

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.view.Gravity
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.CheckBox as AndroidCheckbox
import android.widget.RadioButton as AndroidRadioButton
import android.widget.AutoCompleteTextView as AndroidCompleteTextView
import android.webkit.WebView as AndroidWebView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatToggleButton
import android.widget.Button as AndroidButton
import android.widget.TextView as AndroidTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.text.set
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.datetime.*
import androidx.recyclerview.widget.RecyclerView as AndroidRecyclerView
import java.lang.Exception
import java.text.NumberFormat
import java.text.ParseException
import java.util.Objects


actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = ViewGroup

actual typealias NTextView = AndroidTextView
actual typealias NLabel = AndroidTextView
actual typealias NLink = AndroidTextView
actual typealias NExternalLink = AndroidTextView
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
actual typealias NLocalDateField = AndroidDateField
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = AndroidTimeField
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = AndroidDateTimeField
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = EditText
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = EditText
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = AppCompatSpinner
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = AndroidCompleteTextView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = AndroidSwapView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = android.webkit.WebView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = androidx.recyclerview.widget.RecyclerView

actual fun ViewWriter.stack(setup: ContainingView.() -> Unit) =  element(::FrameLayout, ::ContainingView, setup)
@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit) {
    element(::LinearLayout, ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.VERTICAL
        setup(ContainingView(l))
    }

}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
    element(::LinearLayout, ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.HORIZONTAL
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit) = element(::AndroidButton, ::Button, setup)

fun ViewWriter.textElement(textSize: Float, setup: TextView.() -> Unit) = element(::AndroidTextView, ::TextView) {
    val androidText = native
    androidText.textSize = textSize
    setup(TextView(androidText))
}
fun ViewWriter.header(textSize: Float, setup: TextView.() -> Unit) = element(::AndroidTextView, ::TextView) {
    val androidText = native
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
        return native.text.toString()
    }
    set(value) {
        native.text = value
    }
actual var TextView.align: Align
    get() {
        return when(native.gravity) {
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
            Align.Start -> native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
            Align.End -> native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_END
            Align.Center -> native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_CENTER
            Align.Stretch -> {
                native.textAlignment = android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
                native.updateLayoutParams<ViewGroup.LayoutParams> {
                    this.width = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }
        }
    }
actual var TextView.textSize: Dimension
    get() {
        return Dimension(native.textSize.toInt())
    }
    set(value) {
        native.textSize = value.value.toFloat()
    }
actual var Label.content: String
    get() {
        return native.text.toString()
    }
    set(value) {
        native.text = value
    }

actual fun DismissBackground.onClick(action: suspend () -> Unit) {}

actual fun Button.onClick(action: suspend () -> Unit) {
    native.setOnClickListener { view ->
        launch { action() }
    }
}
actual var Button.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }

actual var Checkbox.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
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
                NativeListeners.listeners.addListener(this@checked, listener)
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
        return native.checked
    }

actual var RadioButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val RadioButton.checked: Writable<Boolean>
    get() {
        return native.checked
    }


actual var Switch.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val Switch.checked: Writable<Boolean>
    get() {
        return native.checked
    }

val NView.selected: Writable<Boolean>
    get() {
        return object : Writable<Boolean> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@selected, listener)
                this@selected.setOnClickListener {_ ->
                    NativeListeners.listeners.get(this@selected)?.forEach { action -> action() }
                }
                return this@selected.removeListener(listener)
            }

            override suspend fun awaitRaw(): Boolean {
                return this@selected.isSelected
            }

            override suspend fun set(value: Boolean) {
                this@selected.isSelected = value
            }
        }
    }

actual var ToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val ToggleButton.checked: Writable<Boolean>
    get() {
        return this@checked.native.selected
    }


actual var RadioToggleButton.enabled: Boolean
    get() {
        return native.isEnabled
    }
    set(value) {
        native.isEnabled = value
    }
actual val RadioToggleButton.checked: Writable<Boolean>
    get() {
        return this@checked.native.selected
    }
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalDateField.range: ClosedRange<LocalDate>?
    get() {
        return native.minDate.toKotlinDate().rangeTo(native.maxDate.toKotlinDate())
    }
    set(value) {
        if (value == null) {
            native.defaultRange()
        } else {
            native.minDate = value.start.toJavaLocalDate()
            native.maxDate = value.endInclusive.toJavaLocalDate()
        }
    }
actual val LocalTimeField.content: Writable<LocalTime?>
    get() {
        return object : Writable<LocalTime?> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@content.native, listener)
                this@content.native.addTextChangedListener {
                    NativeListeners.listeners.get(this@content.native)?.forEach { action -> action() }
                }
                return this@content.native.removeListener(listener)
            }

            override suspend fun awaitRaw(): LocalTime? {
                return LocalTime(this@content.native.time.hour, this@content.native.time.minute)
            }

            override suspend fun set(value: LocalTime?) {
               if (value == null) {
                   this@content.native.setText("")
               } else {
                   this@content.native.time = java.time.LocalTime.of(value.hour, value.minute)
               }
            }
        }
    }
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        return LocalTime(0, 0, 0, 0)
            .rangeTo(LocalTime(23, 59, 59, 0))
    }
    set(value) {
        //Ignore for now.  If we want to make this work will have to do a fair amount of custom stuff, also seems less
        //than useful.
    }


actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() {
        return object : Writable<LocalDateTime?> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@content.native, listener)
                this@content.native.addTextChangedListener {
                    NativeListeners.listeners.get(this@content.native)?.forEach { action -> action() }
                }
                return this@content.native.removeListener(listener)
            }

            override suspend fun awaitRaw(): LocalDateTime? {
                return LocalDateTime(
                    this@content.native.dateTime.year,
                    this@content.native.dateTime.month,
                    this@content.native.dateTime.dayOfMonth,
                    this@content.native.dateTime.hour,
                    this@content.native.dateTime.minute,
                    this@content.native.dateTime.second
                )
            }

            override suspend fun set(value: LocalDateTime?) {
                if (value != null) {
                    this@content.native.dateTime = java.time.LocalDateTime.of(
                        value.year, value.month, value.dayOfMonth, value.hour, value.minute, value.second
                    )
                } else {
                    this@content.native.setText("")
                }
            }
        }
    }
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {}
actual var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() {
        val minDate: java.time.LocalDateTime = this@range.native.minDate
        val maxDate: java.time.LocalDateTime = this@range.native.maxDate
        val min = minDate.toKotlinDateTime()
        val max = maxDate.toKotlinDateTime()
        return min.rangeTo(max)
    }

    set(value) {
        if (value != null) {
            this@range.native.minDate = value.start.toJavaLocalDateTime()
            this@range.native.maxDate = value.endInclusive.toJavaLocalDateTime()
        }
    }

fun View.stringWritable(addNativeListener: () -> Unit, getString: () -> String, setString: (String) -> Unit): Writable<String> {
    return object: Writable<String> {
        override fun addListener(listener: () -> Unit): () -> Unit {
            NativeListeners.listeners.addListener(this@stringWritable, listener)
            addNativeListener()
            return this@stringWritable.removeListener(listener)
        }

        override suspend fun awaitRaw(): String {
            return getString()
        }

        override suspend fun set(value: String) {
            setString(value)
        }
    }
}
fun View.stringNullableWritable(addNativeListener: () -> Unit, getString: () -> String, setString: (String) -> Unit): Writable<String?> {
    return object: Writable<String?> {
        override fun addListener(listener: () -> Unit): () -> Unit {
            NativeListeners.listeners.addListener(this@stringNullableWritable, listener)
            addNativeListener()
            return this@stringNullableWritable.removeListener(listener)
        }

        override suspend fun awaitRaw(): String? {
            return getString()
        }

        override suspend fun set(value: String?) {
            setString(value ?: "")
        }
    }
}

val AndroidTextView.content: Writable<String>
    get() {
        return this@content.stringWritable(
            addNativeListener = {
               this@content.addTextChangedListener { _ ->
                   NativeListeners.listeners.get(this@content)?.forEach { action -> action() }
               }
            },
            getString = { this@content.text.toString() },
            setString = { value -> this@content.text = value }
        )
    }


actual val TextField.content: Writable<String>
    get() {
        return this@content.native.content
    }

var EditText.keyboardHints: KeyboardHints
    get() {
        return when(inputType) {
            InputType.TYPE_CLASS_NUMBER -> KeyboardHints(KeyboardCase.None, KeyboardType.Integer)
            InputType.TYPE_CLASS_TEXT -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_MASK_CLASS -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_MASK_VARIATION -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_MASK_FLAGS -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_NULL -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS -> KeyboardHints(KeyboardCase.Letters, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_CAP_WORDS -> KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES -> KeyboardHints(KeyboardCase.Sentences, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_AUTO_CORRECT -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_MULTI_LINE -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_FLAG_ENABLE_TEXT_CONVERSION_SUGGESTIONS -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_URI -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> KeyboardHints(KeyboardCase.None, KeyboardType.Email)
            InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT -> KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME -> KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS -> KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_FILTER -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_PHONETIC -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS -> KeyboardHints(KeyboardCase.None, KeyboardType.Email)
            InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            InputType.TYPE_CLASS_PHONE -> KeyboardHints(KeyboardCase.None, KeyboardType.Phone)
            InputType.TYPE_CLASS_DATETIME -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
            else -> KeyboardHints(KeyboardCase.None, KeyboardType.Text)
        }
    }

    set(value) {
        val n = this
        val inputType = when (value.type) {
            KeyboardType.Decimal -> {
                InputType.TYPE_CLASS_NUMBER
            }
            KeyboardType.Text -> {
                when (value.case) {
                    KeyboardCase.Words -> InputType.TYPE_TEXT_FLAG_CAP_WORDS
                    KeyboardCase.Letters -> InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    KeyboardCase.Sentences -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    else -> InputType.TYPE_CLASS_TEXT
                }
            }
            KeyboardType.Integer -> InputType.TYPE_CLASS_NUMBER
            KeyboardType.Phone -> InputType.TYPE_CLASS_PHONE
            KeyboardType.Email -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        n.inputType = inputType
    }

actual var TextField.keyboardHints: KeyboardHints
    get() {
        return native.keyboardHints
    }
    set(value) {
        native.keyboardHints = value
    }
actual var TextField.action: Action?
    get() = TODO()
    set(value) {}
actual var TextField.hint: String
    get() {
        return this@hint.native.hint.toString()
    }
    set(value) {
        this@hint.native.hint = value
    }

val numberFormat = NumberFormat.getNumberInstance()
actual var TextField.range: ClosedRange<Double>?
    get() {
        return native.tag as? ClosedRange<Double>
    }
    set(value) {
        if (value == null) return

        native.tag = value
        native.doAfterTextChanged {
            try {
                if (it == null) return@doAfterTextChanged

                val string = it.toString()
                val doubleValue = string.toDouble()
                if (doubleValue < value.start || doubleValue > value.endInclusive) {
                    val newValue = doubleValue.coerceIn(value)
                    it.clear()
                    it.append(newValue.toString())
                }
            } catch (ex: Exception) { ex.printStackTrace() }
        }
    }


actual val TextArea.content: Writable<String>
    get() {
        return this@content.native.content
    }
actual var TextArea.keyboardHints: KeyboardHints
    get() {
        return native.keyboardHints
    }
    set(value) {
        native.keyboardHints = value
    }
actual var TextArea.hint: String
    get() {
        return this@hint.native.hint.toString()
    }
    set(value) {
        this@hint.native.hint = value
    }



actual val Select.selected: Writable<String?>
    get() {
        return this@selected.native.stringNullableWritable(
            addNativeListener = {
                this@selected.native.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        NativeListeners.listeners.get(this@selected.native)?.forEach { action -> action() }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            },
            getString = { this@selected.native.selectedItem.toString() },
            setString = { value ->
                val adapter = this@selected.native.adapter
                val count = adapter.count
                var counter = 0
                while(counter < count) {
                    val item = adapter.getItem(counter).toString()
                    if (item == value) {
                        break;
                    }
                    counter++
                }

                if (counter < count) {
                    this@selected.native.setSelection(counter)
                }
            }
        )
    }
actual var Select.options: List<WidgetOption>
    get() {
        val adapter = native.adapter
        val options = mutableListOf<WidgetOption>()
        val count = adapter.count
        var counter = 0
        while(counter < count) {
            adapter.getItem(counter)?.let { options.add(it as WidgetOption) }
            counter++
        }
        return options
    }
    set(value) {
        native.adapter = ArrayAdapter(native.context, android.R.layout.simple_list_item_1, value)
    }


actual val AutoCompleteTextField.content: Writable<String>
    get() {
        return native.content
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
        return native.url ?: ""
    }
    set(value) {
        native.loadUrl(value)
    }
actual var WebView.permitJs: Boolean
    get() {
        return native.settings.javaScriptEnabled
    }
    set(value) {
        native.settings.javaScriptEnabled = value
    }
actual var WebView.content: String
    get() {
        TODO("NOT IMPLEMENTED")
    }

    set(value) {
        native.loadData(value, null, "utf8")
    }


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

actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {
        val height = constraints.height?.value ?: constraints.maxHeight?.value ?: 0
        val width = constraints.width?.value ?: constraints.maxWidth?.value ?: 0

        minimumHeight = if (constraints.minHeight == null) {
            0
        } else {
            constraints.minHeight.value
        }

        minimumWidth = if (constraints.minWidth == null)  {
            0
        } else {
            constraints.minWidth.value
        }

        updateLayoutParams { this.height = height; this.width = width }
    }
    return ViewWrapper
}
actual val ViewWriter.marginless: ViewWrapper
    get() {
        beforeNextElementSetup {
            this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                setMargins(0, 0, 0, 0)
            }
        }
        return ViewWrapper
    }
actual val ViewWriter.withPadding: ViewWrapper
    get() {
        beforeNextElementSetup {
            val padding = (AndroidAppContext.density * 8).toInt()
            this.setPadding(
                padding,
                padding,
                padding,
                padding
            )
        }
        return ViewWrapper
    }
@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit) {
    return element(::ProgressBar, ::ActivityIndicator, setup)
}

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit) {
    return element(::AndroidTextView, ::Link, setup)
}

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit) {
    return element(::ImageView, ::Image, setup)
}

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit) {
    return element(::View, ::Space, setup)
}

actual fun ViewWriter.space(
    multiplier: Double,
    setup: Space.() -> Unit,
) {
    return element(::View, ::Space, setup)
}


//@Suppress("ACTUAL_WITHOUT_EXPECT")
//actual typealias NDismissBackground = HTMLDivElement
//
//@ViewDsl
//actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit): Unit =
//    themedElementPrivateMeta<NDismissBackground>(
//        name = "span",
//        themeLogic = { _, _ ->
//            classList.add("dismissBackground")
//            classList.add("inclBack")
//        },
//        setup = {
//            setup(DismissBackground(this))
//        }
//    )

actual class NDismissBackground(c: Context) : NView(c)


@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) {
    TODO("NOT YET CERTAIN WHAT THE POINT OF THIS IS.")
}

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit) {
    return element(::AndroidCheckbox, ::Checkbox, setup)
}

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit) {
    return element(::AndroidRadioButton, ::RadioButton, setup)
}

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit) {
    return element(::SwitchCompat, ::Switch, setup)
}

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit) {
    return element(::AppCompatToggleButton, ::ToggleButton, setup)
}

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit) {
    return element(::AndroidDateField, ::LocalDateField) {
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit) {
    return element(::AndroidTimeField, ::LocalTimeField) {
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit) {
    return element(::AndroidDateTimeField, ::LocalDateTimeField) { setup() }
}

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit) {
    return element(::AppCompatToggleButton, ::RadioToggleButton, setup)
}

@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit) {
    return element(::EditText, ::TextField, setup)
}

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit) {
    return element(::EditText, ::TextArea, setup)
}

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit) {
    return element(::AppCompatSpinner, ::Select, setup)
}

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit) {
    return element(::AndroidCompleteTextView, ::AutoCompleteTextField, setup)
}

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) {
    return element(::AndroidSwapView, ::SwapView, setup)
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
    return element(::AndroidWebView, ::WebView, setup)
}

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit) {
    return element(::SurfaceView, ::Canvas, setup)
}

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit) {
    element(::AndroidRecyclerView, ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(currentView.context, LinearLayoutManager.VERTICAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit) {
    element(::AndroidRecyclerView, ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(currentView.context, LinearLayoutManager.HORIZONTAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit) {
    element(::AndroidRecyclerView, ::RecyclerView) {
        native.layoutManager = GridLayoutManager(currentView.context, 3)
        setup()
    }
}

actual var RecyclerView.columns: Int
    get() = 1
    set(value) { TODO() }

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
    element(::AndroidTextView, ::Label, setup)
}

actual class NSeparator(c: Context) : NView(c)