package com.lightningkite.mppexample

expect class Font

expect val systemDefaultFont: Font

expect sealed class ImageSource()
data class ImageVector(val paths: List<Path>) : ImageSource() {
    data class Path(val fillColor: Paint, val strokeColor: Color, val strokeWidth: Dimension, val path: String)
}

data class ImageRemote(val url: String) : ImageSource()
class ImageRaw(val data: ByteArray) : ImageSource()
expect class ImageResource : ImageSource

data class SizeConstraints(
    val minWidth: Dimension? = null,
    val maxWidth: Dimension? = null,
    val minHeight: Dimension? = null,
    val maxHeight: Dimension? = null,
    val width: Dimension? = null,
    val height: Dimension? = null,
)

data class Insets(
    val left: Dimension? = null,
    val top: Dimension? = null,
    val right: Dimension? = null,
    val bottom: Dimension? = null
) {
    constructor(all: Dimension) : this(all, all, all, all)

    companion object {
        fun zero() = Insets(0.px)
    }
}

data class TextStyle(
    val color: Color = Color.black,
    val size: Double = 14.0,
    val font: Font = systemDefaultFont,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val allCaps: Boolean = false,
    val lineSpacingMultiplier: Double = 1.0,
    val letterSpacing: Dimension = 0.px,
)

enum class TextGravity {
    Left, Center, Right
}

data class KeyboardHints(
    val case: KeyboardCase,
    val type: KeyboardType,
    val autocomplete: AutoComplete? = null,
    val action: Action? = null
) {
    companion object {
        val paragraph = KeyboardHints(KeyboardCase.Sentences, KeyboardType.Text)
        val title = KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
        val id = KeyboardHints(KeyboardCase.Letters, KeyboardType.Text)
        val integer = KeyboardHints(KeyboardCase.None, KeyboardType.Integer)
        val decimal = KeyboardHints(KeyboardCase.None, KeyboardType.Decimal)
        val phone = KeyboardHints(KeyboardCase.None, KeyboardType.Phone)
        val password = KeyboardHints(KeyboardCase.None, KeyboardType.Text)
    }
}

enum class AutoComplete { Email, Password, NewPassword, Phone }
enum class KeyboardCase { None, Letters, Words, Sentences }
enum class KeyboardType { Text, Integer, Phone, Decimal }
data class Action(
    val title: String,
    val icon: ImageSource,
    val onSelect: () -> Unit
)

enum class ImageMode { Fit, Crop, Stretch, NoScale }
data class Tab(
    val title: String,
    val icon: ImageSource,
    val onSelect: () -> Unit,
    val onReselect: () -> Unit = onSelect,
)

expect class DimensionRaw
value class Dimension(val value: DimensionRaw)

expect val Int.px: Dimension
expect val Int.rem: Dimension
expect inline operator fun Dimension.plus(other: Dimension): Dimension
