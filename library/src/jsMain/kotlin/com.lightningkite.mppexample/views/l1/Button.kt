package com.lightningkite.mppexample

import org.w3c.dom.*


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Button = HTMLButtonElement

actual inline fun ViewContext.button(setup: Button.() -> Unit): Unit = element<HTMLButtonElement>("button") {
    type = "submit" // may need to remove this and make it a variable
    style.display = "flex"
    style.flexDirection = "row"
    style.justifyContent = "center"
    style.alignItems = "center"
    style.height = "min-content"
    palette = ButtonPalette.Primary

//    text {
//        gravity = TextGravity.Center
//    }

    setup()
}

actual fun Button.onClick(action: () -> Unit) {
    addEventListener("click", {
        action()
    })
}

//actual var Button.content: String
//    get() = throw NotImplementedError()
//    set(value) {
//        val span = getElementsByTagName("span")[0] as HTMLSpanElement
//        span.content = value
//    }

actual var Button.variant: ButtonVariant
    get() = throw NotImplementedError()
    set(value) {
        className = when (value) {
            ButtonVariant.Unstyled -> ""
            ButtonVariant.Contained -> "rock-mui-button rock-mui-button-contained"
            ButtonVariant.Outlined -> "rock-mui-button rock-mui-button-outlined"
            ButtonVariant.Text -> "rock-mui-button rock-mui-button-text"
        }
    }

actual var Button.palette: ButtonPalette
    get() = throw NotImplementedError()
    set(value) {
        when (value) {
            ButtonPalette.Primary -> {
                style.setProperty("--rock-mui-button", "25, 118, 210")
                style.setProperty("--rock-mui-button-hover", "15, 108, 200")
                style.setProperty("--rock-mui-button-focus", "5, 98, 190")
            }

            ButtonPalette.Danger -> {
                style.setProperty("--rock-mui-button", "211, 47, 47")
                style.setProperty("--rock-mui-button-hover", "201, 37, 37")
                style.setProperty("--rock-mui-button-focus", "191, 27, 27")
            }
        }
    }

actual var Button.size: ButtonSize
    get() = throw NotImplementedError()
    set(value) {
        style.setProperty(
            "--rock-mui-button-size",
            when (value) {
                ButtonSize.Small -> "0.75"
                ButtonSize.Medium -> "1"
                ButtonSize.Large -> "1.25"
            }
        )
    }