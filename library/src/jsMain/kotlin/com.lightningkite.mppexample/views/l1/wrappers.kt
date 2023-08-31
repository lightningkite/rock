package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.asList


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

actual fun ViewContext.hoverable(background: Background?, elevation: Dimension?): ViewWrapper {
    elementToDoList.add {
        if (elevation != null) {
            style.setProperty("--hover-box-shadow", elevation.toBoxShadow())
            classList.add("hover-box-shadow")
        }

        if (background != null) {
            when (background.fill) {
                is Color -> {
                    style.setProperty("--hover-background", background.fill.toWeb())
                    classList.add("hover-background")
                }
                is LinearGradient -> {
                    style.setProperty("--hover-background-image", background.fill.toCss())
                    style.setProperty("--hover-background-attachment", if (background.fill.screenStatic) "fixed" else "unset")
                    classList.add("hover-background-image", "hover-background-attachment")
                }
                is RadialGradient -> {
                    style.setProperty("--hover-background-image", background.fill.toCss())
                    style.setProperty("--hover-background-attachment", if (background.fill.screenStatic) "fixed" else "unset")
                    classList.add("hover-background-image", "hover-background-attachment")
                }
                null -> {}
            }

            if (background.stroke != null) {
                style.setProperty("--hover-border-color", background.stroke.toWeb())
                classList.add("hover-border-color")
            }

            if (background.strokeWidth != null) {
                style.borderStyle = "solid"
                style.setProperty("--hover-border-width", background.strokeWidth.value)
                classList.add("hover-border-width")
            }

            if (background.corners != null) {
                throw NotImplementedError("Corners on hover not implemented")
            }
        }
    }
    return ViewWrapper
}

fun HTMLElement.applyBackground(background: Background) {
    style.removeProperty("background")
    style.removeProperty("backgroundImage")
    style.removeProperty("backgroundAttachment")
    when (background.fill) {
        is Color -> style.background = background.fill.toWeb()
        is LinearGradient -> {
            if (background.fill.screenStatic)
                this.style.backgroundAttachment = "fixed"
            this.style.backgroundImage = background.fill.toCss()
        }
        is RadialGradient -> {
            if (background.fill.screenStatic)
                this.style.backgroundAttachment = "fixed"
            this.style.backgroundImage = background.fill.toCss()
        }
        null -> {}
    }

    if (background.stroke == null)
        style.removeProperty("borderColor")
    else
        style.borderColor = background.stroke.toWeb()

    if (background.strokeWidth == null)
        style.removeProperty("borderWidth")
    else {
        style.borderStyle = "solid"
        style.borderWidth = background.strokeWidth.value
    }

    if (background.corners == null) {
        style.removeProperty("borderTopLeftRadius")
        style.removeProperty("borderTopRightRadius")
        style.removeProperty("borderBottomLeftRadius")
        style.removeProperty("borderBottomRightRadius")
    } else {
        style.borderTopLeftRadius = background.corners.topLeft.value
        style.borderTopRightRadius = background.corners.topRight.value
        style.borderBottomLeftRadius = background.corners.bottomLeft.value
        style.borderBottomRightRadius = background.corners.bottomRight.value
    }
}

actual fun ViewContext.withBackground(background: Background): ViewWrapper {
    elementToDoList.add { applyBackground(background) }
    return ViewWrapper
}

actual fun ViewContext.changingBackground(getBackground: ReactiveScope.() -> Background): ViewWrapper {
    elementToDoList.add {
        reactiveScope { applyBackground(getBackground()) }
    }
    return ViewWrapper
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
        if (constraints.minHeight == null)
            style.removeProperty("minHeight")
        else
            style.minHeight = constraints.minHeight.value

        if (constraints.maxHeight == null)
            style.removeProperty("maxHeight")
        else
            style.maxHeight = constraints.maxHeight.value

        if (constraints.minWidth == null)
            style.removeProperty("minWidth")
        else
            style.minWidth = constraints.minWidth.value

        if (constraints.maxWidth == null)
            style.removeProperty("maxWidth")
        else
            style.maxWidth = constraints.maxWidth.value

        if (constraints.width == null)
            style.removeProperty("width")
        else
            style.width = constraints.width.value

        if (constraints.height == null)
            style.removeProperty("height")
        else
            style.height = constraints.height.value

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
        style.display = "flex"
        style.flexDirection = "column"
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
