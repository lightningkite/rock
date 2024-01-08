@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock.views.direct

import android.graphics.drawable.GradientDrawable
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


fun NView.removeListener(listener: () -> Unit): () -> Unit = {
    val key = this
    NativeListeners.listeners.removeListener(key, listener)
    if (NativeListeners.listeners.get(key)?.isEmpty() == true) {
        NativeListeners.listeners.removeKey(key)
    }
}


val NView.selected: Writable<Boolean>
    get() = object : Writable<Boolean> {
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

val NView.hovered: Readable<Boolean>
    get() = object : Readable<Boolean> {
        override fun addListener(listener: () -> Unit): () -> Unit {
            return {}
//            NativeListeners.listeners.addListener(this@hovered, listener)
//            this@hovered.setOnHoverListener { v, _ ->
//                NativeListeners.listeners.get(v)?.forEach { action -> action() }
//            }
//            return this@hovered.removeListener(listener)
        }

        override suspend fun awaitRaw(): Boolean {
            return this@hovered.isHovered
        }
    }


private object ViewActions {
    val actions: MutableMap<Int, Action> = mutableMapOf()
}

internal val View.getAction: Action? get() = ViewActions.actions[hashCode()]
internal fun View.setAction(action: Action?) {
    if (action != null) {
        ViewActions.actions[hashCode()] = action
    } else {
        ViewActions.actions.remove(hashCode())
    }
}

//Not certain what to do here.  my first thought is to use a tag to hold the range for the time picker
//however unlikely someone could decide to set a custom tag on the native
// view which would then mess up the functionality.


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
    NativeListeners.listeners.addListener(this, listener)
    this.addOnLayoutChangeListener /* listener = */ { _, _, _, _, _, _, _, _, _ -> listener() }
    return removeListener(listener)
}

fun View.setPaddingAll(padding: Int) = setPadding(padding, padding, padding, padding)
fun View.setMarginAll(margin: Int) {
    (lparams as? MarginLayoutParams)?.setMargins(margin)
}

internal fun RockPaint.colorInt(): Int = closestColor().toInt()

val applyTextColorFromTheme: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
}

fun <T: NView> ViewWriter.handleTheme(
    view: T,
    viewDraws: Boolean = true,
    background: (Theme) -> Unit = {},
    backgroundRemove: () -> Unit = {},
    foreground: (Theme, T) -> Unit = { _, _  -> },
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
        val usePadding = (viewDraws || (mightTransition && !isRoot))
        val useMargins = (viewDraws || mightTransition) && !viewMarginless

        val borders = !viewMarginless && shouldTransition

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
            val gradientDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                if (borders) {
                    cornerRadii = floatArrayOf(
                        theme.cornerRadii.topLeft.value,
                        theme.cornerRadii.topLeft.value,
                        theme.cornerRadii.topRight.value,
                        theme.cornerRadii.topRight.value,
                        theme.cornerRadii.bottomLeft.value,
                        theme.cornerRadii.bottomLeft.value,
                        theme.cornerRadii.bottomRight.value,
                        theme.cornerRadii.bottomRight.value
                    )
                    setStroke(theme.outlineWidth.value.toInt(), theme.outline.colorInt())
                }

                when (theme.background) {
                    is Color -> {
                        colors = intArrayOf(theme.background.colorInt(), theme.background.colorInt())
                    }

                    is LinearGradient -> {
                        colors = theme.background.stops.map { it.color.toInt() }.toIntArray()
                        orientation = when((theme.background.angle angleTo Angle.zero).turns.times(8).roundToInt()) {
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
                        colors = theme.background.stops.map { it.color.toInt() }.toIntArray()
                        gradientType = GradientDrawable.RADIAL_GRADIENT
                    }
                }
            }
            view.background = gradientDrawable
            view.elevation = if (borders) 0f else theme.elevation.value
            background(theme)
        } else {
            view.background = null
            backgroundRemove()
        }
        foreground(theme, view)
    }
}


inline fun ViewWriter.handleThemeControl(
    view: View,
    crossinline checked: suspend () -> Boolean = { false },
    setup: () -> Unit
) {
//    view.hi
//    view.setOnHoverListener { view, motionEvent -> }
//    val s = view.stateReadable
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
        handleTheme(view, viewDraws = false)
        setup()
    }
}


var linkCounter = 0;


