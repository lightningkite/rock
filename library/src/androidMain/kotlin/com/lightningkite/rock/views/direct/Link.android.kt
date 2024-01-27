package com.lightningkite.rock.views.direct

import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.navigator
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = LinkFrameLayout

actual var Link.to: RockScreen
    get() {
        val rockScreen: RockScreen
        runBlocking {
            rockScreen = PlatformNavigator.currentScreen.await()!!
        }
        return rockScreen
    }
    set(value) {
        native.setOnClickListener {
            native.navigator.navigate(value)
        }
    }
actual var Link.newTab: Boolean
    get() {
        return false
    }
    set(value) {
        Timber.d("New Tab called with value $value")
    }

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit) {
    return viewElement(factory = ::LinkFrameLayout, wrapper = ::Link) {
        native.navigator = navigator
        // OnClickListener may not be set until after handleTheme() is called, so we must manually set isClickable for
        // the RippleDrawable to be added to the background
        native.isClickable = true
        handleTheme(native, viewDraws = false)
        setup(this)
    }
}
