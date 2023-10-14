package com.lightningkite.rock

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.navigation.Router
import com.lightningkite.rock.reactive.ReactiveScope
import com.lightningkite.rock.views.ViewContext


fun ViewContext.navigationView(
    router: Router,
    tabs: ReactiveScope.() -> List<NavigationTab>,
    showNavigation: ReactiveScope.() -> Boolean
) {
    column {
        box {
            routerView(router)
        } in background(paint = Color.fromHex(0xfafafa), padding = Insets.none) in weight(1f)

        tabLayout(
            tabs = tabs,
            exists = showNavigation
        )
    } in fullWidth() in fullHeight()
}
