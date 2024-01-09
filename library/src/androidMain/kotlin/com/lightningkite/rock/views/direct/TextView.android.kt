package com.lightningkite.rock.views.direct

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView as AndroidTextView
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

actual typealias NTextView = AndroidTextView

fun ViewWriter.textElement(textSize: Float, setup: TextView.() -> Unit) =
    viewElement(factory = ::AndroidTextView, wrapper = ::TextView) {
        val androidText = native
        androidText.textSize = textSize
        handleTheme(native, foreground = applyTextColorFromTheme)
        setup(TextView(androidText))
    }

fun ViewWriter.header(textSize: Float, setup: TextView.() -> Unit) =
    viewElement(factory = ::AndroidTextView, wrapper = ::TextView) {
        val androidText = native
        androidText.textSize = textSize
        androidText.setTypeface(androidText.typeface, Typeface.BOLD)
        handleTheme(native, foreground = applyTextColorFromTheme)
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
val CompoundButton.checked: Writable<Boolean>
    get() {
        return object : EquatableByRef("checked", this), Writable<Boolean> {
            override fun addListener(listener: () -> Unit): () -> Unit =
                addListener(CompoundButton::setOnCheckedChangeListener, { CompoundButton.OnCheckedChangeListener { _, _ -> listener() } }, listener)
            override suspend fun awaitRaw(): Boolean = this@checked.isChecked
            override suspend fun set(value: Boolean) { this@checked.isChecked = value }
        }
    }
val android.widget.TextView.content: Writable<String>
    get() {
        return object : EquatableByRef("content", this), Writable<String> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                val watcher = object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) { listener() }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                }
                addTextChangedListener(watcher)
                return { removeTextChangedListener(watcher) }
            }
            override suspend fun awaitRaw(): String = this@content.text?.toString() ?: ""
            override suspend fun set(value: String) { this@content.text = value }
        }
    }

abstract class EquatableByRef(val key: String, val ref: Any) {
    override fun hashCode(): Int = key.hashCode() + ref.hashCode()
    override fun equals(other: Any?): Boolean = other is EquatableByRef && this.key == other.key && this.ref == other.ref
}