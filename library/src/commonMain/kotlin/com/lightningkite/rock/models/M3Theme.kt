package com.lightningkite.rock.models

import kotlin.random.Random

object M3Theme {
    operator fun invoke(
        primary: Color = Color.fromHex(0xFF6200EE.toInt()),
        secondary: Color = Color.fromHex(0xFF03DAC6.toInt()),
        primaryForeground: Color = if (primary.perceivedBrightness < 0.6f) Color.white else Color.black,
        secondaryForeground: Color = if (secondary.perceivedBrightness < 0.6f) Color.white else Color.black,
        foreground: Paint = Color.black,
        backgroundAdjust: Float = 0.1f,
        background: Paint = Color.interpolate(foreground.closestColor().invert(), primary, backgroundAdjust),
        title: FontAndStyle = FontAndStyle(systemDefaultFont),
        body: FontAndStyle = FontAndStyle(systemDefaultFont),
        elevation: Dimension = 2.px,
        cornerRadiusRatio: Float = 1f,
        spacing: Dimension = 0.5.rem,
        outline: Paint = background.closestColor().highlight(0.1f),
        outlineWidth: Dimension = 0.px,
    ) = Theme(
        title = title,
        body = body,
        elevation = elevation,
        cornerRadiusRatio = cornerRadiusRatio,
        defaultSpacing = spacing,
        outline = outline,
        outlineWidth = outlineWidth,
        foreground = foreground,
        background = background,
        hover = {
            copy(
                background = this.background.closestColor().highlight(0.2f),
                outline = this.background.closestColor().highlight(0.2f).highlight(0.1f),
                elevation = this.elevation * 2f,
            )
        },
        down = {
            copy(
                background = this.background.closestColor().highlight(0.3f),
                outline = this.background.closestColor().highlight(0.3f).highlight(0.1f),
                elevation = this.elevation / 2f,
            )
        },
        bar = { null },
        important = {
            copy(
                foreground = primaryForeground,
                background = primary,
                outline = primary.highlight(0.1f),
                important = {
                    copy(
                        foreground = secondaryForeground,
                        background = secondary,
                        outline = secondary.highlight(0.1f),
                    )
                }
            )
        },
        critical = {
            copy(
                foreground = secondaryForeground,
                background = secondary,
                outline = secondary.highlight(0.1f),
            )
        },
    )

    fun randomLight(): Theme {
        val hue = Random.nextFloat().turns
        val saturation = Random.nextFloat() * 0.5f + 0.25f
        val value = Random.nextFloat() * 0.5f + 0.25f
        return this(
            primary = HSVColor(hue = hue, saturation = saturation, value = value).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 1f - saturation, value = 1f - value).toRGB(),
            backgroundAdjust = Random.nextFloat() * 0.15f,
        )
    }

    fun randomDark(): Theme {
        val hue = Random.nextFloat().turns
        val saturation = Random.nextFloat() * 0.5f + 0.25f
        val value = Random.nextFloat() * 0.5f + 0.25f
        return this(
            foreground = Color.white,
            backgroundAdjust = Random.nextFloat() * 0.5f,
            primary = HSVColor(hue = hue, saturation = saturation, value = value).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 1f - saturation, value = 1f - value).toRGB(),
        )
    }

    fun random(): Theme = if (Random.nextBoolean()) randomLight() else randomDark()
}

