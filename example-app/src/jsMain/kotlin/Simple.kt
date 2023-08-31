package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.document

fun main() {
    val context = ViewContext(document.body!!)
    context.run {
        MyApp().run {
            render()
        }
    }
}
