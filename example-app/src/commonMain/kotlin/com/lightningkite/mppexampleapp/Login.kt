package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Login : RockScreen {
    override fun ViewContext.render() {
        val email = Property("")

        column {
            image {
                source =
                    ImageRemote(url = "https://mobileapp.vandenbussche.com/static/media/logo-full.fa76a651.png")
            } in sizedBox(
                SizeConstraints(
                    maxWidth = 320.px
                )
            ) in alignCenter()

            textField(
                label = { "Email" },
                text = email,
            )

            textField(
                keyboardHints = KeyboardHints(
                    autocomplete = AutoComplete.Password,
                    case = KeyboardCase.None
                ),
                label = { "Password" },
                text = Property(""),
            )

            buttonGroup(
                disabled = { false },
                buttons = listOf(
                    ButtonGroupItem(
                        text = "Login",
                        onClick = {
                            println("Login")
                        }
                    ),
                    ButtonGroupItem(
                        text = "Register",
                        onClick = {
                            println("Register")
                        }
                    ),
                    ButtonGroupItem(
                        text = "Forgot Password",
                        onClick = {
                            println("Forgot Password")
                        }
                    ),
                )
            ) in alignRight()

//            button(
//                onClick = {},
//            ) {
//                text { content = "Login" }
//            } in alignRight()
        }
    }

    override fun createPath(): String {
        return PATH
    }

    companion object {
        const val PATH = "/screenA"
    }

}