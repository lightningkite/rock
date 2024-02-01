package com.lightningkite.rock.views.direct

import com.lightningkite.rock.Blob
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.url.URL

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImage = HTMLImageElement

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit): Unit =
    themedElement<NImage>("img") { classList.add("tooltip"); setup(Image(this)) }

actual inline var Image.source: ImageSource
    get() = TODO()
    set(value) {
        when (value) {
            is ImageRemote -> native.src = value.url
            is ImageRaw -> native.src = URL.createObjectURL(Blob(arrayOf(value.data)))
            is ImageResource -> native.src = value.relativeUrl
            is ImageLocal -> native.src = URL.createObjectURL(value.file)
            is ImageVector -> native.src = value.toWeb()
            else -> {}
        }
    }
actual inline var Image.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        native.style.objectFit = when (value) {
            ImageScaleType.Fit -> "contain"
            ImageScaleType.Crop -> "cover"
            ImageScaleType.Stretch -> "fill"
            ImageScaleType.NoScale -> "none"
        }
    }
actual inline var Image.description: String?
    get() = native.alt
    set(value) {
        native.alt = value ?: ""
    }