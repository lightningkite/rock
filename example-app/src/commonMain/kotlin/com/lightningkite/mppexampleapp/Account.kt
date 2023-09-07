package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Account : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        val checked = PersistentProperty("rock.demo.account.checkbox", true)
        val radio = PersistentProperty("rock.demo.account.radio", "one")
        val switch = PersistentProperty("rock.demo.account.switch", true)

        column {
            appBar(title = "Account")
            column {
                text { content = "This is your account." }

                checkBox(checked) {
                    text("Some important setting")
                }

                radioGroup(
                    options = { listOf("one", "two", "three") },
                    value = radio,
                    getKey = { it },
                    getLabel = { it }
                )

                switch(switch) {
                    text("Hello")
                }

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

    companion object {
        const val PATH = "/account"
    }
}
