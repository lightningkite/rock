package com.lightningkite.rock.views

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.models.RadialGradient
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.reactiveScope
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGSizeMake
import platform.QuartzCore.CALayer
import platform.UIKit.UIColor
import platform.UIKit.UIView
import kotlin.math.max
import kotlin.random.Random

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
    val rootTheme = themeStack.size == 1
    val themeGetter = themeStack.lastOrNull() ?: { null }
    val theme2 = currentTheme
    val themeChanged = themeJustChanged
    themeJustChanged = false

    view.calculationContext.reactiveScope {
        val theme = theme2()
        val useBackground = themeChanged && themeGetter() != null
        if(viewDraws || useBackground && !rootTheme) {
            view.extensionMargin = theme.spacing.value
            view.extensionPadding = theme.spacing.value
        } else {
            view.extensionMargin = 0.0
            view.extensionPadding = 0.0
        }
        if(useBackground) {
            when(val b = theme.background) {
                is Color -> {
                    view.layer.apply {
                        backgroundColor = b.toUiColor().CGColor
                        borderWidth = theme.outlineWidth.value
                        borderColor = theme.outline.closestColor().toUiColor().CGColor
                        cornerRadius = listOf(theme.cornerRadii.topLeft.value, theme.cornerRadii.topRight.value, theme.cornerRadii.bottomLeft.value, theme.cornerRadii.bottomRight.value).max()
                        shadowColor = UIColor.grayColor.CGColor
                        shadowOpacity = 1f
                        shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                        shadowRadius = theme.elevation.value
                    }
                }
                is LinearGradient -> {
                    view.layer.apply {
                        backgroundColor = b.closestColor().toUiColor().CGColor
                        borderWidth = theme.outlineWidth.value
                        borderColor = theme.outline.closestColor().toUiColor().CGColor
                        cornerRadius = listOf(theme.cornerRadii.topLeft.value, theme.cornerRadii.topRight.value, theme.cornerRadii.bottomLeft.value, theme.cornerRadii.bottomRight.value).max()
                        shadowColor = UIColor.grayColor.CGColor
                        shadowOpacity = 1f
                        shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                        shadowRadius = theme.elevation.value
                    }
                }
                is RadialGradient -> {
                    view.layer.apply {
                        backgroundColor = b.closestColor().toUiColor().CGColor
                        borderWidth = theme.outlineWidth.value
                        borderColor = theme.outline.closestColor().toUiColor().CGColor
                        cornerRadius = listOf(theme.cornerRadii.topLeft.value, theme.cornerRadii.topRight.value, theme.cornerRadii.bottomLeft.value, theme.cornerRadii.bottomRight.value).max()
                        shadowColor = UIColor.grayColor.CGColor
                        shadowOpacity = 1f
                        shadowOffset = CGSizeMake(0.0, theme.elevation.value)
                        shadowRadius = theme.elevation.value
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
