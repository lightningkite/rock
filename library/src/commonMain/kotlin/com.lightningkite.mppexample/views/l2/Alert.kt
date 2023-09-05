package com.lightningkite.mppexample

fun ViewContext.alert(
    content: ReactiveScope.() -> String?
) {
    box {
        ::exists { content() != null }
        text {
            ::content { content() ?: "" }
        }
    } in background(
        background = Background(
            fill = theme.normal.background,
            corners = CornerRadii(8.px)
        ),
        elevation = 2.px,
    ) in margin(Insets.symmetric(vertical = 12.px))
}

fun ViewContext.errorAlert(
    content: ReactiveScope.() -> String?
) = withTheme(theme.dangerTheme()) {
    alert(content)
}

fun ViewContext.successAlert(
    content: ReactiveScope.() -> String?
) = withTheme(theme.primaryTheme()) {
    alert(content)
}
