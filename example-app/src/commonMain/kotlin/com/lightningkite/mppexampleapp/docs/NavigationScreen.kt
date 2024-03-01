package com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Constant
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.icon

@Routable("docs/navigation")
object NavigationScreen: DocScreen {

    override val title: Readable<String>
        get() = Constant("Navigation: how to manage screens")

    override val covers: List<String> = listOf(
        "Screen",
        "RockScreen",
        "Navigator",
        "PlatformNavigator",
        "navigate",
        "pop",
        "dismiss",
        "replace",
    )

    override fun ViewWriter.render() {
        article {
            h1("Navigation")

            space()
            h2("Navigate with Links")
            text("You can navigate between screens using links.  These are preferred as they render as proper links in web.")
            text("The destinations of links are instances of RockScreen, which ensures at the compilation level that you can't produce invalid links.")
            example(
                """
            link {
                to = DocSearchScreen
                centered - row {
                    centered - icon(Icon.arrowBack, "Back")
                    centered - expanding - text("Back to the search screen")
                }
            }
            """.trimIndent()
            ) {
                link {
                    to = DocSearchScreen
                    centered - row {
                        centered - icon(Icon.arrowBack, "Back")
                        centered - expanding - text("Back to the search screen")
                    }
                }
            }

            space()
            h2("Navigate Programmatically")
            text("You can navigate programmatically as well, which is useful for circumstances such as navigating to a different screen after uploading data is complete.")
            example(
                """
                button {
                    onClick { 
                        navigator.navigate(DocSearchScreen)
                    }
                    centered - row {
                        centered - icon(Icon.arrowBack, "Back")
                        centered - expanding - text("Back to the search screen")
                    }
                }
            """.trimIndent()
            ) {
                button {
                    onClick {
                        navigator.navigate(DocSearchScreen)
                    }
                    centered - row {
                        centered - icon(Icon.arrowBack, "Back")
                        centered - expanding - text("Back to the search screen")
                    }
                }
            }
            text("When navigating programmatically, you can also pop, reset, or replace.")
            example(
                """
                col {
                    button {
                        onClick {
                            navigator.goBack()
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Back to previous screen")
                        }
                    }
                    button {
                        onClick {
                            navigator.replace(DocSearchScreen)
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Replace this view in history with the search screen")
                        }
                    }
                    button {
                        onClick {
                            navigator.reset(DocSearchScreen)
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Clear the stack and go to the search screen")
                        }
                    }
                }
            """.trimIndent()
            ) {
                col {
                    button {
                        onClick {
                            navigator.goBack()
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Back to previous screen")
                        }
                    }
                    button {
                        onClick {
                            navigator.replace(DocSearchScreen)
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Replace this view in history with the search screen")
                        }
                    }
                    button {
                        onClick {
                            navigator.reset(DocSearchScreen)
                        }
                        row {
                            centered - icon(Icon.arrowBack, "Back")
                            centered - expanding - text("Clear the stack and go to the search screen")
                        }
                    }
                }
            }

            space()
            h2("Dialogs")
            example(
                """
                button {
                    text("Open a dialog")
                    onClick {
                        navigator.dialog.navigate(object: RockScreen {
                            override fun ViewWriter.render() {
                                stack {
                                    centered - card - col {
                                        h1("Hello!")
                                        text("This is a dialog.")
                                        button {
                                            centered - text("Dismiss")
                                            onClick {
                                                navigator.dismiss()
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            """.trimIndent()
            ) {
                button {
                    text("Open a dialog")
                    onClick {
                        navigator.dialog.navigate(object: RockScreen {
                            override fun ViewWriter.render() {
                                stack {
                                    centered - card - col {
                                        h1("Hello!")
                                        text("This is a dialog.")
                                        button {
                                            centered - text("Dismiss")
                                            onClick {
                                                navigator.dismiss()
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    }

}