@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock.views.direct

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.core.view.setMargins
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlin.collections.set
import kotlin.math.roundToInt
import android.widget.TextView as AndroidTextView
import com.lightningkite.rock.models.Paint as RockPaint


val NView.selected: Writable<Boolean>
    get() = object : Writable<Boolean> {
        override fun addListener(listener: () -> Unit): () -> Unit {
            return addListener(View::setOnClickListener, { View.OnClickListener { it() } }, listener)
        }

        override suspend fun awaitRaw(): Boolean {
            return this@selected.isSelected
        }

        override suspend fun set(value: Boolean) {
            this@selected.isSelected = value
        }
    }

val NView.hovered: Readable<Boolean>
    get() = object : Readable<Boolean> {
        override fun addListener(listener: () -> Unit): () -> Unit {
            return addListener(View::setOnHoverListener, { View.OnHoverListener { _, _ -> it(); true } }, listener)
        }

        override suspend fun awaitRaw(): Boolean {
            return this@hovered.isHovered
        }
    }


//actual val Select.selected: Writable<String?>
//    get() {
//        return this@selected.native.stringNullableWritable(
//            addNativeListener = {
//                this@selected.native.onItemSelectedListener = object : OnItemSelectedListener {
//                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                        NativeListeners.listeners.get(this@selected.native)?.forEach { action -> action() }
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {}
//                }
//            },
//            getString = { this@selected.native.selectedItem.toString() },
//            setString = { value ->
//                val adapter = this@selected.native.adapter
//                val count = adapter.count
//                var counter = 0
//                while (counter < count) {
//                    val item = adapter.getItem(counter).toString()
//                    if (item == value) {
//                        break
//                    }
//                    counter++
//                }
//
//                if (counter < count) {
//                    this@selected.native.setSelection(counter)
//                }
//            }
//        )
//    }
//actual var Select.options: List<WidgetOption>
//    get() {
//        val adapter = native.adapter
//        val options = mutableListOf<WidgetOption>()
//        val count = adapter.count
//        var counter = 0
//        while (counter < count) {
//            adapter.getItem(counter)?.let { options.add(it as WidgetOption) }
//            counter++
//        }
//        return options
//    }
//    set(value) {
//        native.adapter = ArrayAdapter(native.context, android.R.layout.simple_list_item_1, value)
//    }


fun View.addLayoutChangeListener(listener: () -> Unit): () -> Unit {
    val l = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> listener() }
    this.addOnLayoutChangeListener(l)
    return { this.removeOnLayoutChangeListener(l) }
}

fun View.setPaddingAll(padding: Int) = setPadding(padding, padding, padding, padding)
fun View.setMarginAll(margin: Int) {
    (lparams as? MarginLayoutParams)?.setMargins(margin)
}

internal fun RockPaint.colorInt(): Int = closestColor().toInt()

val applyTextColorFromThemeHeader: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
    textView.setTypeface(theme.title.font, when {
        !theme.title.bold && !theme.title.italic -> Typeface.NORMAL
        !theme.title.bold && theme.title.italic -> Typeface.ITALIC
        theme.title.bold && !theme.title.italic -> Typeface.BOLD
        theme.title.bold && theme.title.italic -> Typeface.BOLD_ITALIC
        else -> Typeface.NORMAL
    })
    textView.isAllCaps = theme.title.allCaps
}
val applyTextColorFromTheme: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
    textView.setTypeface(theme.body.font, when {
        !theme.body.bold && !theme.body.italic -> Typeface.NORMAL
        !theme.body.bold && theme.body.italic -> Typeface.ITALIC
        theme.body.bold && !theme.body.italic -> Typeface.BOLD
        theme.body.bold && theme.body.italic -> Typeface.BOLD_ITALIC
        else -> Typeface.NORMAL
    })
    textView.isAllCaps = theme.body.allCaps
}

inline fun <T: NView> ViewWriter.handleTheme(
    view: T,
    viewDraws: Boolean = true,
    crossinline background: (Theme) -> Unit = {},
    crossinline backgroundRemove: () -> Unit = {},
    crossinline foreground: (Theme, T) -> Unit = { _, _  -> },
) {
    val transition = transitionNextView
    transitionNextView = ViewWriter.TransitionNextView.No
    val currentTheme = currentTheme
    val isRoot = isRoot
    this.isRoot = false

    view.calculationContext.reactiveScope {
        val theme = currentTheme()

        val viewMarginless = viewIsMarginless[view] ?: false

        val shouldTransition = when (transition) {
            ViewWriter.TransitionNextView.No -> false
            ViewWriter.TransitionNextView.Yes -> true
            is ViewWriter.TransitionNextView.Maybe -> transition.logic()
        }
        val mightTransition = transition != ViewWriter.TransitionNextView.No
        val useBackground = shouldTransition
        val usePadding = mightTransition && !isRoot
        val useMargins = (viewDraws || mightTransition) && !viewMarginless

        val borders = !viewMarginless

        if (useMargins) {
            view.setMarginAll(theme.spacing.value.toInt())
        } else {
            view.setMarginAll(0)
        }
        if (usePadding) {
            view.setPaddingAll(theme.spacing.value.toInt())
        } else {
            view.setPaddingAll(0)
        }

        if (useBackground) {
            val gradientDrawable = theme.backgroundDrawable(borders)
            view.background = gradientDrawable
            view.elevation = if (borders) theme.elevation.value else 0f
            background(theme)
        } else {
            view.background = null
            backgroundRemove()
        }
        foreground(theme, view)
    }
}

fun Theme.backgroundDrawable(
    borders: Boolean
): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        if (borders) {
            cornerRadii = floatArrayOf(
                this@backgroundDrawable.cornerRadii.topLeft.value,
                this@backgroundDrawable.cornerRadii.topLeft.value,
                this@backgroundDrawable.cornerRadii.topRight.value,
                this@backgroundDrawable.cornerRadii.topRight.value,
                this@backgroundDrawable.cornerRadii.bottomLeft.value,
                this@backgroundDrawable.cornerRadii.bottomLeft.value,
                this@backgroundDrawable.cornerRadii.bottomRight.value,
                this@backgroundDrawable.cornerRadii.bottomRight.value
            )
            setStroke(outlineWidth.value.toInt(), outline.colorInt())
        }

        when (this@backgroundDrawable.background) {
            is Color -> {
                colors = intArrayOf(this@backgroundDrawable.background.colorInt(), this@backgroundDrawable.background.colorInt())
            }

            is LinearGradient -> {
                colors = background.stops.map { it.color.toInt() }.toIntArray()
                orientation = when ((background.angle angleTo Angle.zero).turns.times(8).roundToInt()) {
                    -3 -> GradientDrawable.Orientation.BL_TR
                    -2 -> GradientDrawable.Orientation.BOTTOM_TOP
                    -1 -> GradientDrawable.Orientation.BR_TL
                    0 -> GradientDrawable.Orientation.LEFT_RIGHT
                    1 -> GradientDrawable.Orientation.TL_BR
                    2 -> GradientDrawable.Orientation.TOP_BOTTOM
                    3 -> GradientDrawable.Orientation.TR_BL
                    else -> GradientDrawable.Orientation.LEFT_RIGHT
                }
                gradientType = GradientDrawable.LINEAR_GRADIENT
            }

            is RadialGradient -> {
                colors = background.stops.map { it.color.toInt() }.toIntArray()
                gradientType = GradientDrawable.RADIAL_GRADIENT
            }
        }
    }
}

inline fun <T: View> ViewWriter.handleThemeControl(
    view: T,
    noinline checked: suspend () -> Boolean = { false },
    crossinline background: (Theme) -> Unit = {},
    crossinline backgroundRemove: () -> Unit = {},
    crossinline foreground: (Theme, T) -> Unit = { _, _  -> },
    setup: () -> Unit
) {
    val hovered = view.hovered
    withThemeGetter({
        if (checked()) return@withThemeGetter it().selected()
        val isHovered = hovered.await()
        when {
//            state and UIControlStateDisabled != 0UL -> it().disabled()
//            state and UIControlStateHighlighted != 0UL -> it().down()
            isHovered -> it().hover()
            else -> it()
        }
    }) {
        if (transitionNextView == ViewWriter.TransitionNextView.No) {
            transitionNextView = ViewWriter.TransitionNextView.Maybe {
                if (checked()) return@Maybe true
                val isHovered = hovered.await()
                when {
//                    state and UIControlStateDisabled != 0UL -> true
//                    state and UIControlStateHighlighted != 0UL -> true
                    isHovered -> true
                    else -> false
                }
            }
        }
        handleTheme(view, false, background, backgroundRemove, foreground)
        setup()
    }
}


