package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.navigatorView

val appTheme = Property<Theme>(MaterialLikeTheme())
fun ViewContext.app() {
    val navigator = PlatformNavigator(AutoRoutes)
    this.navigator = navigator

    col {
        row {
            h2 { content = "Top Bar Example" } in weight(1f) in gravity(Align.Center, Align.Center)
            button { image { this.source = Icons.search.color(Color.white) } }
        } in important in bordering
        navigatorView(navigator) in weight(1f)
    } in setTheme { appTheme.current } in bordering
}

