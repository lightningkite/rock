package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Account : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        column {
            appBar(title = "Account")
            column {
                text { content = "This is your account." }
                button(
                    onClick = {
                        currentUser set null
                        navigator.replace(Login())
                    },
                ) {
                    text {
                        content = "Log Out"
                    }
                }
            } in padding(16.px)
        }
    }

    override fun createPath(): String = PATH
    override val icon = null
    override val title = "Account"
    override val showInNavigation = true

    companion object {
        const val PATH = "/account"
    }
}
