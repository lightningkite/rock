package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("sample/login")
object SampleLogInScreen : RockScreen {
    override fun ViewContext.render() {
        stack {
            image {
                source = ImageRemote("https://picsum.photos/seed/login/1080/1920")
                scaleType = ImageMode.Crop
                alpha = 0.5
            } in bordering
            col {
                space {} in weight(1f)
                col {
                    h1 { content = "My App" }
                    label {
                        content = "Email"
                        textField {
                            keyboardHints = KeyboardHints.email
                        }
                    }
                    label {
                        content = "Password"
                        textField {
                            keyboardHints = KeyboardHints.password
                        }
                    }
                    button {
                        h6 { content = "Log In" }
                        onClick {
                            navigator.navigate(ControlsScreen)
                        }
                    } in important
                } in card in sizedBox(SizeConstraints(maxWidth = 50.rem))
                space {} in weight(1f)
            } in scrolls() in withPadding
        } in bordering
    }
}