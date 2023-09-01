package com.lightningkite.mppexample

import org.w3c.dom.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ActivityIndicator = HTMLDivElement

fun ActivityIndicator.setupSharedIndicator(color: Color) {
    style.boxSizing = "border-box"
    style.display = "block"
    style.position = "absolute"
    style.width = "100%"
    style.height = "100%"
    style.border = "4px solid #fff"
    style.borderRadius = "50%"
    style.animation = "rock-activity-indicator 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite"
    style.borderColor = "${color.toWeb()} transparent transparent transparent"
}

actual inline fun ViewContext.nativeActivityIndicator(setup: ActivityIndicator.() -> Unit): Unit =
    element<HTMLDivElement>("div") {
        style.display = "inline-block"
        style.position = "relative"
        width = 80.px
        height = 80.px

        val defaultColor = Color.blue
        box {
            className = "rock-activity-indicator-item rock-activity-indicator-1"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.45s"
        }
        box {
            className = "rock-activity-indicator-item rock-activity-indicator-2"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.3s"
        }
        box {
            className = "rock-activity-indicator-item rock-activity-indicator-3"
            setupSharedIndicator(defaultColor)
            style.animationDelay = "-0.15s"
        }
        box {
            className = "rock-activity-indicator-item rock-activity-indicator-4"
            setupSharedIndicator(defaultColor)
        }

        setup()
    }

actual var ActivityIndicator.color: Color
    get() = throw NotImplementedError()
    set(value) {
        val color = "${value.toWeb()} transparent transparent transparent"
        getElementsByClassName("rock-activity-indicator-item").asList().forEach {
            it as HTMLElement
            it.style.borderColor = color
        }
    }

actual var ActivityIndicator.width: Dimension
    get() = throw NotImplementedError()
    set(value) {
        style.width = value.value
        style.maxWidth = value.value
        style.minWidth = value.value
    }

actual var ActivityIndicator.height: Dimension
    get() = throw NotImplementedError()
    set(value) {
        style.height = value.value
        style.maxHeight = value.value
        style.minHeight = value.value
    }

actual var ActivityIndicator.lineWidth: Dimension
    get() = throw NotImplementedError()
    set(value) {
        getElementsByClassName("rock-activity-indicator-item").asList().forEach {
            it as HTMLElement
            it.style.borderWidth = value.value
        }
    }
