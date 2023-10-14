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
    ) = Theme(
        id = id,
        foreground = foreground,
        background = background,
        important = {
            copy(
                id = "$id-important",
                foreground = primaryForeground,
                background = primary,
                important = {
                    copy(
                        id = "$id-critical",
                        foreground = secondaryForeground,
                        background = secondary,
                    )
                }
            )
        },
        critical = {
            copy(
                id = "$id-critical",
                foreground = secondaryForeground,
                background = secondary,
            )
        },
    )

    fun randomLight(): Theme {
        val hue = Random.nextFloat().turns
        return MaterialLikeTheme(
            id = "material-${Random.nextInt(100000)}",
            primary = HSVColor(hue = hue, saturation = 0.5f, value = 0.5f).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 0.5f, value = 0.5f).toRGB(),
        )
    }

    fun randomDark(): Theme {
        val hue = Random.nextFloat().turns
        return MaterialLikeTheme(
            id = "material-${Random.nextInt(100000)}",
            foreground = Color.white,
            background = Color.gray(0.1f),
            primary = HSVColor(hue = hue, saturation = 0.5f, value = 0.5f).toRGB(),
            secondary = HSVColor(hue = hue + Angle.halfTurn, saturation = 0.5f, value = 0.5f).toRGB(),
        )
    }

    fun random(): Theme = if (Random.nextBoolean()) randomLight() else randomDark()
}