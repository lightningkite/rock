package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class PasswordRecovery : RockScreen {
    override fun ViewContext.render() {
        val email = Property("")

        column {
            textField(
                label = { "Email" },
                text = email,
            )
            buttonGroup(
                buttons = listOf(
                    ButtonGroupItem(
                        text = "Send Recovery Email",
                        onClick = {
                            println("Send Recovery Email")
                        }
                    ),
                    ButtonGroupItem(
                        text = "Cancel",
                        onClick = {
                            navigator.goBack()
                        }
                    ),
                )
            )
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/password-recovery"
    }
}
