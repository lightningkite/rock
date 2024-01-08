package com.lightningkite.rock.views.l2

import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

fun ViewWriter.navigatorView(navigator: RockNavigator) {
    this.swapView {
        reactiveScope {
            val screen = navigator.currentScreen.await()
            this@swapView.swap {
                this.navigator = navigator
                if (screen != null)
                    with(screen) { render() }
            }
        }
    }
}

fun ViewWriter.navigatorViewDialog() {
    val n = navigator
    this.swapViewDialog {
        reactiveScope {
            val screen = navigator.currentScreen.await()
            this@swapViewDialog.swap {
                this.navigator = n.dialog
                if (screen != null)
                    with(screen) { render() }
            }
        }
    }
}
