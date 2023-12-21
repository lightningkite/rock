package com.lightningkite.rock.views.l2

import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

fun ViewWriter.navigatorView(navigator: RockNavigator) {
    this.swapView {
        val fork = split()
        fork.navigator = navigator
        reactiveScope {
            val screen = navigator.currentScreen.await()
            println("Moving screens to $screen")
            this@swapView.swap {
                if (screen != null)
                    with(screen) { fork.render() }
            }
        }
    }
}

fun ViewWriter.navigatorViewDialog() {
    this.swapViewDialog {
        val fork = split()
        val navigator = navigator.dialog
        fork.navigator = navigator
        reactiveScope {
            val screen = navigator.currentScreen.await()
            this@swapViewDialog.swap {
                if (screen != null)
                    with(screen) { fork.render() }
            }
        }
    }
}
