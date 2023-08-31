package com.lightningkite.mppexample

data class PaintPair(val foreground: Paint, val background: Paint)

data class Theme(
    val titleFont: Font,
    val bodyFont: Font,
    val baseSize: Double,
    val normal: PaintPair,
    val primary: PaintPair,
    val accent: PaintPair,
    val allCaps: Boolean = false
) {
    fun primaryTheme() = copy(
        normal = primary, primary = accent, accent = accent, allCaps = true
    )
}

val ViewContext.themeStack by viewContextAddon(arrayListOf<Theme>())
val ViewContext.theme get() = themeStack.lastOrNull() ?: throw IllegalStateException("No theme set")
inline fun ViewContext.withTheme(theme: Theme, action: () -> Unit) {
    themeStack.add(theme)
    try {
        action()
    } finally {
        themeStack.removeLast()
    }
}

inline fun ViewContext.themedText(crossinline setup: Text.() -> Unit) {
    text {
        textStyle = TextStyle(
            size = theme.baseSize,
            color = theme.normal.foreground.closestColor(),
            font = theme.bodyFont,
            allCaps = theme.allCaps
        )
        setup()
    }
}

//class Table: NView()
//fun ViewContext.table(setup: Table.()->Unit): Unit {
//
//}
//fun <T> Table.bind(
//    rows: Readable<List<T>>,
//    columns: List<String>,
//    renderColumn: (String, T) ->
//) {
//
//}

//class NiceButton: NView()
//
//var NiceButton.text: String
//    get() = throw NotImplementedError()
//    set(value) {
//        TODO()
//    }

fun ViewContext.buttonLike(color: Color): ViewWrapper {
    val hover = color.darken(0.15f)
    val focus = color.darken(0.3f)
    withBackground(
        Background(
            fill = color,
//            fill = color.toGradient(),
            stroke = hover,
            strokeWidth = 2.px,
            corners = CornerRadii(12.px),
        )
    )
    hoverable(
        elevation = 2.px,
        background = Background(
            fill = hover//.toGradient()
        )
    )
    focusable(
        elevation = 4.px,
        background = Background(
            fill = focus//.toGradient()
        )
    )
    return ViewWrapper
}

fun ViewContext.niceButton(
    buttonSetup: Button.() -> Unit,
) {
    button {
        withTheme(theme.primaryTheme()) {
            buttonSetup()
        }
    } in padding(
        Insets.symmetric(
            horizontal = 12.px,
            vertical = 8.px
        )
    ) in buttonLike(theme.primary.background.closestColor())
}
