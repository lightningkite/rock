package com.lightningkite.rock.views.direct

import com.lightningkite.rock.JsAnyNativeDelegate
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.render
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import com.lightningkite.rock.views.navigator
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.Navigator

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NLink(override val js: HTMLAnchorElement): NView2<HTMLAnchorElement>() {
    var navigator: RockNavigator? = null
    var screen: RockScreen? = null
    var onNavigate: (suspend ()->Unit)? = null
}

@ViewDsl
actual inline fun ViewWriter.linkActual(crossinline setup: Link.() -> Unit): Unit = themedElementClickable("a", ::NLink) {
    this.navigator = this@linkActual.navigator
    js.classList.add("rock-stack")
    setup(Link(this))
}

actual inline var Link.to: RockScreen
    get() = this.native.screen ?: RockScreen.Empty
    set(value) {
        this.native.screen = value
        val navigator = native.navigator!!
        navigator.routes.render(value)?.let {
            native.js.href = PlatformNavigator.basePath + it.urlLikePath.render()
        }
        native.js.onclick = {
            it.preventDefault()
            navigator.navigate(value)
            (native.onNavigate)?.let {
                calculationContext.launchManualCancel(it)
            }
        }
    }
actual inline var Link.newTab: Boolean
    get() = native.js.target == "_blank"
    set(value) {
        native.js.target = if (value) "_blank" else "_self"
    }
actual fun Link.onNavigate(action: suspend () -> Unit): Unit {
    native.onNavigate = action
}