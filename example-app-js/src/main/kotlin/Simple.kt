package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement

fun main() {
    val context = ViewContext(document.body!!)
    context.asyncTest()
}
