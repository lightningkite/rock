package com.lightningkite.mppexample

expect class Image : NView

@ViewDsl
expect fun ViewContext.image(setup: Image.() -> Unit = {}): Unit

expect var Image.source: ImageSource
expect var Image.scaleType: ImageMode
