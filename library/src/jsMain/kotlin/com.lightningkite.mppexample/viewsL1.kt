package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement

//actual var NView.rotation: Double
//    get() = TODO("Not yet implemented")
//    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias SimpleLabel = HTMLParagraphElement

actual inline fun ViewContext.simpleLabel(setup: SimpleLabel.() -> Unit): Unit = element("p", setup)
actual var SimpleLabel.text: String
    get() = this.textContent ?: ""
    set(value) {
        this.textContent = value
    }


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Column = HTMLDivElement

actual inline fun ViewContext.column(setup: Column.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "column"
    setup()
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Row = HTMLDivElement

actual inline fun ViewContext.row(setup: Row.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "flex"
    style.flexDirection = "row"
    setup()
}

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
