package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewContext
import kotlinx.browser.document

fun main() {
    val context = ViewContext(document.body!!)
    context.componentDemo()
}
