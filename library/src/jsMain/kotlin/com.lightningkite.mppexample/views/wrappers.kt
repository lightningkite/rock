package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


private fun joinGradientStops(stops: List<GradientStop>): String {
    return stops.joinToString {
        "${it.color.toWeb()} ${it.ratio * 100}%"
    }
}

//fun getElementUuid(element: HTMLElement): String {
//    var current = element.getAttribute("data-rock-uuid")
//    if (current.isNullOrEmpty()) {
//       current = uuid()
//       element.setAttribute("data-rock-uuid", current)
//    }
//    return current
//}

fun LinearGradient.toCss() = "linear-gradient(${angle.turns}turn, ${joinGradientStops(stops)})"
fun RadialGradient.toCss() = "radial-gradient(circle at center, ${joinGradientStops(stops)})"

fun HTMLElement.applyBoxShadow(elevation: Dimension?, prefix: String = "") {
    if (elevation != null) {
        style.setProperty("--${prefix}box-shadow", elevation.toBoxShadow())
        classList.add("${prefix}box-shadow")
    } else {
        classList.remove("${prefix}box-shadow")
    }
}

fun HTMLElement.applyBackground(background: Background, prefix: String = "") {
    classList.remove(
        "${prefix}background",
        "${prefix}background-image",
        "${prefix}background-attachment",
        "${prefix}border-color",
        "${prefix}border-width",
        "${prefix}border-radius"
    )
    when (background.fill) {
        is Color -> {
            style.setProperty("--${prefix}background", background.fill.toWeb())
            classList.add("${prefix}background")
        }

        is LinearGradient -> {
            style.setProperty("--${prefix}background-image", background.fill.toCss())
            style.setProperty(
                "--${prefix}background-attachment", if (background.fill.screenStatic) "fixed" else "unset"
            )
            classList.add("${prefix}background-image", "${prefix}background-attachment")
        }

        is RadialGradient -> {
            style.setProperty("--${prefix}background-image", background.fill.toCss())
            style.setProperty(
                "--${prefix}background-attachment", if (background.fill.screenStatic) "fixed" else "unset"
            )
            classList.add("${prefix}background-image", "${prefix}background-attachment")
        }

        null -> {}
    }

    if (background.stroke != null) {
        style.setProperty("--${prefix}border-color", background.stroke.toWeb())
        classList.add("${prefix}border-color")
    }

    if (background.strokeWidth != null) {
        style.setProperty("--${prefix}border-style", "solid")
        style.setProperty("--${prefix}border-width", background.strokeWidth.value)
        classList.add("${prefix}border-width")
    } else {
        style.setProperty("--${prefix}border-style", "none")
    }
    classList.add("${prefix}border-style")

    if (background.corners != null) {
        style.setProperty("--${prefix}border-top-left-radius", background.corners.topLeft.value)
        style.setProperty("--${prefix}border-top-right-radius", background.corners.topRight.value)
        style.setProperty("--${prefix}border-bottom-left-radius", background.corners.bottomLeft.value)
        style.setProperty("--${prefix}border-bottom-right-radius", background.corners.bottomRight.value)
        classList.add("${prefix}border-radius")
    }
}

actual fun ViewContext.nativeBackground(background: Background?, elevation: Dimension?): ViewWrapper {
    elementToDoList.add {
        if (background != null) applyBackground(background)
        if (elevation != null) {
            style.setProperty("--box-shadow", elevation.toBoxShadow())
            classList.add("box-shadow")
        }
    }
    return ViewWrapper
}

actual fun ViewContext.nativeBackground(paint: Paint) = nativeBackground(Background(fill = paint))

actual fun ViewContext.nativeChangingBackground(getBackground: ReactiveScope.() -> Background): ViewWrapper {
    elementToDoList.add {
        reactiveScope { applyBackground(getBackground()) }
    }
    return ViewWrapper
}

actual fun ViewContext.interactive(
    transitions: Boolean,
    background: Background?,
    hoverBackground: Background?,
    downBackground: Background?,
    focusedBackground: Background?,
    disabledBackground: Background?,
    elevation: Dimension?,
    hoverElevation: Dimension?,
    downElevation: Dimension?,
    focusedElevation: Dimension?,
    disabledElevation: Dimension?
): ViewWrapper {
    elementToDoList.add {
        if (transitions) style.transition = "all 0.15s linear"
        else style.removeProperty("transition")
        if (elevation != null) applyBoxShadow(elevation)
        if (hoverElevation != null) applyBoxShadow(hoverElevation, "hover-")
        if (downElevation != null) applyBoxShadow(downElevation, "down-")
        if (focusedElevation != null) applyBoxShadow(focusedElevation, "focused-")
        if (disabledElevation != null) applyBoxShadow(disabledElevation, "disabled-")
        if (background != null) applyBackground(background)
        if (hoverBackground != null) applyBackground(hoverBackground, "hover-")
        if (downBackground != null) applyBackground(downBackground, "down-")
        if (focusedBackground != null) applyBackground(focusedBackground, "focused-")
        if (disabledBackground != null) applyBackground(disabledBackground, "disabled-")
    }
    return ViewWrapper
}

actual fun ViewContext.stackCenter(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.alignItems = "center"
    style.justifyContent = "center"
}

actual fun ViewContext.stackRight(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.justifyContent = "flex-end"
}

actual fun ViewContext.stackLeft(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.justifyContent = "flex-start"
}

actual fun ViewContext.stackTop(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.alignItems = "flex-start"
}

actual fun ViewContext.stackBottom(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.alignItems = "flex-end"
}

//actual fun ViewContext.background(background: Background): ViewWrapper = containsNext<HTMLDivElement>("div") {
//    style.removeProperty("background")
//    style.removeProperty("backgroundImage")
//    if (background.fill is Color)
//        style.background = background.fill.toWeb()
//    if (background.fill is LinearGradient) {
//        this.style.backgroundImage = "linear-gradient(${background.fill.angle.turns}turn, ${
//            background.fill.stops.joinToString { it.color.toWeb() }
//        })"
//    }
//}

actual fun ViewContext.padding(insets: Insets): ViewWrapper {
    elementToDoList.add {
        style.paddingLeft = insets.left?.value ?: "0"
        style.paddingRight = insets.right?.value ?: "0"
        style.paddingTop = insets.top?.value ?: "0"
        style.paddingBottom = insets.bottom?.value ?: "0"
    }
    return ViewWrapper
}

actual fun ViewContext.padding(insets: Dimension): ViewWrapper = padding(Insets(insets))

actual fun ViewContext.margin(insets: Insets): ViewWrapper {
    elementToDoList.add {
        style.marginLeft = insets.left?.value ?: "0"
        style.marginRight = insets.right?.value ?: "0"
        style.marginTop = insets.top?.value ?: "0"
        style.marginBottom = insets.bottom?.value ?: "0"
    }
    return ViewWrapper
}

actual fun ViewContext.margin(insets: Dimension): ViewWrapper = margin(Insets(insets))

actual fun ViewContext.sizedBox(constraints: SizeConstraints): ViewWrapper {
    elementToDoList.add {
        if (constraints.minHeight == null) style.removeProperty("minHeight")
        else style.minHeight = constraints.minHeight.value

        if (constraints.maxHeight == null) style.removeProperty("maxHeight")
        else style.maxHeight = constraints.maxHeight.value

        if (constraints.minWidth == null) style.removeProperty("minWidth")
        else style.minWidth = constraints.minWidth.value

        if (constraints.maxWidth == null) style.removeProperty("maxWidth")
        else style.maxWidth = constraints.maxWidth.value

        if (constraints.width == null) style.removeProperty("width")
        else style.width = constraints.width.value

        if (constraints.height == null) style.removeProperty("height")
        else style.height = constraints.height.value

        style.overflowX = "hidden"
        style.overflowY = "hidden"

    }
    return ViewWrapper
}

actual fun ViewContext.fullWidth(): ViewWrapper {
    elementToDoList.add {
        style.width = "100%"
    }
    return ViewWrapper
}

actual fun ViewContext.fullHeight(): ViewWrapper {
    elementToDoList.add {
        style.height = "100%"
    }
    return ViewWrapper
}

actual fun ViewContext.alignLeft(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "column" -> elementToDoList.add {
            style.alignSelf = "start"
        }
    }
    return ViewWrapper
}

actual fun ViewContext.alignRight(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "column" -> elementToDoList.add {
            style.alignSelf = "end"
        }
    }
    return ViewWrapper
}

actual fun ViewContext.alignCenter(): ViewWrapper {
    elementToDoList.add {
        style.alignSelf = "center"
    }
    return ViewWrapper
}

actual fun ViewContext.alignTop(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "row" -> elementToDoList.add {
            style.alignSelf = "start"
        }
    }
    return ViewWrapper
}

actual fun ViewContext.alignBottom(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "row" -> elementToDoList.add {
            style.alignSelf = "end"
        }
    }
    return ViewWrapper
}

actual fun ViewContext.weight(amount: Float): ViewWrapper {
    elementToDoList.add {
        style.flexGrow = "$amount"
        style.flexShrink = "$amount"
    }
    return ViewWrapper
}

actual fun ViewContext.scrolls(): ViewWrapper {
    elementToDoList.add {
        style.overflowY = "scroll"
    }
    return ViewWrapper
}

actual fun ViewContext.scrollsHorizontally(): ViewWrapper {
    elementToDoList.add {
        style.display = "flex"
        style.flexDirection = "row"
        style.overflowX = "scroll"
    }
    return ViewWrapper
}

actual fun ViewContext.clickable(
    enabled: Readable<Boolean>,
    onClick: suspend () -> Unit,
): ViewWrapper {
    elementToDoList.add {
        addEventListener("click", {
            if (enabled.once) launch(onClick)
        })
    }
    return ViewWrapper
}

actual fun ViewContext.clickable(
    onClick: suspend () -> Unit,
): ViewWrapper {
    elementToDoList.add {
        addEventListener("click", {
            launch(onClick)
        })
    }
    return ViewWrapper
}
