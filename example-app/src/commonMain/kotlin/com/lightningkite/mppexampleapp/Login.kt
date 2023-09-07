package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Login(
    private val email: String? = null,
    private val redirect: String? = null,
) : RockScreen {
    override fun ViewContext.render() {
        val email = Property(email ?: "")
        val password = Property("")
        val error = Property<String?>(null)

        if (currentUser.once != null)
            navigator.navigate(Dashboard())

        column {
            column {
                image {
                    source =
                        ImageRemote(url = "https://mobileapp.vandenbussche.com/static/media/logo-full.fa76a651.png")
                } in sizedBox(
                    SizeConstraints(
                        maxWidth = 320.px
                    )
                ) in alignCenter()
                successAlert(content = { if (this@Login.email == null) null else "Successfully created account! You can now sign in." })

                textField(
                    label = { "Email" },
                    text = email,
                    leadingIcon = ImageVector(
                        viewBoxWidth = 512,
                        viewBoxHeight = 512,
                        width = 18.px,
                        height = 18.px,
                        paths = listOf(
                            ImageVector.Path(
                                path = "M510.678,112.275c-2.308-11.626-7.463-22.265-14.662-31.054c-1.518-1.915-3.104-3.63-4.823-5.345 c-12.755-12.818-30.657-20.814-50.214-20.814H71.021c-19.557,0-37.395,7.996-50.21,20.814c-1.715,1.715-3.301,3.43-4.823,5.345 C8.785,90.009,3.63,100.649,1.386,112.275C0.464,116.762,0,121.399,0,126.087V385.92c0,9.968,2.114,19.55,5.884,28.203 c3.497,8.26,8.653,15.734,14.926,22.001c1.59,1.586,3.169,3.044,4.892,4.494c12.286,10.175,28.145,16.32,45.319,16.32h369.958 c17.18,0,33.108-6.145,45.323-16.384c1.718-1.386,3.305-2.844,4.891-4.43c6.27-6.267,11.425-13.741,14.994-22.001v-0.064 c3.769-8.653,5.812-18.171,5.812-28.138V126.087C512,121.399,511.543,116.762,510.678,112.275z M46.509,101.571 c6.345-6.338,14.866-10.175,24.512-10.175h369.958c9.646,0,18.242,3.837,24.512,10.175c1.122,1.129,2.179,2.387,3.112,3.637 L274.696,274.203c-5.348,4.687-11.954,7.002-18.696,7.002c-6.674,0-13.276-2.315-18.695-7.002L43.472,105.136 C44.33,103.886,45.387,102.7,46.509,101.571z M36.334,385.92V142.735L176.658,265.15L36.405,387.435 C36.334,386.971,36.334,386.449,36.334,385.92z M440.979,420.597H71.021c-6.281,0-12.158-1.651-17.174-4.552l147.978-128.959 l13.815,12.018c11.561,10.046,26.028,15.134,40.36,15.134c14.406,0,28.872-5.088,40.432-15.134l13.808-12.018l147.92,128.959 C453.137,418.946,447.26,420.597,440.979,420.597z M475.666,385.92c0,0.529,0,1.051-0.068,1.515L335.346,265.221L475.666,142.8 V385.92z",
                                fillColor = Color.gray
                            )
                        )
                    )
                )

                textField(
                    keyboardHints = KeyboardHints(
                        autocomplete = AutoComplete.Password, case = KeyboardCase.None
                    ),
                    label = { "Password" },
                    text = password,
                    leadingIcon = ImageVector(
                        viewBoxWidth = 24,
                        viewBoxHeight = 24,
                        width = 18.px,
                        height = 18.px,
                        paths = listOf(
                            ImageVector.Path(
                                path = "M2 12C2 8.22876 2 6.34315 3.17157 5.17157C4.34315 4 6.22876 4 10 4H14C17.7712 4 19.6569 4 20.8284 5.17157C22 6.34315 22 8.22876 22 12C22 15.7712 22 17.6569 20.8284 18.8284C19.6569 20 17.7712 20 14 20H10C6.22876 20 4.34315 20 3.17157 18.8284C2 17.6569 2 15.7712 2 12Z",
                                strokeColor = Color.gray,
                                strokeWidth = 1.5
                            ),
                            ImageVector.Path(
                                path = "M12.0002 10V14M10.2678 11L13.7319 13M13.7317 11L10.2676 13",
                                strokeColor = Color.gray,
                                strokeWidth = 1.0
                            ),
                            ImageVector.Path(
                                path = "M6.73266 10V14M5.00023 11L8.46434 13M8.4641 11L5 13",
                                strokeColor = Color.gray,
                                strokeWidth = 1.0
                            ),
                            ImageVector.Path(
                                path = "M17.2681 10V14M15.5356 11L18.9997 13M18.9995 11L15.5354 13",
                                strokeColor = Color.gray,
                                strokeWidth = 1.0
                            )
                        )
                    )
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
                                val response = fetch("https://swapi.dev/api/people/1")
                                if (response.ok) {
                                    currentUser set AuthenticatedUser(
                                        email = email.once,
                                    )
                                    if (redirect != null)
                                        navigator.replace(navigator.router.findScreen(redirect))
                                    else
                                        navigator.replace(Dashboard())
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
            } in padding(16.px) in sizedBox(
                SizeConstraints(
                    maxWidth = 640.px,
                    minWidth = 640.px
                )
            ) in alignCenter() in fullWidth()
        }
    }

    override fun createPath(): String {
        println("CREATING PATH")
        val path = "$PATH${
            mapOf(
                "email" to email,
                "redirect" to redirect
            ).toURLParams()
        }"
        println(path)
        return path
    }

    companion object {
        const val PATH = "/"

        fun create(props: RouteProps) = Login(
            email = props["email"],
            redirect = props["redirect"]
        )
    }
}
