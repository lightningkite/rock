package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement

fun main() {
    val context = ViewContext(document.body!!)
    context.elementToDoList.add {
        style.position = "absolute"
        style.left = "0px"
        style.top = "0px"
        style.right = "0px"
        style.bottom = "0px"
    }
    context.elementTest()
}
