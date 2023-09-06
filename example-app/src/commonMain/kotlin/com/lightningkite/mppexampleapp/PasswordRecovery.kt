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
        } in padding(16.px) in sizedBox(
            SizeConstraints(
                maxWidth = 640.px,
                minWidth = 640.px
            )
        )
    }

    override fun createPath(): String = PATH
    override val icon = null
    override val title = "Password Recovery"
    override val showInNavigation = false

    companion object {
        const val PATH = "/password-recovery"
    }
}
