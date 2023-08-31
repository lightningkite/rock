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
                nativeText { content = "Not found" }
            },
            theme = Theme(
                titleFont = systemDefaultFont,
                bodyFont = systemDefaultFont,
                baseSize = 18.0,
                normal = PaintPair(
                    foreground = Color.black,
                    background = Color.white
                ),
                primary = PaintPair(
                    foreground = Color.white,
                    background = Color.fromHex(0x1566B7)
                ),
                accent = PaintPair(
                    foreground = Color.white,
                    background = Color.green
                )
            )
        )
    }
}

