package com.lightningkite.mppexample

fun ViewContext.activityIndicator(exists: Property<Boolean>?) {
    nativeActivityIndicator {
        if (exists != null)
            ::exists { exists.current }
        if (renderContext == RenderContext.Button) {
            color = theme.normal.foreground.closestColor()
            width = 32.px
            height = 32.px
            lineWidth = 4.px
        } else {
            color = theme.primary.background.closestColor()
            lineWidth = 8.px
        }
    }
}

