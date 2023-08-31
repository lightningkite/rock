package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ActivityIndicator = HTMLDivElement

fun ActivityIndicator.setupSharedIndicator(color: Color) {
    style.boxSizing = "border-box"
    style.display = "block"
    style.position = "absolute"
    style.width = 64.px.value
    style.height = 64.px.value
    style.margin = 8.px.value
    style.border = "8px solid #fff"
    style.borderRadius = "50%"
    style.animation = "rock-activity-indicator 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite"
    style.borderColor = "${color.toWeb()} transparent transparent transparent"
}

actual inline fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit =
    element<HTMLDivElement>("div") {
        style.display = "inline-block"
        style.position = "relative"
        style.width = 80.px.value
        style.height = 80.px.value
        style.minWidth = 80.px.value
        style.minHeight = 80.px.value
        style.maxWidth = 80.px.value
        style.maxHeight = 80.px.value

        val defaultColor = Color.blue
        box {
            className = "rock-activity-indicator-1"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.45s"
        }
        box {
            className = "rock-activity-indicator-2"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.3s"
        }
        box {
            className = "rock-activity-indicator-3"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.15s"
        }
        box {
            className = "rock-activity-indicator-4"
            setupSharedIndicator(defaultColor)
        }

        setup()
    }

actual var ActivityIndicator.color: Color
    get() = throw NotImplementedError()
    set(value) {
        val color = "${value.toWeb()} transparent transparent transparent"
        getElementsByClassName("rock-activity-indicator-1").get(0).apply {
            this as HTMLElement
            this.style.borderColor = color
        }
        getElementsByClassName("rock-activity-indicator-2").get(0).apply {
            this as HTMLElement
            this.style.borderColor = color
        }
        getElementsByClassName("rock-activity-indicator-3").get(0).apply {
            this as HTMLElement
            this.style.borderColor = color
        }
        getElementsByClassName("rock-activity-indicator-4").get(0).apply {
            this as HTMLElement
            this.style.borderColor = color
        }
    }
