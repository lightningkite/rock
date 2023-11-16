package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewWriter
import kotlinx.browser.document

fun main() {
    val context = ViewWriter(document.body!!)
    context.app()
}
