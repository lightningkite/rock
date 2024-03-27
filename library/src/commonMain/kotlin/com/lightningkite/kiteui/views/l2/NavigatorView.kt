package com.lightningkite.kiteui.views.l2

import com.lightningkite.kiteui.contains
import com.lightningkite.kiteui.navigation.KiteUiNavigator
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.consumeInputEvents
import com.lightningkite.kiteui.views.direct.marginless
import com.lightningkite.kiteui.views.direct.swapView
import com.lightningkite.kiteui.views.direct.swapViewDialog
import com.lightningkite.kiteui.views.direct.swapping
import com.lightningkite.kiteui.views.ignoreInteraction
import com.lightningkite.kiteui.views.navigator

fun ViewWriter.navigatorView(navigator: KiteUiNavigator) {
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
        ignoreInteraction = true
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
