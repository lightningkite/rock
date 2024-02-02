package com.lightningkite.rock.views.l2

import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.consumeInputEvents
import com.lightningkite.rock.views.direct.marginless
import com.lightningkite.rock.views.direct.swapView
import com.lightningkite.rock.views.direct.swapViewDialog
import com.lightningkite.rock.views.direct.swapping
import com.lightningkite.rock.views.navigator

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
        native.consumeInputEvents()
        this@swapViewDialog.swapping(
            current = { n.dialog.currentScreen.await() },
            views = { screen ->
                this.navigator = n.dialog
                if (screen != null)
                    with(screen) { render() }
            }
        )
    } in marginless
}
