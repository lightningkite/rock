@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock.views.direct

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.net.Uri
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.lightningkite.rock.R
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import timber.log.Timber
import java.util.*
import kotlin.collections.set
import android.webkit.WebView as AndroidWebView
import android.widget.AutoCompleteTextView as AndroidCompleteTextView
import android.widget.CheckBox as AndroidCheckbox
import android.widget.RadioButton as AndroidRadioButton
import android.widget.TextView as AndroidTextView
import androidx.recyclerview.widget.RecyclerView as AndroidRecyclerView
import com.lightningkite.rock.models.Paint as RockPaint


@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {
    viewElement(factory = ::NSeparator, wrapper = ::Separator) {
        native.updateLayoutParams {
            width = LayoutParams.MATCH_PARENT
            height = (2 * native.resources.displayMetrics.density).toInt()
        }
        applyTheme(native, viewDraws = false)
        setup(this)
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = ViewGroup

actual typealias NTextView = AndroidTextView
actual typealias NLabel = AndroidTextView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = LinkFrameLayout
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = FrameLayout

actual typealias NImage = ImageView
actual typealias NActivityIndicator = ProgressBar
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = FrameLayout
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = android.widget.CheckBox
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = android.widget.RadioButton
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = SwitchCompat
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = FrameLayout
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = View
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
actual typealias NSwapView = FrameLayout
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = android.webkit.WebView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = SurfaceView
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = androidx.recyclerview.widget.RecyclerView

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit) = viewElement(
    factory = ::FrameLayout,
    wrapper = ::ContainingView
) {
    applyTheme(native, viewDraws = false) { theme, view ->
        view.background = colorDrawable(theme.background.colorInt())
    }
    setup(this)
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::LinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.VERTICAL
        applyTheme(l, viewDraws = false)
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit) {
    viewElement(factory = ::LinearLayout, wrapper = ::ContainingView) {
        val l = native as LinearLayout
        l.orientation = LinearLayout.HORIZONTAL
        applyTheme(l, viewDraws = false)
        setup(ContainingView(l))
    }
}

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit) {
    viewElement(factory = ::FrameLayout, wrapper = ::ContainingView) {
        val frame = native as FrameLayout
        applyTheme(frame)
        setup(Button(frame))
    }
}

fun ViewWriter.textElement(textSize: Float, setup: TextView.() -> Unit) =
    viewElement(factory = ::AndroidTextView, wrapper = ::TextView) {
        val androidText = native
        androidText.textSize = textSize
        applyTheme(native, foreground = applyTextColorFromTheme)
        setup(TextView(androidText))
    }

fun ViewWriter.header(textSize: Float, setup: TextView.() -> Unit) =
    viewElement(factory = ::AndroidTextView, wrapper = ::TextView) {
        val androidText = native
        androidText.textSize = textSize
        androidText.setTypeface(androidText.typeface, Typeface.BOLD)
        applyTheme(native, foreground = applyTextColorFromTheme)
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
    get() {
        val rockScreen: RockScreen
        runBlocking {
            rockScreen = (native.context as RockActivity).navigator.currentScreen.await()!!
        }
        return rockScreen
    }
    set(value) {
        native.setOnClickListener {
            native.navigator.navigate(value)
        }
    }

actual var Link.newTab: Boolean
    get() {
        return false
    }
    set(value) {
        Timber.d("New Tab called with value $value")
    }

actual var ExternalLink.to: String
    get() {
        return (native.tag as? Pair<UUID, String>)?.second ?: ""
    }
    set(value) {
        native.tag = UUID.randomUUID() to value
        native.setOnClickListener { view ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                categories.add(Intent.CATEGORY_APP_BROWSER)
                data = Uri.parse(value)
            }
            view.context.packageManager.resolveActivity(intent, intent.flags)?.let {
                view.context.startActivity(intent)
            }
        }
    }
actual var ExternalLink.newTab: Boolean
    get() {
        return false
    }
    set(value) {
        Timber.d("WHAT AM I? $value")
    }


actual var Image.source: ImageSource
    get() {
        return native.tag as ImageResource
    }
    set(value) {
        native.tag = value
        when (value) {
            is ImageRaw -> {
                //TODO()
            }

            is ImageRemote -> {
                //TODO()
            }

            is ImageResource -> {
                Timber.d("HITHER AND THITHER IMAGE RESOURCE")
                native.setImageDrawable(value.drawable)
            }

            is ImageVector -> {
//                val drawable = VectorDrawableCreator.getVectorDrawable(
//                    context = native.context,
//                    width = value.width.value.toInt(),
//                    height = value.height.value.toInt(),
//                    viewportWidth = value.viewBoxWidth.toFloat(),
//                    viewportHeight = value.viewBoxHeight.toFloat(),
//                    paths = value.paths.map {
//                        val path = VectorDrawableCreator.PathData(
//                            it.path,
//                            it.fillColor?.closestColor()?.toInt() ?: 0
//                        )
//                        path
//                    }
//                )
                native.setImageDrawable(
                    ContextCompat.getDrawable(
                        native.context,
                        R.drawable.ic_android_black_24dp
                    )
                )
            }

            else -> {
                throw RuntimeException("Android View Tag is not an Image Source")
            }
        }
    }
actual var Image.scaleType: ImageScaleType
    get() {
        return when (this.native.scaleType) {
            ImageView.ScaleType.MATRIX -> ImageScaleType.NoScale
            ImageView.ScaleType.FIT_XY -> ImageScaleType.Stretch
            ImageView.ScaleType.FIT_START -> ImageScaleType.Fit
            ImageView.ScaleType.FIT_CENTER -> ImageScaleType.Fit
            ImageView.ScaleType.FIT_END -> ImageScaleType.Fit
            ImageView.ScaleType.CENTER -> ImageScaleType.Fit
            ImageView.ScaleType.CENTER_CROP -> ImageScaleType.Crop
            ImageView.ScaleType.CENTER_INSIDE -> ImageScaleType.NoScale
            else -> ImageScaleType.Fit
        }
    }
    set(value) {
        val scaleType: ImageView.ScaleType = when (value) {
            ImageScaleType.Fit -> ImageView.ScaleType.FIT_CENTER
            ImageScaleType.Crop -> ImageView.ScaleType.CENTER_CROP
            ImageScaleType.Stretch -> ImageView.ScaleType.FIT_XY
            ImageScaleType.NoScale -> ImageView.ScaleType.CENTER_INSIDE
        }
        this.native.scaleType = scaleType
    }
actual var Image.description: String?
    get() {
        return native.contentDescription.toString()
    }
    set(value) {
        native.contentDescription = value
    }
actual var TextView.content: String
    get() {
        return native.text.toString()
    }
    set(value) {
        native.text = value
    }
actual var TextView.align: Align
    get() {
        return when (native.gravity) {
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
        return Dimension(native.textSize)
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
                this@selected.setOnClickListener { _ ->
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

private object ViewActions {
    val actions: MutableMap<Int, Action> = mutableMapOf()
}

private val View.getAction: Action? get() = ViewActions.actions[hashCode()]
private fun View.setAction(action: Action?) {
    if (action != null) {
        ViewActions.actions[hashCode()] = action
    } else {
        ViewActions.actions.remove(hashCode())
    }
}

actual var LocalDateField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
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
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }

//Not certain what to do here.  my first thought is to use a tag to hold the range for the time picker
//however unlikely someone could decide to set a custom tag on the native
// view which would then mess up the functionality.

actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        return LocalTime(0, 0, 0, 0)
            .rangeTo(LocalTime(23, 59, 59, 0))
    }
    set(value) {
//        TODO("Not Implemented")
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
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
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

fun View.stringWritable(
    addNativeListener: () -> Unit,
    getString: () -> String,
    setString: (String) -> Unit,
): Writable<String> {
    return object : Writable<String> {
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

fun View.stringNullableWritable(
    addNativeListener: () -> Unit,
    getString: () -> String,
    setString: (String) -> Unit,
): Writable<String?> {
    return object : Writable<String?> {
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
        return when (inputType) {
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
            InputType.TYPE_TEXT_FLAG_ENABLE_TEXT_CONVERSION_SUGGESTIONS -> KeyboardHints(
                KeyboardCase.None,
                KeyboardType.Text
            )

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
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var TextField.hint: String
    get() {
        return this@hint.native.hint.toString()
    }
    set(value) {
        this@hint.native.hint = value
    }
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
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
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
                while (counter < count) {
                    val item = adapter.getItem(counter).toString()
                    if (item == value) {
                        break
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
        while (counter < count) {
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
        return native.keyboardHints
    }
    set(value) {
        native.keyboardHints = value
    }
actual var AutoCompleteTextField.action: Action?
    get() {
        return native.tag as? Action
    }
    set(value) {
        native.tag = value
    }

private class RockStringAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    val items: List<String> = objects
}

actual var AutoCompleteTextField.suggestions: List<String>
    get() {
        return (native.adapter as RockStringAdapter).items
    }
    set(value) {
        native.setAdapter(RockStringAdapter(native.context, AndroidAppContext.autoCompleteLayoutResource, value))
    }


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
        return native.tag as? String ?: ""
    }
    set(value) {
        native.tag = value
        native.loadData(value, null, "utf8")
    }


actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {}
fun View.addLayoutChangeListener(listener: () -> Unit): () -> Unit {
    NativeListeners.listeners.addListener(this, listener)
    this.addOnLayoutChangeListener /* listener = */ { _, _, _, _, _, _, _, _, _ -> listener() }
    return removeListener(listener)
}

actual val Canvas.width: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@width.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@width.native.width.toDouble()
            }
        }
    }
actual val Canvas.height: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@height.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@height.native.height.toDouble()
            }
        }
    }

actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}
actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {}

actual fun <T> RecyclerView.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>) -> Unit): Unit {}

fun View.setPaddingAll(padding: Int) = setPadding(padding, padding, padding, padding)
fun View.setMarginAll(margin: Int) {
    Timber.d("SET MARGIN ALL: $margin for view $this")
    (layoutParams as? MarginLayoutParams)?.setMargins(margin)
}
private fun colorDrawable(color: Int): ColorDrawable = ColorDrawable(color)
private fun RockPaint.colorInt(): Int = closestColor().toInt()
private fun CornerRadii.toFloatArray(): FloatArray {
    return floatArrayOf(
        topLeft.value,
        topLeft.value,
        topRight.value,
        topRight.value,
        bottomLeft.value,
        bottomLeft.value,
        bottomRight.value,
        bottomRight.value
    )
}

private fun noCorners(): FloatArray {
    return floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
}

private fun LinearGradient.orientation(): Orientation {
    return when (angle.degrees.toInt()) {
        in 0..90 -> GradientDrawable.Orientation.LEFT_RIGHT
        in 91..180 -> GradientDrawable.Orientation.TOP_BOTTOM
        in 181..270 -> GradientDrawable.Orientation.RIGHT_LEFT
        in 271..360 -> GradientDrawable.Orientation.BOTTOM_TOP
        else -> GradientDrawable.Orientation.LEFT_RIGHT
    }
}

private fun View.noBorderElevationOrCorners() {
    setMarginAll(0)
    (background as? GradientDrawable)?.cornerRadii = noCorners()
    elevation = 0f
    invalidate()
}

val applyTextColorFromTheme: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
}

fun <T : View>ViewWriter.applyTheme(
    view: T,
    id: String = UUID.randomUUID().toString(),
    viewDraws: Boolean = true,
    background: (Theme) -> Unit = {},
    backgroundRemove: () -> Unit = {},
    foreground: (Theme, T) -> Unit = { _, _  -> },
) {

    val rootTheme = themeStack.size == 1
    val themeGetter = themeStack.lastOrNull() ?: { null }
    val theme2 = currentTheme
    val themeChanged = themeJustChanged
    themeJustChanged = false

    calculationContext.reactiveScope {
//        Timber.d("THEME JUST CHANGED!!!!")
        Timber.d("SET BACKGROUND ON $id")
        val theme = theme2()
        val useBackground = themeChanged && themeGetter() != null
        if (rootTheme) {
            view.noBorderElevationOrCorners()
            return@reactiveScope
        }

        if (viewDraws) {
            view.setMarginAll(theme.spacing.value.toInt())
        }

        if (useBackground || viewDraws && !rootTheme) {
            view.setPaddingAll(theme.spacing.value.toInt())
            view.setMarginAll(theme.spacing.value.toInt())
        } else {
            view.setMarginAll(0)
            view.setPaddingAll(0)
            view.invalidate()
        }

        if (useBackground) {
            val nativeProperties: ViewProperty? = view.tag as? ViewProperty
            val containsMarginLess = nativeProperties?.properties
                ?.contains(ViewProperties.MARGINLESS) ?: false

            val gradientDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                if (!containsMarginLess) {
                    cornerRadii = theme.cornerRadii.toFloatArray()
                    setStroke(theme.outlineWidth.value.toInt(), theme.outline.colorInt())
                }
                when(theme.background) {
                    is Color -> {
                        colors = intArrayOf(theme.background.colorInt(), theme.background.colorInt())
                    }
                    is LinearGradient -> {
                        colors = theme.background.stops.map { it.color.toInt() }.toIntArray()
                        orientation = theme.background.orientation()
                        gradientType = GradientDrawable.LINEAR_GRADIENT
                    }
                    is RadialGradient -> {
                        colors = theme.background.stops.map { it.color.toInt() }.toIntArray()
                        gradientType = GradientDrawable.RADIAL_GRADIENT
                    }
                }
            }

            view.background = gradientDrawable
            background(theme)
        } else {
            backgroundRemove()
        }
        foreground(theme, view)
    }
}

data class ViewProperty(val properties: List<ViewProperties>)
enum class ViewProperties {
    MARGINLESS
}

actual val ViewWriter.marginless: ViewWrapper
    get() {
        afterNextElementSetup {
            this.tag = ViewProperty(listOf(ViewProperties.MARGINLESS))
            this.noBorderElevationOrCorners()
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper
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

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    beforeNextElementSetup {
        try {
            this.updateLayoutParams {
                (this as LinearLayout.LayoutParams).weight = amount
            }
        } catch (ex: Throwable) {
            throw RuntimeException("Weight is only available within a column or row.")
        }
    }
    return ViewWrapper
}

private fun alignmentToGravity(alignment: Align, isVertical: Boolean): Int {
    return when (alignment) {
        Align.Start -> Gravity.START
        Align.Center -> if (isVertical) Gravity.CENTER_VERTICAL else Gravity.CENTER_HORIZONTAL
        Align.End -> Gravity.END
        else -> Gravity.START
    }
}

@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        val params = this.layoutParams as ViewGroup.LayoutParams
        val horizontalGravity = alignmentToGravity(horizontal, isVertical = false)
        val verticalGravity = alignmentToGravity(vertical, isVertical = true)
        if (params is LinearLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else if (params is FrameLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        if (horizontal == Align.Stretch) {
            params.width = LayoutParams.MATCH_PARENT
        }

        if (vertical == Align.Stretch) {
            params.height = LayoutParams.MATCH_PARENT
        }

        this.layoutParams = params
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        wrapNext(ScrollView(this.currentView.context)) {
            layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        wrapNext(HorizontalScrollView(this.currentView.context)) {
            layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {
        val height = constraints.height?.value ?: constraints.maxHeight?.value ?: 0
        val width = constraints.width?.value ?: constraints.maxWidth?.value ?: 0

        minimumHeight = if (constraints.minHeight == null) {
            0
        } else {
            constraints.minHeight.value.toInt()
        }

        minimumWidth = if (constraints.minWidth == null) {
            0
        } else {
            constraints.minWidth.value.toInt()
        }

        updateLayoutParams { this.height = height.toInt(); this.width = width.toInt() }
    }
    return ViewWrapper
}

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit) {
    return viewElement(factory = ::ProgressBar, wrapper = ::ActivityIndicator) {
        applyTheme(native)
        setup(this)
    }
}

var linkCounter = 0;

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit) {
    return viewElement(factory = ::LinkFrameLayout, wrapper = ::Link) {
        native.navigator = navigator
        linkCounter++
        applyTheme(native, "LinkView: $linkCounter")
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit) {
    return viewElement(factory = ::ImageView, wrapper = ::Image) {
        applyTheme(native) { theme, view ->
            afterNextElementSetup {
                view.drawable?.colorFilter = PorterDuffColorFilter(theme.foreground.colorInt(), PorterDuff.Mode.MULTIPLY)
            }
        }
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit) {
    return viewElement(factory = ::NSpace, wrapper = ::Space) {
        applyTheme(native)
        setup(this)
    }
}

actual fun ViewWriter.space(
    multiplier: Double,
    setup: Space.() -> Unit,
) {
    return viewElement(factory = ::View, wrapper = ::Space) {
        applyTheme(native) { theme, view, ->
            view.setPaddingAll((theme.spacing.value * multiplier).toInt())
            setup(this)
        }
    }
}

actual class NDismissBackground(c: Context) : NView(c)


@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) {
    currentView.background = null
}

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit) {
    return viewElement(factory = ::AndroidCheckbox, wrapper = ::Checkbox) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit) {
    return viewElement(factory = ::AndroidRadioButton, wrapper = ::RadioButton) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit) {
    return viewElement(factory = ::SwitchCompat, wrapper = ::Switch) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit) {
    return viewElement(factory = ::FrameLayout, wrapper = ::ToggleButton) {
        applyTheme(native)
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit) {
    return viewElement(factory = ::AndroidDateField, wrapper = ::LocalDateField) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit) {
    return viewElement(factory = ::AndroidTimeField, wrapper = ::LocalTimeField) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit) {
    return viewElement(factory = ::AndroidDateTimeField, wrapper = ::LocalDateTimeField) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit) {
    return viewElement(factory = ::AppCompatToggleButton, wrapper = ::RadioToggleButton) {
        applyTheme(native)
        setup(this)
    }
}


@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit) {
    return viewElement(factory = ::EditText, wrapper = ::TextField) {
        applyTheme<AndroidTextView>(native, foreground = applyTextColorFromTheme)
        setup(this)
    }
}

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit) {
    return viewElement(factory = ::EditText, wrapper = ::TextArea, setup = setup)
}

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit) {
    return viewElement(factory = ::AppCompatSpinner, wrapper = ::Select, setup = setup)
}

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit) {
    return viewElement(factory = ::AndroidCompleteTextView, wrapper = ::AutoCompleteTextField, setup = setup)
}

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) {
    return viewElement(factory = ::FrameLayout, wrapper = ::SwapView, setup = setup)
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit) {

}

actual fun SwapView.swap(
    transition: ScreenTransition,
    createNewView: () -> Unit,
) {
    val oldView = this.native.getChildAt(0)
    val newViewIndex = if (oldView == null) {
        0
    } else {
        1
    }

    val newView = this.native.getChildAt(newViewIndex)

    TransitionManager.beginDelayedTransition(native, TransitionSet().apply {
        transition.exit?.addTarget(oldView ?: View(native.context))
        transition.enter?.addTarget(newView)
        transition.enter?.let { addTransition(it) }
        transition.exit?.let { addTransition(it) }
    })
    oldView?.let { oldNN -> native.removeView(oldNN) }
    createNewView()
}

@ViewDsl
actual fun ViewWriter.webView(setup: WebView.() -> Unit) {
    return viewElement(factory = ::AndroidWebView, wrapper = ::WebView, setup = setup)
}

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit) {
    return viewElement(factory = ::SurfaceView, wrapper = ::Canvas, setup = setup)
}

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(currentView.context, LinearLayoutManager.VERTICAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
        native.layoutManager = LinearLayoutManager(currentView.context, LinearLayoutManager.HORIZONTAL, false)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit) {
    viewElement(factory = ::AndroidRecyclerView, wrapper = ::RecyclerView) {
        native.layoutManager = GridLayoutManager(currentView.context, 3)
        setup()
    }
}

actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
        TODO("WHAT")
    }

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit,
): ViewWrapper {
//    TODO("Not yet implemented")
    return ViewWrapper
}

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit) {
    viewElement(factory = ::FrameLayout, wrapper = ::ExternalLink, setup = setup)
}

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit) {
    viewElement(factory = ::AndroidTextView, wrapper = ::Label, setup = setup)
}

actual class NSeparator : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}