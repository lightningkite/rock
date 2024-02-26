package com.lightningkite.rock.models

import com.lightningkite.rock.dom.HTMLElement
import kotlinx.browser.document
import kotlinx.dom.appendElement
import kotlinx.dom.createElement
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGPathElement
import org.w3c.dom.svg.SVGSVGElement
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


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

fun HTMLElement.renderSvgIcon(svg: Icon) {
    appendElementSvg<SVGSVGElement>("svg") {
        this.style.width = svg.width.value
        this.style.height = svg.height.value
        this.style.setProperty("fill", "currentColor")
        this.setAttribute("viewBox", svg.run { "$viewBoxMinX $viewBoxMinY $viewBoxWidth $viewBoxHeight" })
        for(path in svg.pathDatas) {
            appendElementSvg<SVGPathElement>("path") {
                this.setAttribute("d", path)
            }
        }
    }
//    return buildString {
//        append("data:image/svg+xml;utf8,<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
//        append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"${width.value}\" height=\"${height.value}\" viewBox=\"$viewBoxMinX $viewBoxMinY $viewBoxWidth $viewBoxHeight\">")
//        paths.forEach { path ->
//            append(
//                "<path d=\"${path.path}\" stroke=\"${path.strokeColor?.toWeb() ?: Color.transparent.toWeb()}\" stroke-width=\"${path.strokeWidth ?: 0}\" fill=\"${
//                    (path.fillColor ?: Color.transparent).closestColor().toWeb()
//                }\"/>"
//            )
//        }
//        append("</svg>")
//    }
}
@OptIn(ExperimentalContracts::class)
@SinceKotlin("1.4")
public fun <T: Element> Element.appendElementSvg(name: String, init: T.() -> Unit): T {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    val element = ownerDocument!!.createElementNS("http://www.w3.org/2000/svg", name) as T
    init(element)
    appendChild(element)
    return element
}

