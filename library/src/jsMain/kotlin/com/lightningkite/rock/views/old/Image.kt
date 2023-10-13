package com.lightningkite.rock.views.old

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.HTMLImageElement
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Image = HTMLImageElement

@ViewDsl
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
        else if (value is ImageVector) {
            src = value.toWeb()
            style.width = value.width.value
            style.height = value.height.value
        }
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

fun ImageVector.toWeb(): String {
    return buildString {
        append("data:image/svg+xml;utf8,<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"${width.value}\" height=\"${height.value}\" viewBox=\"$viewBoxMinX $viewBoxMinY $viewBoxWidth $viewBoxHeight\">")
        paths.forEach { path ->
            append(
                "<path d=\"${path.path}\" stroke=\"${path.strokeColor?.toWeb() ?: Color.transparent.toWeb()}\" stroke-width=\"${path.strokeWidth ?: 0}\" fill=\"${
                    (path.fillColor ?: Color.transparent).closestColor().toWeb()
                }\"/>"
            )
        }
        append("</svg>")
    }
}
