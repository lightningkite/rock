package com.lightningkite.mppexample

expect class Dimension

expect val DimensionZero: Dimension

expect class Font

expect val systemDefaultFont: Font

expect sealed class ImageSource()
data class ImageVector(val paths: List<Path>): ImageSource() {
    data class Path(val fillColor: Paint, val strokeColor: Color, val strokeWidth: Dimension, val path: String)
}
data class ImageRemote(val url: String): ImageSource()
class ImageRaw(val data: ByteArray): ImageSource()
expect class ImageResource: ImageSource

data class Background(
    val fill: Paint? = null,
    val stroke: Color? = null,
    val strokeWidth: Dimension? = null,
    val corners: CornerRadii? = null,
) {
    companion object {
        fun capsule(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
        ) = Background(fill, stroke, strokeWidth, corners = null)
        fun rectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
        ) = Background(fill, stroke, strokeWidth, corners = CornerRadii(DimensionZero))
        fun roundedRectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
            corners: CornerRadii
        ) = Background(fill, stroke, strokeWidth, corners = corners)
        fun roundedRectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
            cornerRadius: Dimension
        ) = Background(fill, stroke, strokeWidth, corners = CornerRadii(cornerRadius))
    }
}

data class CornerRadii(
    val topLeft: Dimension,
    val topRight: Dimension,
    val bottomLeft: Dimension,
    val bottomRight: Dimension
) {
    constructor(all: Dimension) : this(all, all, all, all)
}

data class Insets(val left: Dimension, val top: Dimension, val right: Dimension, val bottom: Dimension) {
    constructor(all: Dimension) : this(all, all, all, all)
}

data class TextStyle(
    val color: Color = Color.black,
    val font: Font,
    val bold: Boolean,
    val italic: Boolean,
    val allCaps: Boolean,
    val lineSpacingMultiplier: Double,
    val letterSpacing: Dimension,
)


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
