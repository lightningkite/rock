package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.fetch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("sample/login")
object SampleLogInScreen : RockScreen {
    override fun ViewWriter.render() {
        val email = Property("")
        val password = Property("")
        stack {
            spacing = 0.rem
            image {
                source = Resources.imagesSolera
                scaleType = ImageScaleType.Crop
                opacity = 0.5
            } 
            col {
                space {} in weight(1f)
                col {
                    h1 { content = "My App" }
                    label {
                        content = "Email"
                        textField {
                            keyboardHints = KeyboardHints.email
                            content bind email
                        }
                    }
                    label {
                        content = "Password"
                        textField {
                            keyboardHints = KeyboardHints.password
                            content bind password
                            action = Action(
                                title = "Log In",
                                icon = Icon.login,
                                onSelect = {
                                    fakeLogin(email)
                                }
                            )
                        }
                    }
                    button {
                        h6 { content = "Log In" }
                        onClick {
                            delay(1000)
                            fakeLogin(email)
                        }
                    } in important
                } in card in sizedBox(SizeConstraints(maxWidth = 50.rem)) in gravity(Align.Center, Align.Center)
                space {} in weight(1f)
            } in scrolls in padded
        } 
    }

    private suspend fun ViewWriter.fakeLogin(email: Property<String>) {
        fetch("fake-login/${email.await()}")
        navigator.navigate(ControlsScreen)
    }
}