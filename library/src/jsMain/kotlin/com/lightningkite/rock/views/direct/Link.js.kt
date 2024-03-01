package com.lightningkite.rock.views.direct

import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.render
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.navigator
import org.w3c.dom.HTMLAnchorElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = HTMLAnchorElement

@ViewDsl
actual inline fun ViewWriter.linkActual(crossinline setup: Link.() -> Unit): Unit = themedElementClickable<NLink>("a") {
    this.asDynamic().__ROCK__navigator = navigator
    classList.add("rock-stack")
    setup(Link(this))
}

actual inline var Link.to: RockScreen
    get() = this.native.asDynamic().__ROCK__screen as RockScreen
    set(value) {
        this.native.asDynamic().__ROCK__screen = value
        val navigator = (this.native.asDynamic().__ROCK__navigator as RockNavigator)
        navigator.routes.render(value)?.let {
            native.href = PlatformNavigator.basePath + it.urlLikePath.render()
        }
        native.onclick = {
            it.preventDefault()
            navigator.navigate(value)
        }
    }
actual inline var Link.newTab: Boolean
    get() = native.target == "_blank"
    set(value) {
        native.target = if (value) "_blank" else "_self"
    }