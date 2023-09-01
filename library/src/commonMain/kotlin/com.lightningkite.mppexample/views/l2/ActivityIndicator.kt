package com.lightningkite.mppexample

fun ViewContext.activityIndicator() {
    nativeActivityIndicator {
        if (renderContext == RenderContext.Button) {
            color = theme.normal.foreground.closestColor()
            width = 36.px
            height = 36.px
            lineWidth = 4.px
        } else {
            color = theme.primary.background.closestColor()
            lineWidth = 8.px
        }
    }
}
