package com.lightningkite.rock.models

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
            background = this.foreground,
            outline = this.foreground.closestColor().highlight(1f)
        )
    },
    val critical: (Theme.() -> Theme) = { this.important(this).let { it.important(it) } },
    val warning: (Theme.() -> Theme) = {
        copy(
            id = "${this.id}-warning",
            background = Color.fromHex(0xFFd4cb79.toInt()),
            outline = Color.fromHex(0xFFd4cb79.toInt()).highlight(0.1f),
            foreground = Color.black
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

