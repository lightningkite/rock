package com.lightningkite.rock.models

import com.lightningkite.rock.FileReference
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

class AnimationId

expect class Font

expect val systemDefaultFont: Font
expect val systemDefaultFixedWidthFont: Font

data class FontAndStyle(
    val font: Font = systemDefaultFont,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val allCaps: Boolean = false,
    val lineSpacingMultiplier: Double = 1.2,
    val additionalLetterSpacing: Dimension = 0.px,
)

data class Icon(
    val width: Dimension, val height: Dimension,
    val viewBoxMinX: Int = 0, val viewBoxMinY: Int = 0, val viewBoxWidth: Int = 24, val viewBoxHeight: Int = 24,
    val pathDatas: List<String>,
) {
    fun toImageSource(fillColor: Paint?, strokeColor: Color? = null, strokeWidth: Double? = null) = ImageVector(width, height, viewBoxMinX, viewBoxMinY, viewBoxWidth, viewBoxHeight, pathDatas.map { ImageVector.Path(fillColor, strokeColor, strokeWidth, it) })
    companion object {
        val search = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"))
        val home = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M240-200h120v-240h240v240h120v-360L480-740 240-560v360Zm-80 80v-480l320-240 320 240v480H520v-240h-80v240H160Zm320-350Z"))
        val menu = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z"))
        val close = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"))
        val settings = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m370-80-16-128q-13-5-24.5-12T307-235l-119 50L78-375l103-78q-1-7-1-13.5v-27q0-6.5 1-13.5L78-585l110-190 119 50q11-8 23-15t24-12l16-128h220l16 128q13 5 24.5 12t22.5 15l119-50 110 190-103 78q1 7 1 13.5v27q0 6.5-2 13.5l103 78-110 190-118-50q-11 8-23 15t-24 12L590-80H370Zm70-80h79l14-106q31-8 57.5-23.5T639-327l99 41 39-68-86-65q5-14 7-29.5t2-31.5q0-16-2-31.5t-7-29.5l86-65-39-68-99 42q-22-23-48.5-38.5T533-694l-13-106h-79l-14 106q-31 8-57.5 23.5T321-633l-99-41-39 68 86 64q-5 15-7 30t-2 32q0 16 2 31t7 30l-86 65 39 68 99-42q22 23 48.5 38.5T427-266l13 106Zm42-180q58 0 99-41t41-99q0-58-41-99t-99-41q-59 0-99.5 41T342-480q0 58 40.5 99t99.5 41Zm-2-140Z"))
        val done = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"))
        val add = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M440-440H200v-80h240v-240h80v240h240v80H520v240h-80v-240Z"))
        val delete = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"))
        val arrowBack = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m313-440 224 224-57 56-320-320 320-320 57 56-224 224h487v80H313Z"))
        val chevronRight = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z"))
        val logout = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h280v80H200v560h280v80H200Zm440-160-55-58 102-102H360v-80h327L585-622l55-58 200 200-200 200Z"))
        val login = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M480-120v-80h280v-560H480v-80h280q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H480Zm-80-160-55-58 102-102H120v-80h327L345-622l55-58 200 200-200 200Z"))
        val moreHoriz = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M240-400q-33 0-56.5-23.5T160-480q0-33 23.5-56.5T240-560q33 0 56.5 23.5T320-480q0 33-23.5 56.5T240-400Zm240 0q-33 0-56.5-23.5T400-480q0-33 23.5-56.5T480-560q33 0 56.5 23.5T560-480q0 33-23.5 56.5T480-400Zm240 0q-33 0-56.5-23.5T640-480q0-33 23.5-56.5T720-560q33 0 56.5 23.5T800-480q0 33-23.5 56.5T720-400Z"))
        val moreVert = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M480-160q-33 0-56.5-23.5T400-240q0-33 23.5-56.5T480-320q33 0 56.5 23.5T560-240q0 33-23.5 56.5T480-160Zm0-240q-33 0-56.5-23.5T400-480q0-33 23.5-56.5T480-560q33 0 56.5 23.5T560-480q0 33-23.5 56.5T480-400Zm0-240q-33 0-56.5-23.5T400-720q0-33 23.5-56.5T480-800q33 0 56.5 23.5T560-720q0 33-23.5 56.5T480-640Z"))
        val deleteForever = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m376-300 104-104 104 104 56-56-104-104 104-104-56-56-104 104-104-104-56 56 104 104-104 104 56 56Zm-96 180q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520Zm-400 0v520-520Z"))
        val remove = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M200-440v-80h560v80H200Z"))
        val download = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M480-320 280-520l56-58 104 104v-326h80v326l104-104 56 58-200 200ZM240-160q-33 0-56.5-23.5T160-240v-120h80v120h480v-120h80v120q0 33-23.5 56.5T720-160H240Z"))
        val sync = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M160-160v-80h110l-16-14q-52-46-73-105t-21-119q0-111 66.5-197.5T400-790v84q-72 26-116 88.5T240-478q0 45 17 87.5t53 78.5l10 10v-98h80v240H160Zm400-10v-84q72-26 116-88.5T720-482q0-45-17-87.5T650-648l-10-10v98h-80v-240h240v80H690l16 14q49 49 71.5 106.5T800-482q0 111-66.5 197.5T560-170Z"))
        val block = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q54 0 104-17.5t92-50.5L228-676q-33 42-50.5 92T160-480q0 134 93 227t227 93Zm252-124q33-42 50.5-92T800-480q0-134-93-227t-227-93q-54 0-104 17.5T284-732l448 448Z"))
        val sort = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M120-240v-80h240v80H120Zm0-200v-80h480v80H120Zm0-200v-80h720v80H120Z"))
        val filterList = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M400-240v-80h160v80H400ZM240-440v-80h480v80H240ZM120-640v-80h720v80H120Z"))
        val star = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m354-247 126-76 126 77-33-144 111-96-146-13-58-136-58 135-146 13 111 97-33 143ZM233-80l65-281L80-550l288-25 112-265 112 265 288 25-218 189 65 281-247-149L233-80Zm247-350Z"))
        val starFilled = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m233-80 65-281L80-550l288-25 112-265 112 265 288 25-218 189 65 281-247-149L233-80Z"))
        val person = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-160v-112q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v112H160Zm80-80h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z"))
        val group = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M40-160v-112q0-34 17.5-62.5T104-378q62-31 126-46.5T360-440q66 0 130 15.5T616-378q29 15 46.5 43.5T680-272v112H40Zm720 0v-120q0-44-24.5-84.5T666-434q51 6 96 20.5t84 35.5q36 20 55 44.5t19 53.5v120H760ZM360-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47Zm400-160q0 66-47 113t-113 47q-11 0-28-2.5t-28-5.5q27-32 41.5-71t14.5-81q0-42-14.5-81T544-792q14-5 28-6.5t28-1.5q66 0 113 47t47 113ZM120-240h480v-32q0-11-5.5-20T580-306q-54-27-109-40.5T360-360q-56 0-111 13.5T140-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-640q0-33-23.5-56.5T360-720q-33 0-56.5 23.5T280-640q0 33 23.5 56.5T360-560Zm0 320Zm0-400Z"))
        val warning = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("m40-120 440-760 440 760H40Zm138-80h604L480-720 178-200Zm302-40q17 0 28.5-11.5T520-280q0-17-11.5-28.5T480-320q-17 0-28.5 11.5T440-280q0 17 11.5 28.5T480-240Zm-40-120h80v-200h-80v200Zm40-100Z"))
        val send = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M120-160v-640l760 320-760 320Zm80-120 474-200-474-200v140l240 60-240 60v140Zm0 0v-400 400Z"))
        val chat = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M240-400h320v-80H240v80Zm0-120h480v-80H240v80Zm0-120h480v-80H240v80ZM80-80v-720q0-33 23.5-56.5T160-880h640q33 0 56.5 23.5T880-800v480q0 33-23.5 56.5T800-240H240L80-80Zm126-240h594v-480H160v525l46-45Zm-46 0v-480 480Z"))
        val list = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M360-200v-80h480v80H360Zm0-240v-80h480v80H360Zm0-240v-80h480v80H360ZM200-160q-33 0-56.5-23.5T120-240q0-33 23.5-56.5T200-320q33 0 56.5 23.5T280-240q0 33-23.5 56.5T200-160Zm0-240q-33 0-56.5-23.5T120-480q0-33 23.5-56.5T200-560q33 0 56.5 23.5T280-480q0 33-23.5 56.5T200-400Zm0-240q-33 0-56.5-23.5T120-720q0-33 23.5-56.5T200-800q33 0 56.5 23.5T280-720q0 33-23.5 56.5T200-640Z"))
        val notificationFilled = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M160-200v-80h80v-280q0-83 50-147.5T420-792v-28q0-25 17.5-42.5T480-880q25 0 42.5 17.5T540-820v28q80 20 130 84.5T720-560v280h80v80H160ZM480-80q-33 0-56.5-23.5T400-160h160q0 33-23.5 56.5T480-80Z"))
        val notification = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M160-200v-80h80v-280q0-83 50-147.5T420-792v-28q0-25 17.5-42.5T480-880q25 0 42.5 17.5T540-820v28q80 20 130 84.5T720-560v280h80v80H160Zm320-300Zm0 420q-33 0-56.5-23.5T400-160h160q0 33-23.5 56.5T480-80ZM320-280h320v-280q0-66-47-113t-113-47q-66 0-113 47t-47 113v280Z"))
        val email = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf("M160-160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800h640q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160Zm320-280L160-640v400h640v-400L480-440Zm0-80 320-200H160l320 200ZM160-640v-80 480-400Z"))
    }

}


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

expect sealed class VideoSource()
data class VideoRemote(val url: String) : VideoSource()
class VideoRaw(val data: ByteArray) : VideoSource()
class VideoLocal(val file: FileReference) : VideoSource()
expect class VideoResource : VideoSource

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
        val belowLeft: PopoverPreferredDirection = PopoverPreferredDirection(false, after = true, align = Align.Start)
        val belowCenter: PopoverPreferredDirection = PopoverPreferredDirection(false, after = true, align = Align.Center)
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
    val title: suspend () -> String
    val icon: suspend () -> Icon
    val count: (suspend ()->Int?)?
}

data class NavGroup(
    override val title: suspend () -> String,
    override val icon: suspend () -> Icon,
    override val count: (suspend () -> Int?)? = null,
    val children: suspend () -> List<NavElement>,
) : NavElement{
    constructor(title: String, icon: Icon, children: List<NavElement> = listOf()) : this({ title }, { icon }, null, { children })
}

@Deprecated("Use NavLink", ReplaceWith("NavLink"))
typealias NavItem = NavLink

data class NavLink(
    override val title: suspend () -> String,
    override val icon: suspend () -> Icon,
    override val count: (suspend () -> Int?)? = null,
    val destination: suspend () -> RockScreen,
) : NavElement {
    constructor(title: String, icon: Icon, destination: RockScreen) : this({ title }, { icon }, null, { destination })
}

@Deprecated("Use NavExternal", ReplaceWith("NavExternal"))
typealias ExternalNav = NavExternal
data class NavExternal(
    override val title: suspend () -> String,
    override val icon: suspend () -> Icon,
    override val count: (suspend () -> Int?)? = null,
    val to: suspend () -> String,
) : NavElement

data class NavAction(
    override val title: suspend () -> String,
    override val icon: suspend () -> Icon,
    override val count: (suspend () -> Int?)? = null,
    val onSelect: suspend () -> Unit,
) : NavElement

data class NavCustom(
    override val title: suspend () -> String = { "" },
    override val icon: suspend () -> Icon = { Icon.moreHoriz },
    override val count: (suspend () -> Int?)? = null,
    val square: ViewWriter.()->Unit,
    val long: ViewWriter.()->Unit,
    val tall: ViewWriter.()->Unit,
) : NavElement

data class Action(
    val title: String,
    val icon: Icon,
    val onSelect: suspend () -> Unit,
)


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
inline operator fun Dimension.div(other: Int): Dimension = this / other.toFloat()
inline operator fun Dimension.div(other: Double): Dimension = this / other.toFloat()

data class WidgetOption(val key: String, val display: String)