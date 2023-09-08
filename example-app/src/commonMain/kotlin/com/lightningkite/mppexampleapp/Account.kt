package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Account : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        val checked = PersistentProperty("rock.demo.account.checkbox", true)
        val radio = PersistentProperty("rock.demo.account.radio", "one")
        val switch = PersistentProperty("rock.demo.account.switch", true)
        val dropdown = PersistentProperty<String?>("rock.demo.account.dropdown", "two")
        val textField = Property(currentUser.once?.email ?: "")
        val autocomplete = Property<String?>("one")

        val swapText = Property("a")
        val swapProp = Property<ViewContext.() -> Unit>({
            text("SWAP1")
        })

        column {
            appBar(title = "Account")

            column {

                swapView(swapProp)

                button(onClick = {
                    swapProp set {
                        text("SWAP${swapText.once}")
                        swapText.modify { it + "1" }
                    }
                }) {
                    text("SWAP")
                }

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

                textField(
                    label = { "Test" },
                    text = textField,
                )

                autoComplete(
                    label = "AutoComplete",
                    options = { listOf("one", "two", "three") },
                    prop = autocomplete,
                    getKey = { it },
                    getLabel = { it }
                )

                text {
                    ::content { autocomplete.current ?: "n/a" }
                }

                stack {

                }

                link {
                    to = CategoryScreen(rootCategory.subcategories[0])
                    content = "Cart Link With A Long Name"
                }
            } in padding(16.px)
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/account"
    }
}
