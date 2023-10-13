package com.lightningkite.rock.models

interface Theme {
    val id: String

    val title: FontAndStyle get() = FontAndStyle(systemDefaultFont)
    val body: FontAndStyle get() = FontAndStyle(systemDefaultFont)
    val elevation: Dimension get() = 2.px
    val cornerRadii: CornerRadii get() = CornerRadii(8.px)
    val spacing: Dimension get() = 8.px
    val foreground: Paint get() = Color.black
    val outline: Paint get() = Color.black
    val outlineWidth: Dimension get() = 0.px
    val background: Paint get() = Color.white

    fun hover(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-hover"
        override val background: Paint get() = this@Theme.background.closestColor().highlight(0.2f)
    }
    fun selected(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-selected"
        override val background: Paint get() = this@Theme.background.closestColor().darken(0.2f)
    }
    fun disabled(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-disabled"
        override val foreground: Paint get() = this@Theme.foreground.closestColor().copy(alpha = 0.5f)
    }

    fun important(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-important"
        override val foreground: Paint get() = this@Theme.background
        override val background: Paint get() = this@Theme.foreground
    }
    fun critical(): Theme = important().important()
    fun warning(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-warning"
        override val background: Paint get() = Color.yellow
        override val foreground: Paint get() = Color.black
    }
    fun danger(): Theme = object: Theme by this {
        override val id = this@Theme.id + "-danger"
        override val background: Paint get() = Color.red
        override val foreground: Paint get() = Color.white
    }
}
