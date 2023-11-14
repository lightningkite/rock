package com.lightningkite.rock.views.l2

import com.lightningkite.rock.models.ScreenTransition
import com.lightningkite.rock.models.ScreenTransitions
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

fun ViewContext.navigatorView(navigator: RockNavigator) {
    this.swapView {
        val fork = split()
        fork.navigator = navigator
        reactiveScope {
            val screen = navigator.currentScreen.await()
            this@swapView.swap {
                if (screen != null)
                    with(screen) { fork.render() }
            }
        }
    }
}

fun ViewContext.navigatorViewDialog() {
    this.swapViewDialog {
        val fork = split()
        val navigator = navigator.dialog
        fork.navigator = navigator
        reactiveScope {
            println("Getting current screen")
            val screen = navigator.currentScreen.await()
            println("Current screen is $screen")
            this@swapViewDialog.swap {
                println("Swapping in $screen")
                if (screen != null)
                    with(screen) { fork.render() }
            }
        }
    }
}
