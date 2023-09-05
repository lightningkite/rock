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

        val screen = Property<RockScreen>(PasswordRecovery())

        launch {
            window.setTimeout({
                screen set NotFound()
            }, 1000)
        }

        withTheme(theme) {
            column {
                h1 {
                    content = "Hello, world!"
                } in background(Color.fromHex(0xeeeeee))

                box {
                    routerView(
                        Router(
                            routes = listOf(
                                Route(Login.PATH) { _, params -> Login(email = params["email"]) },
                                Route(NotFound.PATH) { _, _ -> NotFound() },
                                Route(Register.PATH) { _, _ -> Register() },
                                Route(PasswordRecovery.PATH) { _, _ -> PasswordRecovery() },
                                Route(Dashboard.PATH) { _, _ -> Dashboard() }
                            ),
                            fallback = NotFound()
                        )
                    )
                } in background(Color.fromHex(0xfafafa)) in weight(1f)

                row {
                    gravity = StackGravity.Center

                    button(
                        onClick = { navigator.replace(PasswordRecovery()) }
                    ) {
                        text { content = "Home" }
                    }

                    space() in weight(1f)

                    text {
                        content = "Goodbye, world!"
                    }
                } in background(Color.fromHex(0xeeeeee))
            }
        }
    }
}

