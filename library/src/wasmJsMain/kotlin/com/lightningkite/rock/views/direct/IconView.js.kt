package com.lightningkite.rock.views.direct

import com.lightningkite.rock.Blob
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import kotlinx.dom.addClass
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.get
import org.w3c.dom.svg.SVGElement
import org.w3c.dom.svg.SVGSVGElement
import org.w3c.dom.url.URL

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NIconView(override val js: HTMLDivElement): NView2<HTMLDivElement>() {
    var icon: Icon? = null
}

@ViewDsl
actual inline fun ViewWriter.iconActual(crossinline setup: IconView.() -> Unit): Unit =
    themedElement("div", ::NIconView) {
        this.js.setAttribute("role", "img")
        js.addClass("viewDraws")
        js.addClass("icon")
        setup(IconView(this))
    }

actual var IconView.source: Icon?
    get() = native.icon
    set(value) {
        native.icon = value
        native.js.innerHTML = ""
        value?.let {
            native.js.renderSvgIcon(value)
        }
    }

actual inline var IconView.description: String?
    get() = (native.js.firstElementChild as SVGSVGElement).getElementsByTagName("title").get(0)?.innerHTML
    set(value) {
        (native.js.firstElementChild as? SVGSVGElement)?.getElementsByTagName("title")?.get(0)?.let {
            it.innerHTML = value ?: ""
        } ?: (native.js.firstElementChild as? SVGSVGElement)?.let {
            it.appendElement("title") {
                innerHTML = value ?: ""
            }
        }
    }
