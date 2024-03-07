package com.lightningkite.rock.views.direct

import com.lightningkite.rock.Blob
import com.lightningkite.rock.clockMillis
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.get
import org.w3c.dom.url.URL

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageView = HTMLDivElement

@ViewDsl
actual inline fun ViewWriter.imageActual(crossinline setup: ImageView.() -> Unit): Unit =
    themedElement<HTMLDivElement>("div") {
        addClass("viewDraws", "swapImage")
        setup(ImageView(this))
    }

actual inline var ImageView.source: ImageSource?
    get() = TODO()
    set(value) {
        when (value) {
            null -> setSrc("")
            is ImageRemote -> {
                setSrc(value.url)
            }
            is ImageRaw -> {
                setSrc(URL.createObjectURL(Blob(arrayOf(value.data))))
            }
            is ImageResource -> {
                setSrc(PlatformNavigator.basePath + value.relativeUrl)
            }
            is ImageLocal -> {
                setSrc(URL.createObjectURL(value.file))
            }
            is ImageVector -> {
                setSrc(value.toWeb())
            }
            else -> {}
        }
    }
fun ImageView.setSrc(url: String) {
    if(!animationsEnabled) {
        native.innerHTML = ""
    }
    if(url.isBlank()) {
        if(animationsEnabled) {
            val children = (0..<native.children.length).mapNotNull { native.children[it] }
            children.forEach {
                (it as? HTMLElement)?.style?.opacity = "0"
                window.setTimeout({
                    native.removeChild(it)
                }, 150)
            }
        }
        return
    }

    if (native.children.length > 0) {
        val exists = native.children[0] as HTMLImageElement
        exists.src = url
        return
    }

    val newElement = document.createElement("img") as HTMLImageElement
    newElement.style.opacity = "0"
    val now = clockMillis()
    newElement.onload = {
        val children = (0..<native.children.length).mapNotNull { native.children[it] }
        if((clockMillis() - now).also { println("Image load for $url took $it ms") } < 32 && children.isEmpty()) {
            // disable animations and get it done; no reason to show the user an animation
            newElement.withoutAnimation {
                newElement.style.opacity = "1"
            }
        } else {
            newElement.style.opacity = "1"
            var found = false
            children.forEach {
                if(it === newElement)
                    found = true
                else if(found) {
                    (it as? HTMLElement)?.style?.opacity = "0"
                    window.setTimeout({
                        native.removeChild(it)
                    }, 150)
                }
            }
        }
    }
    newElement.src = url
    native.appendChild(newElement)
}
actual inline var ImageView.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        native.className = native.className.split(' ').filter { !it.startsWith("scaleType-") }.plus("scaleType-$value").joinToString(" ")
    }
actual inline var ImageView.description: String?
    get() = native.getAttribute("aria-label")
    set(value) {
        native.setAttribute("aria-label", value ?: "")
    }

@ViewDsl
actual inline fun ViewWriter.zoomableImageActual(crossinline setup: ImageView.() -> Unit) {
    // TODO
    val wrapper: ImageView.() -> Unit = {
        setup()
        scaleType = ImageScaleType.Fit
    }

    image(wrapper)
}