package com.lightningkite.rock.views.direct

import com.lightningkite.rock.Blob
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.dom.addClass
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.url.URL

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageView = HTMLImageElement

@ViewDsl
actual fun ViewWriter.imageActual(setup: ImageView.() -> Unit): Unit =
    themedElement<NImageView>("img") {
        addClass(".viewDraws")
        setup(ImageView(this))
    }

actual inline var ImageView.source: ImageSource?
    get() = TODO()
    set(value) {
        when (value) {
            null -> native.src = ""
            is ImageRemote -> native.src = value.url
            is ImageRaw -> native.src = URL.createObjectURL(Blob(arrayOf(value.data)))
            is ImageResource -> native.src = PlatformNavigator.basePath + value.relativeUrl
            is ImageLocal -> native.src = URL.createObjectURL(value.file)
            is ImageVector -> native.src = value.toWeb()
            else -> {}
        }
    }
actual inline var ImageView.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        native.style.objectFit = when (value) {
            ImageScaleType.Fit -> "contain"
            ImageScaleType.Crop -> "cover"
            ImageScaleType.Stretch -> "fill"
            ImageScaleType.NoScale -> "none"
        }
    }
actual inline var ImageView.description: String?
    get() = native.alt
    set(value) {
        native.alt = value ?: ""
    }

@ViewDsl
actual fun ViewWriter.zoomableImageActual(setup: ImageView.() -> Unit) {
    // TODO
    val wrapper: ImageView.() -> Unit = {
        setup()
        scaleType = ImageScaleType.Fit
    }

    image(wrapper)
}