package com.lightningkite.rock.models

data class Theme(
    val id: String,
    val title: FontAndStyle = FontAndStyle(systemDefaultFont),
    val body: FontAndStyle = FontAndStyle(systemDefaultFont),
    val elevation: Dimension = 2.px,
    val cornerRadii: CornerRadii = CornerRadii(8.px),
    val spacing: Dimension = 0.5.rem,
    val foreground: Paint = Color.black,
    val outline: Paint = Color.black,
    val outlineWidth: Dimension = 0.px,
    val background: Paint = Color.white,
    val noMarginOnSwitch: Boolean = false,
    val hover: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-hover",
            background = this.background.closestColor().highlight(0.2f),
            outline = this.background.closestColor().highlight(0.2f).highlight(0.1f),
            elevation = this.elevation * 2f,
        )
    },
    val down: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-down",
            background = this.background.closestColor().highlight(0.3f),
            outline = this.background.closestColor().highlight(0.3f).highlight(0.1f),
            elevation = this.elevation / 2f,
        )
    },
    val unselected: (Theme.() -> Theme) = { this.important(this).let { it.copy(id = it.id + "-outline", background = Color.transparent, outline = it.background, outlineWidth = 4.px, foreground = it.background)} },
    val selected: (Theme.() -> Theme) = { this.important(this) },
    val disabled: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-disabled",
            foreground = this.foreground.closestColor().copy(alpha = 0.5f)
        )
    },
    val bar: (Theme.() -> Theme?) = {
        copy(
            id = "${this.id}-bar",
            foreground = this.background,
            background = this.foreground,
            outline = this.foreground.closestColor().highlight(1f)
        )
    },
    val important: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-important",
            foreground = this.background,
            background = this.foreground,
            outline = this.foreground.closestColor().highlight(1f)
        )
    },
    val critical: (Theme.() -> Theme) = { this.important(this).let { it.important(it) } },
    val warning: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-warning",
            background = Color.fromHex(0xFFe36e24.toInt()),
            outline = Color.fromHex(0xFFe36e24.toInt()).highlight(0.1f),
            foreground = Color.white
        )
    },
    val danger: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-danger",
            background = Color.fromHex(0xFFB00020.toInt()),
            outline = Color.fromHex(0xFFB00020.toInt()).highlight(0.1f),
            foreground = Color.white
        )
    },
    val affirmative: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-affirmitive",
            background = Color.fromHex(0xFF20a020.toInt()),
            outline = Color.fromHex(0xFF20a020.toInt()).highlight(0.1f),
            foreground = Color.white
        )
    },
) {
    @JsName("hoverDirect") inline fun hover() = hover(this)
    @JsName("downDirect") inline fun down() = down(this)
    @JsName("selectedDirect") inline fun selected() = selected(this)
    @JsName("unselectedDirect") inline fun unselected() = unselected(this)
    @JsName("disabledDirect") inline fun disabled() = disabled(this)
    @JsName("barDirect") inline fun bar() = bar(this)
    @JsName("importantDirect") inline fun important() = important(this)
    @JsName("criticalDirect") inline fun critical() = critical(this)
    @JsName("warningDirect") inline fun warning() = warning(this)
    @JsName("dangerDirect") inline fun danger() = danger(this)
    @JsName("affirmativeDirect") inline fun affirmative() = affirmative(this)
}

