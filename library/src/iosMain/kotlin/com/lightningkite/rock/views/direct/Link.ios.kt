package com.lightningkite.rock.views.direct

import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.navigator

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = NativeLink

@ViewDsl
actual inline fun ViewWriter.linkActual(crossinline setup: Link.() -> Unit): Unit = element(NativeLink()) {
    handleThemeControl(this) {
        setup(Link(this))
        onNavigator = navigator
    }
}

actual inline var Link.to: RockScreen
    get() = native.toScreen ?: RockScreen.Empty
    set(value) {
        native.toScreen = value
    }
actual inline var Link.newTab: Boolean
    get() = native.newTab
    set(value) {
        native.newTab = value
    }
actual fun Link.onNavigate(action: suspend () -> Unit): Unit {
    native.onNavigate = action
}