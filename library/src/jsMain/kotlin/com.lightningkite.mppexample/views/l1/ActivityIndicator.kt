package com.lightningkite.mppexample

import org.w3c.dom.HTMLDivElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias ActivityIndicator = HTMLDivElement

//fun ActivityIndicator.setupSharedIndicator(color: Color) {
//    style.boxSizing = "border-box"
//    style.display = "block"
//    style.position = "absolute"
//    style.width = 64.px.value
//    style.height = 64.px.value
//    style.margin = 8.px.value
//    style.border = "8px solid #fff"
//    style.borderRadius = "50%"
//    style.animation = "rock-activity-indicator 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite"
//    style.borderColor = "${color.toWeb()} transparent transparent transparent"
//}

actual inline fun ViewContext.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit = element<HTMLDivElement>("div") {
    style.display = "inline-block"
    style.position = "relative"
    style.width = 80.px.value
    style.height = 80.px.value
    style.minWidth = 80.px.value
    style.minHeight = 80.px.value
    style.maxWidth = 80.px.value
    style.maxHeight = 80.px.value

    color = Color.blue
    setup()

//    val defaultColor = Color.blue
//    box {
//        setupSharedIndicator(defaultColor)
//        style.animationDelay = "-0.45s"
//    }
//    box {
//        setupSharedIndicator(defaultColor)
//        style.animationDelay = "-0.3s"
//    }
//    box {
//        setupSharedIndicator(defaultColor)
//        style.animationDelay = "-0.15s"
//    }
//    box {
//        setupSharedIndicator(defaultColor)
//    }
}

actual var ActivityIndicator.color: Color
    get() = throw NotImplementedError()
    set(value) {
        val color = value.toWeb()
        innerHTML = "<div style=\"display: block; flex-direction: column; box-sizing: border-box; position: absolute; width: 64px; height: 64px; margin: 8px; border-width: 8px; border-style: solid; border-color: $color transparent transparent; border-image: none; border-radius: 50%; animation: 1.2s cubic-bezier(0.5, 0, 0.5, 1) -0.45s infinite normal none running rock-activity-indicator;\"></div><div style=\"display: block; flex-direction: column; box-sizing: border-box; position: absolute; width: 64px; height: 64px; margin: 8px; border-width: 8px; border-style: solid; border-color: $color transparent transparent; border-image: none; border-radius: 50%; animation: 1.2s cubic-bezier(0.5, 0, 0.5, 1) -0.3s infinite normal none running rock-activity-indicator;\"></div><div style=\"display: block; flex-direction: column; box-sizing: border-box; position: absolute; width: 64px; height: 64px; margin: 8px; border-width: 8px; border-style: solid; border-color: $color transparent transparent; border-image: none; border-radius: 50%; animation: 1.2s cubic-bezier(0.5, 0, 0.5, 1) -0.15s infinite normal none running rock-activity-indicator;\"></div><div style=\"display: block; flex-direction: column; box-sizing: border-box; position: absolute; width: 64px; height: 64px; margin: 8px; border-width: 8px; border-style: solid; border-color: $color transparent transparent; border-image: none; border-radius: 50%; animation: 1.2s cubic-bezier(0.5, 0, 0.5, 1) 0s infinite normal none running rock-activity-indicator;\"></div>"
    }
