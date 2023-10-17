package com.lightningkite.rock.views.l2

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.models.ScreenTransitions
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

fun ViewContext.navigatorView(navigator: RockNavigator) {
    this.swapView {
        val fork = split()
        fork.navigator = navigator
        reactiveScope {
            val screen = navigator.currentScreen.current
            this@swapView.swap {
                with(screen) { fork.render() }
            }
        }
    }
}