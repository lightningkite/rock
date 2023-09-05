package com.lightningkite.mppexample

import org.w3c.dom.HTMLImageElement
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Image = HTMLImageElement

actual inline fun ViewContext.image(setup: Image.() -> Unit): Unit = element<HTMLImageElement>("img") {
    scaleType = ImageMode.Fit
    if (style.getPropertyValue("width") == "")
        style.width = "inherit"
    if (style.getPropertyValue("height") == "")
        style.height = "inherit"
    setup()
}

@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toBase64(): String =
    Base64.encode(this)

actual var Image.source: ImageSource
    get() = throw NotImplementedError()
    set(value) {
        if (value is ImageRemote)
            src = value.url
        else if (value is ImageRaw)
            src = value.data.toBase64()
        else if (value is ImageResource)
            throw NotImplementedError()
        else if (value is ImageVector)
            throw NotImplementedError()
    }

actual var Image.scaleType: ImageMode
    get() = throw NotImplementedError()
    set(value) {
        style.objectFit = when (value) {
            ImageMode.Fit -> "contain"
            ImageMode.Crop -> "cover"
            ImageMode.Stretch -> "fill"
            ImageMode.NoScale -> "none"
        }
    }
