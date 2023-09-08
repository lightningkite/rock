package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.activityIndicator(
    visible: Readable<Boolean>? = null, exists: Readable<Boolean>? = null,
    lineWidth: Dimension = 4.px,
    width: Dimension = 32.px,
    height: Dimension = 32.px,
    color: Color? = null
) {
    nativeActivityIndicator {
        if (exists != null)
            ::exists { exists.current }
        if (visible != null)
            ::visible { visible.current }

        this.color = color ?: theme.primary.background.closestColor()
        this.width = width
        this.height = height
        this.lineWidth = lineWidth
    }
}

