package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


private fun joinGradientStops(stops: List<GradientStop>): String {
    return stops.joinToString {
        "${it.color.toWeb()} ${it.ratio * 100}%"
    }
}

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

@ViewModifierDsl3
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

@ViewModifierDsl3
actual fun ViewContext.nativeBackground(paint: Paint) = nativeBackground(Background(fill = paint))

@ViewModifierDsl3
actual fun ViewContext.nativeChangingBackground(getBackground: ReactiveScope.() -> Background): ViewWrapper {
    elementToDoList.add {
        reactiveScope { applyBackground(getBackground()) }
    }
    return ViewWrapper
}

@ViewModifierDsl3
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

@ViewModifierDsl3
actual fun ViewContext.changingInteractive(
    transitions: Boolean,
    background: (ReactiveScope.() -> Background)?,
    hoverBackground: (ReactiveScope.() -> Background)?,
    downBackground: (ReactiveScope.() -> Background)?,
    focusedBackground: (ReactiveScope.() -> Background)?,
    disabledBackground: (ReactiveScope.() -> Background)?,
    elevation: (ReactiveScope.() -> Dimension)?,
    hoverElevation: (ReactiveScope.() -> Dimension)?,
    downElevation: (ReactiveScope.() -> Dimension)?,
    focusedElevation: (ReactiveScope.() -> Dimension)?,
    disabledElevation: (ReactiveScope.() -> Dimension)?
): ViewWrapper {
    elementToDoList.add {
        if (transitions) style.transition = "all 0.15s linear"
        else style.removeProperty("transition")
        reactiveScope {
            if (elevation != null) applyBoxShadow(elevation())
        }
        reactiveScope {
            if (hoverElevation != null) applyBoxShadow(hoverElevation(), "hover-")
        }
        reactiveScope {
            if (downElevation != null) applyBoxShadow(downElevation(), "down-")
        }
        reactiveScope {
            if (focusedElevation != null) applyBoxShadow(focusedElevation(), "focused-")
        }
        reactiveScope {
            if (disabledElevation != null) applyBoxShadow(disabledElevation(), "disabled-")
        }
        reactiveScope {
            if (background != null) applyBackground(background())
        }
        reactiveScope {
            if (hoverBackground != null) applyBackground(hoverBackground(), "hover-")
        }
        reactiveScope {
            if (downBackground != null) applyBackground(downBackground(), "down-")
        }
        reactiveScope {
            if (focusedBackground != null) applyBackground(focusedBackground(), "focused-")
        }
        reactiveScope {
            if (disabledBackground != null) applyBackground(disabledBackground(), "disabled-")
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.stackCenter(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.justifyContent = "center"
    style.alignItems = "center"
}

@ViewModifierDsl3
actual fun ViewContext.stackCenterLeft(): ViewWrapper {
    elementToDoList.add {
        style.alignSelf = "center"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.stackCenterRight(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.justifyContent = "flex-end"
    style.alignItems = "center"
}

@ViewModifierDsl3
actual fun ViewContext.stackRight(): ViewWrapper = containsNext<HTMLDivElement>("div") {
    style.display = "flex"
    style.justifyContent = "flex-end"
}

@ViewModifierDsl3
actual fun ViewContext.stackLeft(): ViewWrapper {
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.stackTop(): ViewWrapper {
    elementToDoList.add {
        style.alignSelf = "flex-start"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.stackBottom(): ViewWrapper {
    elementToDoList.add {
        style.alignSelf = "flex-end"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.padding(insets: Insets): ViewWrapper {
    elementToDoList.add {
        style.paddingLeft = insets.left?.value ?: "0"
        style.paddingRight = insets.right?.value ?: "0"
        style.paddingTop = insets.top?.value ?: "0"
        style.paddingBottom = insets.bottom?.value ?: "0"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.padding(insets: Dimension): ViewWrapper = padding(Insets(insets))

@ViewModifierDsl3
actual fun ViewContext.padding(horizontal: Dimension, vertical: Dimension): ViewWrapper =
    padding(Insets.symmetric(horizontal = horizontal, vertical = vertical))

@ViewModifierDsl3
actual fun ViewContext.padding(left: Dimension, top: Dimension, right: Dimension, bottom: Dimension): ViewWrapper =
    padding(Insets(left, top, right, bottom))

@ViewModifierDsl3
actual fun ViewContext.margin(insets: Insets): ViewWrapper {
    elementToDoList.add {
        style.marginLeft = insets.left?.value ?: "0"
        style.marginRight = insets.right?.value ?: "0"
        style.marginTop = insets.top?.value ?: "0"
        style.marginBottom = insets.bottom?.value ?: "0"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.margin(left: Dimension, top: Dimension, right: Dimension, bottom: Dimension): ViewWrapper =
    margin(Insets(left, top, right, bottom))

@ViewModifierDsl3
actual fun ViewContext.margin(insets: Dimension): ViewWrapper = margin(Insets(insets))

@ViewModifierDsl3
actual fun ViewContext.margin(horizontal: Dimension, vertical: Dimension): ViewWrapper =
    margin(Insets.symmetric(horizontal = horizontal, vertical = vertical))

@ViewModifierDsl3
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

@ViewModifierDsl3
actual fun ViewContext.fullWidth(): ViewWrapper {
    elementToDoList.add {
        style.width = "100%"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.fullHeight(): ViewWrapper {
    elementToDoList.add {
        style.height = "100%"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.alignLeft(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "column" -> elementToDoList.add {
            style.alignSelf = "start"
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.alignRight(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "column" -> elementToDoList.add {
            style.alignSelf = "end"
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.alignCenter(): ViewWrapper {
    elementToDoList.add {
        style.alignSelf = "center"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.alignTop(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "row" -> elementToDoList.add {
            style.alignSelf = "start"
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.alignBottom(): ViewWrapper {
    val parent = this.stack.last()
    when (parent.style.flexDirection) {
        "row" -> elementToDoList.add {
            style.alignSelf = "end"
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.weight(amount: Float): ViewWrapper {
    elementToDoList.add {
        style.flexGrow = "$amount"
        style.flexShrink = "$amount"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.scrolls(): ViewWrapper {
    elementToDoList.add {
        style.overflowY = "scroll"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.scrollsHorizontally(): ViewWrapper {
    elementToDoList.add {
        style.display = "flex"
        style.flexDirection = "row"
        style.overflowX = "scroll"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.clickable(
    enabled: Readable<Boolean>,
    onClick: suspend () -> Unit,
): ViewWrapper {
    elementToDoList.add {
        addEventListener("click", {
            if (enabled.once) launch(onClick)
        })
        reactiveScope {
            style.cursor = if (enabled.current) "pointer" else "unset"
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.clickable(
    onClick: suspend () -> Unit,
): ViewWrapper {
    elementToDoList.add {
        addEventListener("click", {
            launch(onClick)
        })
        style.cursor = "pointer"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewContext.ignoreInteraction(): ViewWrapper {
    elementToDoList.add {
        style.setProperty("pointer-events", "none")
    }
    wrapperToDoList.add {
        style.setProperty("pointer-events", "none")
    }
    return ViewWrapper
}
