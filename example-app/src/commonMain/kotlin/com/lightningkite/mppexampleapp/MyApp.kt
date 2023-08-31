package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class MyApp : RockApp {
    override fun ViewContext.render() {
        Router(
            context = this,
            routes = listOf(
                Route(HomeScreen.PATH) { HomeScreen() },
                Route(TestComponent.PATH) { TestComponent() },
                Route(TestComponent.PATH) { TestComponent() },
                Route(UserScreen.PATH) { UserScreen.create(it) },
                Route(UserSettings.PATH) { UserSettings.create(it) },
            ),
            fallback = {
                text { content = "Not found" }
            }
        )
    }
}

