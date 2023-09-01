package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.browser.window

class MyApp : RockApp {
    override fun ViewContext.render() {
        val theme = Theme(
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
                background = Color.fromHex(0x9C27B0),
            ),
            normalDisabled = PaintPair(
                foreground = Color.fromHex(0x999999),
                background = Color.white
            ),
            primaryDisabled = PaintPair(
                foreground = Color.fromHex(0xededed),
                background = Color.fromHex(0x666666)
            ),
            accentDisabled = PaintPair(
                foreground = Color.fromHex(0x999999),
                background = Color.green
            ),
        )

        val screen = Property<RockScreen>(HomeScreen())

        launch {
            window.setTimeout({
                screen set TestComponent()
            }, 1000)
        }

        withTheme(theme) {
            column {
                text(level = HeadingLevel.H1) {
                    content = "Hello, world!"
                } in nativeBackground(Color.fromHex(0xeeeeee)) in padding(1.rem)

                box {
                    swapView(screen)
//                    routerView {
//                        println("Creating router")
//                        Router(
//                            context = this@render,
//                            routes = listOf(
//                                Route(HomeScreen.PATH) { HomeScreen() },
//                                Route(TestComponent.PATH) { TestComponent() },
//                                Route(TestComponent.PATH) { TestComponent() },
//                                Route(UserScreen.PATH) { UserScreen.create(it) },
//                                Route(UserSettings.PATH) { UserSettings.create(it) },
//                            ),
//                            fallback = {
//                                nativeText { content = "Not found" }
//                            },
//                            theme = theme
//                        )
//                    }
                } in padding(Insets.symmetric(vertical = 2.5.rem)) in weight(1f)

                row {
                    gravity = StackGravity.Center

                    button(
                        onClick = {
                            navigator.navigate(TestComponent())
                        }
                    ) {
                        text { content = "Test" }
                    }

                    text {
                        content = "Demo footer"
                    }

                    space() in weight(1f)

                    text {
                        content = "Goodbye, world!"
                    }
                } in nativeBackground(Color.fromHex(0xeeeeee)) in padding(1.rem)

            }
        }
    }
}

