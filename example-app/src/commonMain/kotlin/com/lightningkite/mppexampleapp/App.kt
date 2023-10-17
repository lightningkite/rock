package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.navigatorView

val appTheme = Property<Theme>(MaterialLikeTheme())
fun ViewContext.app() {
    val navigator = PlatformNavigator(AutoRoutes)
    this.navigator = navigator

    col {
        row {
            button {
                image { this.source = Icons.arrowBack.color(Color.white); description = "Go Back" }
                onClick { navigator.goBack() }
            }
            h2 { ::content { navigator.currentScreen.current.title.current } } in weight(1f) in gravity(Align.Center, Align.Center)
            externalLink {
                image { this.source = Icons.search.color(Color.white); description = "See Code on GitHub"; newTab = true }
                ::to { "https://github.com/lightningkite/rock/tree/main/example-app/src/commonMain/kotlin/com/lightningkite/mppexampleapp/${navigator.currentScreen.current::class.toString().removePrefix("class ")}.kt" }
            }
        } in important in bordering
        navigatorView(navigator) in weight(1f) in bordering
    } in setTheme { appTheme.current } in bordering
}

