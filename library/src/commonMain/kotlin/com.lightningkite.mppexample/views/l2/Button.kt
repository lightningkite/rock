package com.lightningkite.mppexample

enum class ButtonVariant { Unstyled, Contained, Outlined, Text }
enum class ButtonSize { Small, Medium, Large }
enum class ButtonPalette { Primary, Danger }

data class ButtonOptions(
    val variant: ButtonVariant = ButtonVariant.Contained,
    val size: ButtonSize = ButtonSize.Medium,
    val palette: ButtonPalette = ButtonPalette.Primary,
)

fun ViewContext.button(
    buttonSetup: NativeButton.() -> Unit,
) {
    nativeButton {
        withTheme(theme.primaryTheme()) {
            buttonSetup()
        }
    } in padding(
        Insets.symmetric(
            horizontal = 12.px,
            vertical = 8.px
        )
    ) in buttonLike(theme.primary.background.closestColor())
}

//TODO handle theming for disabled button. consider adding more to the PaintPair class to hold foreground/background for disabled state
