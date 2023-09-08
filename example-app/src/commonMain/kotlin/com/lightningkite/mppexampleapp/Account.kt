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
        val toggleButtonGroup = PersistentProperty("rock.demo.account.toggle", 1)
        val dropdownOptions = Property(listOf(null, "one", "two", "three", "four", "five", "six"))
        val recyclerItems = Property((1..100).toList())

        val swapText = Property("a")
        val swapProp = Property<ViewContext.() -> Unit>({
            text("SWAP1")
        })

        column {
            appBar(title = "Account")

            column {

                row {
                    toggleButtonGroup(
                        toggled = { toggleButtonGroup.current },
                        onToggle = { toggleButtonGroup.set(it) },
                        disabled = { false },
                        buttons = {
                            listOf(
                                ToggleButton("One", 1, RockIcon.Home),
                                ToggleButton("Two", 2, RockIcon.Account),
                                ToggleButton("Three", 3, RockIcon.HeartOutlined),
                            )
                        },
                    )
                    expand()
                    column {
                        swapView(swapProp)
                        button(onClick = {
                            swapProp set {
                                text("SWAP${swapText.once}")
                                swapText.modify { it + "1" }
                            }
                        }) { text("SWAP") } in alignRight()
                    }
                }

                row {
                    checkBox(checked) {
                        text("Some important setting")
                    }
                    expand()
                    column {
                        radioGroup(
                            options = { listOf("one", "two", "three") },
                            value = radio,
                            getKey = { it },
                            getLabel = { it },
                        )
                    }
                    expand()
                    switch(switch) {
                        text("Hello")
                    }
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


                row {
                    dropDown(
                        options = dropdownOptions,
                        prop = dropdown,
                        getLabel = { it ?: "Select an item" },
                        getKey = { it ?: "null" },
                    ) in weight(1f) in margin(right = 8.px)

                    button(onClick = {
                        dropdownOptions.modify { it + "another" }
                    }) { text("Add item to dropdown") }
                }

                textField(
                    label = { "Test" },
                    text = textField,
                )


                row {
                    gravity = RowGravity.Bottom
                    autoComplete(
                        label = "AutoComplete",
                        options = { listOf("one", "two", "three") },
                        prop = autocomplete,
                        getKey = { it },
                        getLabel = { it }
                    ) in weight(1f) in margin(right = 4.px)
                    text { ::content { "Autocomplete value: ${autocomplete.current ?: "n/a"}" } }
                }

                link {
                    to = Cart()
                    content = "Link to Cart"
                } in padding(vertical = 8.px)


                button(onClick = { recyclerItems.modify { it + 1 } }) {
                    text("Add item to recycler")
                }

                recyclerView(
                    data = { recyclerItems.current },
                    render = {
                        text(it.toString())
                    },
                    estimatedItemHeightInPixels = 18
                ) in weight(1f) in nativeBackground(Color.blue.lighten(0.8f)) in scrolls()

            } in padding(16.px) in weight(1f)
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/account"
    }
}
