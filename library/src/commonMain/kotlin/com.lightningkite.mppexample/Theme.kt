package com.lightningkite.mppexample

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
