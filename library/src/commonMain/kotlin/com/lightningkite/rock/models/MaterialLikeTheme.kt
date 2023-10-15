package com.lightningkite.rock.models

import kotlin.random.Random

object MaterialLikeTheme {
    operator fun invoke(
        id: String = "material",
        foreground: Paint = Color.black,
        background: Paint = Color.white,
        primary: Color = Color.fromHex(0xFF6200EE.toInt()),
        secondary: Color = Color.fromHex(0xFF03DAC6.toInt()),
        primaryForeground: Color = if (primary.average < 0.5f) Color.white else Color.black,
        secondaryForeground: Color = if (secondary.average < 0.5f) Color.white else Color.black,
        title: FontAndStyle = FontAndStyle(systemDefaultFont),
        body: FontAndStyle = FontAndStyle(systemDefaultFont),
        elevation: Dimension = 2.px,
        cornerRadii: CornerRadii = CornerRadii(8.px),
        spacing: Dimension = 8.px,
        outline: Paint = background.closestColor().highlight(0.1f),
        outlineWidth: Dimension = 0.px,
    ) = Theme(
        id = id,
        title = title,
        body = body,
        elevation = elevation,
        cornerRadii = cornerRadii,
        spacing = spacing,
        outline = outline,
        outlineWidth = outlineWidth,
        foreground = foreground,
        background = background,
        important = {
            copy(
                id = "$id-important",
                foreground = primaryForeground,
                background = primary,
                outline = primary.highlight(0.1f),
                important = {
                    copy(
                        id = "$id-critical",
                        foreground = secondaryForeground,
                        background = secondary,
                        outline = secondary.highlight(0.1f),
                    )
                }
            )
        },
        critical = {
            copy(
                id = "$id-critical",
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
        return MaterialLikeTheme(
            id = "material-${Random.nextInt(100000)}",
            primary = HSVColor(hue = hue, saturation = saturation, value = value).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 1f - saturation, value = 1f - value).toRGB(),
            outlineWidth = if(Random.nextBoolean()) 2.px else 0.px
        )
    }

    fun randomDark(): Theme {
        val hue = Random.nextFloat().turns
        val saturation = Random.nextFloat() * 0.5f + 0.25f
        val value = Random.nextFloat() * 0.5f + 0.25f
        return MaterialLikeTheme(
            id = "material-${Random.nextInt(100000)}",
            foreground = Color.white,
            background = Color.gray(0.2f),
            primary = HSVColor(hue = hue, saturation = saturation, value = value).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 1f - saturation, value = 1f - value).toRGB(),
            outlineWidth = if(Random.nextBoolean()) 2.px else 0.px
        )
    }

    fun random(): Theme = if (Random.nextBoolean()) randomLight() else randomDark()
}

fun Theme.randomTitleFontSettings() = copy(title = title.copy(font = systemDefaultFont, bold = Random.nextBoolean(), allCaps = Random.nextBoolean()))
fun Theme.randomElevationAndCorners() = copy(
    elevation = Random.nextInt(4).px,
    cornerRadii = CornerRadii(Random.nextInt(32).px)
)