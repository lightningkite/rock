package com.lightningkite.rock.views.l2

import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

fun ViewWriter.navigatorView(navigator: RockNavigator) {
    this.swapView {
        this@swapView.swapping(
            current = { navigator.currentScreen.await() },
            views = { screen ->
                this.navigator = navigator
                if (screen != null)
                    with(screen) { render() }
            }
        )
    }
}

fun ViewWriter.navigatorViewDialog() {
    val n = navigator
    this.swapViewDialog {
        this@swapViewDialog.swapping(
            current = { n.dialog.currentScreen.await() },
            views = { screen ->
                this.navigator = n.dialog
                if (screen != null)
                    with(screen) { render() }
            }
        )
    }
}
