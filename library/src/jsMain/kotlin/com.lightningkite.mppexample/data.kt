package com.lightningkite.mppexample

import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.HTMLElement

actual typealias DimensionRaw = String
actual val Int.px: Dimension
    get() = Dimension("${this}px")

actual val Int.rem: Dimension
    get() = Dimension("${this}rem")

actual val Double.rem: Dimension
    get() = Dimension("${this}rem")

actual inline operator fun Dimension.plus(other: Dimension): Dimension = Dimension("calc(${this.value} + ${other.value})")

actual typealias Font = String

actual val systemDefaultFont: Font get() = "Helvetica"

actual sealed class ImageSource actual constructor()
actual class ImageResource(val relativeUrl: String) : ImageSource()

fun Dimension.toBoxShadow(): String {
    if (value == "0px")
        return "none"
    val offsetX = 0.px.value
    val offsetY = value
    val blur = 4.px.value
    val spread = 0.px.value
    return "$offsetX $offsetY $blur $spread #77777799"
}
