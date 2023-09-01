package com.lightningkite.mppexample

fun ViewContext.activityIndicator(visible: Property<Boolean>? = null, exists: Property<Boolean>? = null) {
    nativeActivityIndicator {
        if (exists != null)
            ::exists { exists.current }
        if (visible != null)
            ::visible { visible.current }

        val context = renderContext
        if (context is ButtonRenderContext) {
            color = theme.normal.foreground.closestColor()

            val size = when (context.size) {
                ButtonSize.Small -> 16.px
                ButtonSize.Medium -> 24.px
                ButtonSize.Large -> 32.px
            }
            width = size
            height = size
            lineWidth = when (context.size) {
                ButtonSize.Small -> 1.px
                ButtonSize.Medium -> 2.px
                ButtonSize.Large -> 4.px
            }
        } else {
            color = theme.primary.background.closestColor()
            lineWidth = 8.px
        }
    }
}

