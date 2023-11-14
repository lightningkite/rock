package com.lightningkite.rock.views.l3

//import com.lightningkite.rock.models.Color
//import com.lightningkite.rock.navigation.Router
//import com.lightningkite.rock.reactive.ReactiveScope
//import com.lightningkite.rock.views.ViewContext
//
//
//fun ViewContext.navigationView(
//    router: Router,
//    tabs: suspend () -> List<NavigationTab>,
//    showNavigation: suspend () -> Boolean
//) {
//    column {
//        box {
//            routerView(router)
//        } in background(paint = Color.fromHex(0xfafafa), padding = Insets.none) in weight(1f)
//
//        tabLayout(
//            tabs = tabs,
//            exists = showNavigation
//        )
//    } in fullWidth() in fullHeight()
//}
