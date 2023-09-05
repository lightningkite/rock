package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Login(
    private val email: String? = null,
) : RockScreen {
    override fun ViewContext.render() {
        val email = Property(email ?: "")
        val password = Property("")
        val error = Property<String?>(null)

        column {
            image {
                source = ImageRemote(url = "https://mobileapp.vandenbussche.com/static/media/logo-full.fa76a651.png")
            } in sizedBox(
                SizeConstraints(
                    maxWidth = 320.px
                )
            ) in alignCenter()
            successAlert(content = { if (this@Login.email == null) null else "Successfully created account! You can now sign in." })

            textField(
                label = { "Email" },
                text = email,
            )

            textField(
                keyboardHints = KeyboardHints(
                    autocomplete = AutoComplete.Password, case = KeyboardCase.None
                ),
                label = { "Password" },
                text = password,
            )

            buttonGroup(
                disabled = { false }, buttons = listOf(
                    ButtonGroupItem(text = "Login", onClick = {
                        if (email.once == "") {
                            error set "Please enter your email"
                        } else if (password.once == "") {
                            error set "Please enter your password"
                        } else {
                            error set null
                            // delay 1000 ms
                            val response = fetch("https://swapi.dev/api/people/1")
                            if (response.ok) {
                                navigator.navigate(Dashboard())
                            } else {
                                error set "Invalid email or password"
                            }
                        }
                    }),
                    ButtonGroupItem(text = "Register", onClick = {
                        navigator.navigate(Register())
                    }),
                    ButtonGroupItem(text = "Forgot Password", onClick = {
                        navigator.navigate(PasswordRecovery())
                    }),
                )
            )
            errorAlert(content = { error.current })
        }
    }

    override fun createPath(): String {
        return if (email == null) PATH else "$PATH?email=$email"
    }

    companion object {
        const val PATH = "/"
    }

}