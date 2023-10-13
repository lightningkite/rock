package com.lightningkite.rock.views.old

import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias Link = HTMLAnchorElement

@ViewDsl
actual inline fun ViewContext.link(setup: Link.() -> Unit): Unit = box {
    element<HTMLAnchorElement>("a") {
        setup()
    }
}

actual var Link.to: RockScreen
    get() = throw NotImplementedError()
    set(value) {
        this.setAttribute("href", value.createPath())
    }

actual var Link.content: String
    get() = throw NotImplementedError()
    set(value) {
        innerText = value
    }
