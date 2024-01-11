package com.lightningkite.rock.views

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.reactiveScope
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIColor
import platform.UIKit.UIView

fun Color.toUiColor(): UIColor = UIColor(
    red = red.toDouble(),
    green = green.toDouble(),
    blue = blue.toDouble(),
    alpha = alpha.toDouble()
)

@OptIn(ExperimentalForeignApi::class)
fun ViewWriter.handleTheme(
    view: NView,
    viewDraws: Boolean = true,
    background: (Theme) -> Unit = {},
    backgroundRemove: () -> Unit = {},
    foreground: (Theme) -> Unit = {}
) {
    val transition = transitionNextView
    transitionNextView = ViewWriter.TransitionNextView.No
    val currentTheme = currentTheme
    val isRoot = isRoot
    this.isRoot = false

    var firstTime = true
    inline fun animateAfterFirst(crossinline action: ()->Unit) {
        if(firstTime) {
            firstTime = false
            action()
        } else {
            UIView.animateWithDuration(0.25) { action() }
        }
    }

    view.calculationContext.reactiveScope {
        val theme = currentTheme()

        val viewMarginless = view.extensionMarginless ?: false
        val shouldTransition = when (transition) {
            ViewWriter.TransitionNextView.No -> false
            ViewWriter.TransitionNextView.Yes -> true
            is ViewWriter.TransitionNextView.Maybe -> transition.logic()
        }
        val mightTransition = transition != ViewWriter.TransitionNextView.No
        val useBackground = shouldTransition
        val usePadding = mightTransition && !isRoot
        val useMargins = (viewDraws || mightTransition) && !viewMarginless

        val borders = !(view.extensionMarginless ?: false) && shouldTransition

        if(useMargins) {
            view.extensionMargin = theme.spacing.value
        } else {
            view.extensionMargin = 0.0
        }
        if(usePadding) {
            view.extensionPadding = theme.spacing.value
        } else {
            view.extensionPadding = 0.0
        }

        animateAfterFirst {
            if (useBackground) {
                when (val b = theme.background) {
                    is Color -> {
                        view.layer.apply {
                            backgroundColor = b.toUiColor().CGColor
                            if (borders) {
                                borderWidth = theme.outlineWidth.value
                                borderColor = theme.outline.closestColor().toUiColor().CGColor
                                cornerRadius = listOf(
                                    theme.cornerRadii.topLeft.value,
                                    theme.cornerRadii.topRight.value,
                                    theme.cornerRadii.bottomLeft.value,
                                    theme.cornerRadii.bottomRight.value
                                ).max()
                                shadowColor = UIColor.grayColor.CGColor
                                shadowOpacity = 1f
                                shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                                shadowRadius = theme.elevation.value
                            }
                        }
                    }

                    is LinearGradient -> {
                        view.layer.apply {
                            backgroundColor = b.closestColor().toUiColor().CGColor
                            if (borders) {
                                borderWidth = theme.outlineWidth.value
                                borderColor = theme.outline.closestColor().toUiColor().CGColor
                                cornerRadius = listOf(
                                    theme.cornerRadii.topLeft.value,
                                    theme.cornerRadii.topRight.value,
                                    theme.cornerRadii.bottomLeft.value,
                                    theme.cornerRadii.bottomRight.value
                                ).max()
                                shadowColor = UIColor.grayColor.CGColor
                                shadowOpacity = 1f
                                shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                                shadowRadius = theme.elevation.value
                            }
                        }
                    }

                    is RadialGradient -> {
                        view.layer.apply {
                            backgroundColor = b.closestColor().toUiColor().CGColor
                            if (borders) {
                                borderWidth = theme.outlineWidth.value
                                borderColor = theme.outline.closestColor().toUiColor().CGColor
                                cornerRadius = listOf(
                                    theme.cornerRadii.topLeft.value,
                                    theme.cornerRadii.topRight.value,
                                    theme.cornerRadii.bottomLeft.value,
                                    theme.cornerRadii.bottomRight.value
                                ).max()
                                shadowColor = UIColor.grayColor.CGColor
                                shadowOpacity = 1f
                                shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                                shadowRadius = theme.elevation.value
                            }
                        }
                    }
                }
                background(theme)
            } else {
                view.layer.apply {
                    backgroundColor = null
                    borderWidth = 0.0
                    borderColor = null
                    cornerRadius = 0.0
                    shadowColor = null
                    shadowOpacity = 0f
                    shadowOffset = CGSizeMake(0.0, 0.0)
                    shadowRadius = 0.0
                }
                backgroundRemove()
            }
            foreground(theme)
        }
    }
}
