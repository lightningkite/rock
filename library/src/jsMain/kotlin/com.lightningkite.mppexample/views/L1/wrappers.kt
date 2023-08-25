package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement


private fun joinGradientStops(stops: List<GradientStop>): String {
    return stops.joinToString {
        "${it.color.toWeb()} ${it.ratio * 100}%"
    }
}

actual fun ViewContext.withBackground(background: Background): ViewWrapper {
    elementToDoList.add {
        style.removeProperty("background")
        style.removeProperty("backgroundImage")
        style.removeProperty("backgroundAttachment")
        if (background.fill is Color)
            style.background = background.fill.toWeb()
        if (background.fill is LinearGradient) {
            if (background.fill.screenStatic)
                this.style.backgroundAttachment = "fixed"
            this.style.backgroundImage = "linear-gradient(${background.fill.angle.turns}turn, ${
                joinGradientStops(background.fill.stops)
            })"
        }
        if (background.fill is RadialGradient) {
            if (background.fill.screenStatic)
                this.style.backgroundAttachment = "fixed"
            this.style.backgroundImage = "radial-gradient(circle at center, ${
                joinGradientStops(background.fill.stops)
            })"
        }
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

actual fun ViewContext.padding(insets: Insets): ViewWrapper =
    containsNext<HTMLDivElement>("div") {
        style.paddingLeft = insets.left?.value ?: "0"
        style.paddingRight = insets.right?.value ?: "0"
        style.paddingTop = insets.top?.value ?: "0"
        style.paddingBottom = insets.bottom?.value ?: "0"
    }

actual fun ViewContext.padding(insets: String): ViewWrapper = padding(Insets(Dimension(insets)))

actual fun ViewContext.margin(insets: Insets): ViewWrapper =
    containsNext<HTMLDivElement>("div") {
        style.marginLeft = insets.left?.value ?: "0"
        style.marginRight = insets.right?.value ?: "0"
        style.marginTop = insets.top?.value ?: "0"
        style.marginBottom = insets.bottom?.value ?: "0"
    }

actual fun ViewContext.margin(insets: String): ViewWrapper = margin(Insets(Dimension(insets)))

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

