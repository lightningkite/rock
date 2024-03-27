package com.lightningkite.mppexampleapp

import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.direct.col
import com.lightningkite.kiteui.views.direct.hasPopover
import com.lightningkite.kiteui.views.direct.onlyWhen
import com.lightningkite.kiteui.views.direct.text
import kotlinx.browser.document

fun main() {
    val context = ViewWriter(document.body!!)
    context.app()
//    with(context) {
//        col {
//            repeat(5) {
//                onlyWhen { true }
//                hasPopover { text("POPOVER") }
//                text("TEST")
//            }
//        }
//    }
}
