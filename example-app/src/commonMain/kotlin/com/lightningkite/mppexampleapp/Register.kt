package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Register : RockScreen {
    override fun ViewContext.render() {
        val firstName = Property("")
        val lastName = Property("")
        val email = Property("")
        val phone = Property("")
        val company = Property("")
        val accountNumber = Property("")
        val password = Property("")
        val confirmPassword = Property("")
        val error = Property<String?>(null)

        column {
            textField(
                label = { "First Name" },
                text = firstName,
            )
            textField(
                label = { "Last Name" },
                text = lastName,
            )
            textField(
                label = { "Email" },
                text = email,
            )
            textField(
                label = { "Phone" },
                text = phone,
                keyboardHints = KeyboardHints.phone
            )
            textField(
                label = { "Company" },
                text = company,
            )
            textField(
                label = { "Account Number" },
                text = accountNumber,
            )
            textField(
                label = { "Password" },
                text = password,
                keyboardHints = KeyboardHints.password
            )
            textField(
                label = { "Confirm Password" },
                text = confirmPassword,
                keyboardHints = KeyboardHints.newPassword
            )
            buttonGroup(
                buttons = listOf(
                    ButtonGroupItem(
                        text = "Register",
                        onClick = {
                            if (firstName.once.isEmpty()) {
                                error set "Please enter your first name"
                            } else if (lastName.once.isEmpty()) {
                                error set "Please enter your last name"
                            } else if (email.once.isEmpty()) {
                                error set "Please enter your email"
                            } else if (phone.once.isEmpty()) {
                                error set "Please enter your phone number"
                            } else if (company.once.isEmpty()) {
                                error set "Please enter your company"
                            } else if (accountNumber.once.isEmpty()) {
                                error set "Please enter your account number"
                            } else if (password.once.isEmpty()) {
                                error set "Please enter your password"
                            } else if (confirmPassword.once.isEmpty()) {
                                error set "Please confirm your password"
                            } else if (password.once != confirmPassword.once) {
                                error set "Passwords do not match"
                            } else {
                                error set null
                                val response = fetch("https://swapi.dev/api/people/1")
                                if (response.ok) {
                                    navigator.navigate(Login(email = email.once))
                                } else {
                                    error set "Unable to register"
                                }
                            }
                        }
                    ),
                    ButtonGroupItem(
                        text = "Cancel",
                        onClick = { navigator.goBack() }
                    ),
                )
            )
            errorAlert(content = { error.current })
        } in padding(16.px) in sizedBox(
            SizeConstraints(
                maxWidth = 640.px,
                minWidth = 640.px
            )
        )
    }

    override fun createPath(): String {
        return PATH
    }

    companion object {
        const val PATH = "/register"
    }
}