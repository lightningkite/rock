package com.lightningkite.rock.models

import kotlin.random.Random

data class Theme(
    val id: String,
    val title: FontAndStyle = FontAndStyle(systemDefaultFont),
    val body: FontAndStyle = FontAndStyle(systemDefaultFont),
    val elevation: Dimension = 2.px,
    val cornerRadii: CornerRadii = CornerRadii(8.px),
    val spacing: Dimension = 8.px,
    val foreground: Paint = Color.black,
    val outline: Paint = Color.black,
    val outlineWidth: Dimension = 0.px,
    val background: Paint = Color.white,
    val hover: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-hover",
            background = this.background.closestColor().highlight(0.2f),
            elevation = this.elevation * 2f,
        )
    },
    val down: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-down",
            background = this.background.closestColor().highlight(0.3f),
            elevation = this.elevation / 2f,
        )
    },
    val selected: (Theme.() -> Theme) = { this.important(this) },
    val disabled: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-disabled",
            foreground = this.foreground.closestColor().copy(alpha = 0.5f)
        )
    },
    val important: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-important",
            foreground = this.background,
            background = this.foreground
        )
    },
    val critical: (Theme.() -> Theme) = { this.important(this).let { it.important(it) } },
    val warning: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-warning",
            background = Color.fromHex(0xFFcce08d.toInt()),
            foreground = Color.black
        )
    },
    val danger: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-danger",
            background = Color.fromHex(0xFFB00020.toInt()),
            foreground = Color.white
        )
    },
) {
    @JsName("hoverDirect") fun hover() = hover(this)
    @JsName("downDirect") fun down() = down(this)
    @JsName("selectedDirect") fun selected() = selected(this)
    @JsName("disabledDirect") fun disabled() = disabled(this)
    @JsName("importantDirect") fun important() = important(this)
    @JsName("criticalDirect") fun critical() = critical(this)
    @JsName("warningDirect") fun warning() = warning(this)
    @JsName("dangerDirect") fun danger() = danger(this)
}

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
