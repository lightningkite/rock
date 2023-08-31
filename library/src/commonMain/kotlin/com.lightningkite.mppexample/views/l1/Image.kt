package com.lightningkite.mppexample


expect class Image : NView

expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource
expect var Image.scaleType: ImageMode
