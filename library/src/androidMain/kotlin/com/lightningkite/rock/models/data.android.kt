package com.lightningkite.rock.models

actual class ImageResource : ImageSource()
actual class Font

actual val systemDefaultFont: Font
    get() = TODO("Not yet implemented")

actual sealed class ImageSource actual constructor()
actual class DimensionRaw

actual val Int.px: Dimension
    get() = TODO("NOT YET IMPLEMENTED")
actual val Int.rem: Dimension
    get() = TODO("NOT YET IMPLEMENTED")
actual val Double.rem: Dimension
    get() = TODO("NOT YET IMPLEMENTED")

actual inline operator fun Dimension.plus(other: Dimension): Dimension = TODO("NOT YET IMPLEMENTED")
actual inline operator fun Dimension.minus(other: Dimension): Dimension = TODO("NOT YET IMPLEMENTED")
actual inline operator fun Dimension.times(other: Float): Dimension = TODO("NOT YET IMPLEMENTED")
actual inline operator fun Dimension.div(other: Float): Dimension = TODO("NOT YET IMPLEMENTED")