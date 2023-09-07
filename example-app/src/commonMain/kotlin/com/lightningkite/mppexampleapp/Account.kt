package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Account : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        val checked = PersistentProperty("rock.demo.account.checkbox", true)
        val radio = PersistentProperty("rock.demo.account.radio", "one")
        val switch = PersistentProperty("rock.demo.account.switch", true)
        val dropdown = PersistentProperty<String?>("rock.demo.account.dropdown", "two")

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
                    getLabel = { it },
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

                val dropdownOptions = Property(listOf(null, "one", "two", "three", "four", "five", "six"))

                dropDown(
                    options = dropdownOptions,
                    prop = dropdown,
                    getLabel = { it ?: "Select an item" },
                    getKey = { it ?: "null" },
                )

                button(onClick = {
                    dropdownOptions.modify { it + "another" }
                }) { text("Add item to dropdown") }
            } in padding(16.px)
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/account"
    }
}
