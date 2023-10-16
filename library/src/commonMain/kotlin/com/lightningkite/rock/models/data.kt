package com.lightningkite.rock.models

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.Paint
import com.lightningkite.rock.navigation.RockScreen

class AnimationId

expect class Font

expect val systemDefaultFont: Font

data class FontAndStyle(
    val font: Font = systemDefaultFont,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val allCaps: Boolean = false,
    val lineSpacingMultiplier: Double = 1.0,
    val additionalLetterSpacing: Dimension = 0.px,
)

expect sealed class ImageSource()
data class ImageVector(
    val width: Dimension, val height: Dimension,
    val viewBoxMinX: Int = 0, val viewBoxMinY: Int = 0, val viewBoxWidth: Int = 24, val viewBoxHeight: Int = 24,
    val paths: List<Path>,
) : ImageSource() {
    fun color(fillColor: Paint? = null, strokeColor: Color? = null, strokeWidth: Double? = null) = copy(paths = paths.map { it.copy(fillColor = fillColor, strokeColor = strokeColor, strokeWidth = strokeWidth) })
    data class Path(val fillColor: Paint? = null, val strokeColor: Color? = null, val strokeWidth: Double? = null, val path: String)
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

        val none = zero()

        fun symmetric(horizontal: Dimension = 0.px, vertical: Dimension = 0.px) =
            Insets(horizontal, vertical, horizontal, vertical)
    }
}

data class TextStyle(
    val color: Color = Color.black,
    val disabledColor: Color = Color.gray,
    val size: Double = 14.0,
    val font: Font = systemDefaultFont,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val allCaps: Boolean = false,
    val lineSpacingMultiplier: Double = 1.0,
    val letterSpacing: Dimension = 0.px,
)

enum class Align {
    Start, Center, End, Stretch
}

enum class TextGravity {
    Left, Center, Right
}

data class KeyboardHints(
    val case: KeyboardCase = KeyboardCase.None,
    val type: KeyboardType = KeyboardType.Text,
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
        val password = KeyboardHints(autocomplete = AutoComplete.Password)
        val newPassword = KeyboardHints(autocomplete = AutoComplete.NewPassword)
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

expect class DimensionRaw
value class Dimension(val value: DimensionRaw)
expect val Int.px: Dimension
expect val Int.rem: Dimension
expect val Double.rem: Dimension
expect inline operator fun Dimension.plus(other: Dimension): Dimension
expect inline operator fun Dimension.minus(other: Dimension): Dimension
expect inline operator fun Dimension.times(other: Float): Dimension
inline operator fun Dimension.times(other: Int): Dimension = this * other.toFloat()
expect inline operator fun Dimension.div(other: Float): Dimension
inline operator fun Dimension.div(other: Int): Dimension = this * other.toFloat()

data class NavigationTab(
    val title: String,
    val icon: Icon,
    val screen: RockScreen
)

interface Icon {
    fun toVector(
        width: Dimension,
        height: Dimension,
        color: Color,
    ): ImageVector
}

data class WidgetOption(val key: String, val display: String)