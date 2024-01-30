package com.lightningkite.rock.models

import com.lightningkite.rock.FileReference
import com.lightningkite.rock.navigation.RockScreen
import kotlin.jvm.JvmInline

class AnimationId

expect class Font

expect val systemDefaultFont: Font

data class FontAndStyle(
    val font: Font = systemDefaultFont,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val allCaps: Boolean = false,
    val lineSpacingMultiplier: Double = 1.2,
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
class ImageLocal(val file: FileReference) : ImageSource()
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

data class PopoverPreferredDirection(
    val horizontal: Boolean = false,
    val after: Boolean = true,
    val align: Align = Align.End,
) {
    companion object {
        val belowRight: PopoverPreferredDirection = PopoverPreferredDirection(false, after = true, align = Align.End)
    }
}

data class KeyboardHints(
    val case: KeyboardCase = KeyboardCase.None,
    val type: KeyboardType = KeyboardType.Text,
    val autocomplete: AutoComplete? = null
) {
    companion object {
        val paragraph = KeyboardHints(KeyboardCase.Sentences, KeyboardType.Text)
        val title = KeyboardHints(KeyboardCase.Words, KeyboardType.Text)
        val id = KeyboardHints(KeyboardCase.Letters, KeyboardType.Text)
        val integer = KeyboardHints(KeyboardCase.None, KeyboardType.Integer)
        val decimal = KeyboardHints(KeyboardCase.None, KeyboardType.Decimal)
        val phone = KeyboardHints(KeyboardCase.None, KeyboardType.Phone)
        val email = KeyboardHints(KeyboardCase.None, KeyboardType.Email, autocomplete = AutoComplete.Email)
        val password = KeyboardHints(autocomplete = AutoComplete.Password)
        val newPassword = KeyboardHints(autocomplete = AutoComplete.NewPassword)
    }
}
enum class AutoComplete { Email, Password, NewPassword, Phone }
enum class KeyboardCase { None, Letters, Words, Sentences }
enum class KeyboardType { Text, Integer, Phone, Decimal, Email }

sealed interface NavElement {
    val title: String
    val icon: Icon
}

data class NavGroup(
    override val title: String,
    override val icon: Icon,
    val children: suspend () -> List<NavElement>,
) : NavElement{
    constructor(title: String, icon: Icon, children: List<NavElement> = listOf()) : this(title, icon, { children })
}

data class NavItem(
    override val title: String,
    override val icon: Icon,
    val destination: suspend () -> RockScreen,
) : NavElement {
    constructor(title: String, icon: Icon, destination: RockScreen) : this(title, icon, { destination })
}

data class ExternalNav(
    override val title: String,
    override val icon: Icon,
    val to: String,
) : NavElement

data class Action(
    override val title: String,
    override val icon: Icon,
    val onSelect: suspend () -> Unit,
) : NavElement



enum class ImageScaleType { Fit, Crop, Stretch, NoScale }

expect class DimensionRaw
@JvmInline
value class Dimension(val value: DimensionRaw): Comparable<Dimension> {
    override fun compareTo(other: Dimension): Int = this.compareToImpl(other)
}
expect val Int.px: Dimension
expect val Int.rem: Dimension
expect val Int.dp: Dimension
expect val Double.rem: Dimension
expect val Double.dp: Dimension
expect fun Dimension.compareToImpl(other: Dimension): Int
expect inline operator fun Dimension.plus(other: Dimension): Dimension
expect inline operator fun Dimension.minus(other: Dimension): Dimension
expect inline operator fun Dimension.times(other: Float): Dimension
inline operator fun Dimension.times(other: Int): Dimension = this * other.toFloat()
inline operator fun Dimension.times(other: Double): Dimension = this * other.toFloat()
expect inline operator fun Dimension.div(other: Float): Dimension
inline operator fun Dimension.div(other: Int): Dimension = this * other.toFloat()
inline operator fun Dimension.div(other: Double): Dimension = this * other.toFloat()

data class WidgetOption(val key: String, val display: String)