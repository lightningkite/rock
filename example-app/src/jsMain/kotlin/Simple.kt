package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.hasPopover
import com.lightningkite.rock.views.direct.onlyWhen
import com.lightningkite.rock.views.direct.text
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
