package com.lightningkite.rock.models

import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

private val measuringDiv = (document.createElement("div") as HTMLDivElement).apply {
    id = "____--measuringDiv"
    style.height = "0"
    style.width = "0"
    style.position = "absolute"
    style.outline = "none"
    style.border = "none"
    style.padding = "none"
    style.margin = "none"
    style.boxSizing = "content-box"
    document.body!!.appendChild(this)
}
actual fun Dimension.compareToImpl(other: Dimension): Int = value.cssCalc().compareTo(other.value.cssCalc())

private fun String.cssCalc(): Int {
    measuringDiv.style.height = this
    return measuringDiv.offsetHeight
}