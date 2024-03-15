@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.lightningkite.rock.views.direct

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.*
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.widget.*
import androidx.core.view.setMargins
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlin.math.max
import kotlin.math.min
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


fun View.addLayoutChangeListener(listener: () -> Unit): () -> Unit {
    val l = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> listener() }
    this.addOnLayoutChangeListener(l)
    return { this.removeOnLayoutChangeListener(l) }
}

fun View.setPaddingAll(padding: Int) = setPadding(padding, padding, padding, padding)
fun View.setMarginAll(margin: Int) {
    (lparams as? MarginLayoutParams)?.setMargins(margin)
}

fun RockPaint.colorInt(): Int = closestColor().toInt()

val applyTextColorFromThemeHeader: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
    textView.setTypeface(
        theme.title.font, when {
            !theme.title.bold && !theme.title.italic -> Typeface.NORMAL
            !theme.title.bold && theme.title.italic -> Typeface.ITALIC
            theme.title.bold && !theme.title.italic -> Typeface.BOLD
            theme.title.bold && theme.title.italic -> Typeface.BOLD_ITALIC
            else -> Typeface.NORMAL
        }
    )
    textView.isAllCaps = theme.title.allCaps
}
val applyTextColorFromTheme: (Theme, AndroidTextView) -> Unit = { theme, textView ->
    textView.setTextColor(theme.foreground.colorInt())
    textView.setTypeface(
        theme.body.font, when {
            !theme.body.bold && !theme.body.italic -> Typeface.NORMAL
            !theme.body.bold && theme.body.italic -> Typeface.ITALIC
            theme.body.bold && !theme.body.italic -> Typeface.BOLD
            theme.body.bold && theme.body.italic -> Typeface.BOLD_ITALIC
            else -> Typeface.NORMAL
        }
    )
    textView.isAllCaps = theme.body.allCaps
}

inline fun <T : NView> ViewWriter.handleTheme(
    view: T,
    viewDraws: Boolean = true,
    viewLoads: Boolean = false,
    platformDrawable: Drawable? = null,
    noinline applyThemeToPlatformDrawable: Drawable.(Theme) -> Unit = {},
    crossinline background: (Theme) -> Unit = {},
    crossinline backgroundRemove: () -> Unit = {},
    crossinline foreground: (Theme, T) -> Unit = { _, _ -> },
) {
    val transition = transitionNextView
    transitionNextView = ViewWriter.TransitionNextView.No
    val currentTheme = currentTheme
    val parentTheme = lastTheme
    val isRoot = isRoot
    this.isRoot = false
    val changedThemes = changedThemes
    this.changedThemes = false
    var animator: ValueAnimator? = null
    val parentIsSwap = includePaddingAtStackEmpty && stack.size == 1

    view.calculationContext.reactiveScope {
        val theme = currentTheme()

        val viewForcePadding = viewHasPadding[view] ?: false

        val shouldTransition = when (transition) {
            ViewWriter.TransitionNextView.No -> false
            ViewWriter.TransitionNextView.Yes -> true
            is ViewWriter.TransitionNextView.Maybe -> transition.logic()
        }
        val mightTransition = transition != ViewWriter.TransitionNextView.No
        val useBackground = shouldTransition
        val usePadding = (mightTransition && !isRoot || viewForcePadding || parentIsSwap)

        val parentSpacing = ((view.parent as? HasSpacingMultiplier)?.spacingOverride?.await() ?: theme.spacing).value

        if (viewLoads && view.androidCalculationContext.loading.await()) {

            val backgroundDrawable = theme.backgroundDrawable(parentSpacing, view.isClickable, view.background,
                platformDrawable)
            val animation = ValueAnimator.ofFloat(0f, 1f)

            animation.setDuration(1000)
            animation.repeatMode = ValueAnimator.REVERSE
            animation.repeatCount = Animation.INFINITE

            val formDrawable = backgroundDrawable.getDrawable(0) as GradientDrawable

            val originalColors = formDrawable.colors?.map { Color.fromInt(it) } ?: listOf()
            val currentColors = originalColors.map { it.toInt() }.toIntArray()
            animation.addUpdateListener { it: ValueAnimator ->
                for (index in originalColors.indices) currentColors[index] =
                    originalColors[index].highlight(it.animatedFraction * 0.1f + 0.05f).toInt()
                formDrawable.colors = currentColors
            }

            animation.start()
            animator = animation
            view.background = backgroundDrawable
            view.elevation = if (useBackground && parentSpacing > 0f) theme.elevation.value else 0f
            if (useBackground) {
                background(theme)
            } else {
                backgroundRemove()
            }
        } else {
            animator?.removeAllListeners()
            animator?.cancel()
            animator = null
            if (useBackground) {
                val backgroundDrawable = theme.backgroundDrawable(parentSpacing, view.isClickable, view.background,
                    platformDrawable)
                view.background = backgroundDrawable
                view.elevation = if (parentSpacing > 0f) theme.elevation.value else 0f
                background(theme)
            } else if (view.isClickable) {
                view.elevation = 0f
                view.background = theme.rippleDrawableOnly(parentSpacing, view.background, platformDrawable)
                backgroundRemove()
            } else if (view is TransitionImageView) {
                view.elevation = 0f
                view.background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    val cr = when(val it = theme.cornerRadii) {
                        is CornerRadii.Constant -> min(parentSpacing, it.value.value)
                        is CornerRadii.RatioOfSpacing -> it.value * parentSpacing
                    }
                    cornerRadii = floatArrayOf(cr, cr, cr, cr, cr, cr, cr, cr)
                    colors = intArrayOf(theme.background.applyAlpha(0.01f).colorInt(), theme.background.applyAlpha(0.01f).colorInt())
                }
                backgroundRemove()
            } else {
                view.elevation = 0f
                view.background = null
                backgroundRemove()
            }
        }

        val platformDrawableSuggestedPadding = Rect()
        platformDrawable?.getPadding(platformDrawableSuggestedPadding)
        val rockThemePadding =
            if (usePadding) ((view as? HasSpacingMultiplier)?.spacingOverride?.await() ?: theme.spacing).value.toInt()
            else 0

        val paddingLeft = max(rockThemePadding, platformDrawableSuggestedPadding.left)
        val paddingRight = max(rockThemePadding, platformDrawableSuggestedPadding.right)
        val paddingTop = max(rockThemePadding, platformDrawableSuggestedPadding.top)
        val paddingBottom = max(rockThemePadding, platformDrawableSuggestedPadding.bottom)

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

        platformDrawable?.let {
            val layeredBackground = view.background as LayerDrawable
            // Potentially unclear: whenever platformDrawable is not null, it is the second of two layers in the background
            assert(layeredBackground.numberOfLayers == 2)
            layeredBackground.setLayerInsetStart(1, paddingLeft - platformDrawableSuggestedPadding.left)
            layeredBackground.setLayerInsetEnd(1, paddingRight - platformDrawableSuggestedPadding.right)
            layeredBackground.setLayerInsetTop(1, paddingTop - platformDrawableSuggestedPadding.top)
            layeredBackground.setLayerInsetBottom(1, paddingBottom - platformDrawableSuggestedPadding.bottom)
        }

        platformDrawable?.let { it.applyThemeToPlatformDrawable(theme) }
        foreground(theme, view)
    }
}

fun Theme.rippleDrawableOnly(
    parentSpacing: Float,
    existingBackground: Drawable? = null,
    platformDrawable: Drawable? = null,
): LayerDrawable {
    val rippleColor = ColorStateList.valueOf(hover().background.colorInt())
    val preparing = (existingBackground as? RippleDrawable)?.apply {
        setColor(rippleColor)
    } ?: RippleDrawable(rippleColor, null, null).apply {
        addLayer(null)
        platformDrawable?.let(::addLayer)
    }
    preparing.setDrawable(0, GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        val cr = when(val it = this@rippleDrawableOnly.cornerRadii) {
            is CornerRadii.Constant -> min(parentSpacing, it.value.value)
            is CornerRadii.RatioOfSpacing -> it.value * parentSpacing
        }
        cornerRadii = floatArrayOf(cr, cr, cr, cr, cr, cr, cr, cr)
        colors = intArrayOf(background.applyAlpha(0.01f).colorInt(), background.applyAlpha(0.01f).colorInt())
    })
    return preparing
}

fun Theme.backgroundDrawable(
    parentSpacing: Float,
    clickable: Boolean = false,
    existingBackground: Drawable? = null,
    platformDrawable: Drawable? = null,
): LayerDrawable {
    val formDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        val cr = when(val it = this@backgroundDrawable.cornerRadii) {
            is CornerRadii.Constant -> min(parentSpacing, it.value.value)
            is CornerRadii.RatioOfSpacing -> it.value * parentSpacing
        }
        cornerRadii = floatArrayOf(cr, cr, cr, cr, cr, cr, cr, cr)
        setStroke(outlineWidth.value.toInt(), outline.colorInt())

        when (this@backgroundDrawable.background) {
            is Color -> {
                val oldColor: Int? =
                    ((existingBackground as? LayerDrawable)?.getDrawable(0) as? GradientDrawable)?.colors?.get(0)
                val newColor = this@backgroundDrawable.background.colorInt()

                if (oldColor != null && animationsEnabled) {
                    // Run the animation from old colors to new colors
                    val animator: ValueAnimator = ValueAnimator.ofArgb(oldColor, newColor).apply {
                        repeatMode = ValueAnimator.RESTART
                        repeatCount = 0
                        duration = 300
                    }

                    animator.addUpdateListener {
                        val intermediateColor = it.animatedValue as Int
                        colors = intArrayOf(intermediateColor, intermediateColor)
                    }
                    animator.start()
                } else {
                    // Set new colors immediately
                    colors = intArrayOf(newColor, newColor)
                }
            }

            is LinearGradient -> {
                colors = background.stops.map { it.color.toInt() }.toIntArray()
                orientation = when ((background.angle angleTo Angle.zero).turns.times(8).roundToInt()) {
                    -3 -> GradientDrawable.Orientation.TR_BL
                    -2 -> GradientDrawable.Orientation.TOP_BOTTOM
                    -1 -> GradientDrawable.Orientation.TL_BR
                    0 -> GradientDrawable.Orientation.LEFT_RIGHT
                    1 -> GradientDrawable.Orientation.BL_TR
                    2 -> GradientDrawable.Orientation.BOTTOM_TOP
                    3 -> GradientDrawable.Orientation.BR_TL
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

    return if (clickable) {
        val rippleColor = ColorStateList.valueOf(hover().background.colorInt())

        // If we can reuse the existing RippleDrawable, do it to preserve any pending ripples
        // Problem: if the color is set mid-animation, then it is not applied until the next animation
        (existingBackground as? RippleDrawable)?.apply {
            setColor(rippleColor)
        } ?: RippleDrawable(rippleColor, null, null).apply {
            addLayer(null)
            platformDrawable?.let(::addLayer)
        }
    } else {
        LayerDrawable(arrayOfNulls(1)).apply { platformDrawable?.let(::addLayer) }
    }.apply { setDrawable(0, formDrawable) }
}

inline fun <T : View> ViewWriter.handleThemeControl(
    view: T,
    viewLoads: Boolean = false,
    noinline checked: suspend () -> Boolean = { false },
    platformDrawable: Drawable? = null,
    noinline applyThemeToPlatformDrawable: Drawable.(Theme) -> Unit = {},
    crossinline background: (Theme) -> Unit = {},
    crossinline backgroundRemove: () -> Unit = {},
    crossinline foreground: (Theme, T) -> Unit = { _, _ -> },
    setup: () -> Unit
) {
    val hovered = view.hovered
    withThemeGetter({
        if (checked()) return@withThemeGetter it().selected()
        val isHovered = hovered.await()
        when {
            // TODO: State control
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
        handleTheme(view, false, viewLoads, platformDrawable, applyThemeToPlatformDrawable, background,
            backgroundRemove, foreground)
        setup()
    }
}


