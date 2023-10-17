package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.exists
import kotlinx.browser.document

fun main() {
    val context = ViewContext(document.body!!)
    console.log(document.body!!::exists)
    context.app()
}
