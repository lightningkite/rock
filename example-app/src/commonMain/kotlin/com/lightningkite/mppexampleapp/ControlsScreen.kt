package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import kotlinx.datetime.*

@Routable("controls")
object ControlsScreen : RockScreen {
    override fun ViewWriter.render() {
        val booleanContent = Property(false)
        col {

            h1 { content = "Controls" } in withDefaultPadding in hasPopover {
                text {
                    content = "Pop over!"
                } in card
            }
            col {
                h2 { content = "Buttons" }
                row {
                    space {} in weight(1f)
                    button { onClick { delay(1000L) }; text { content = "Sample" } }
                    button { onClick { delay(1000L) }; text { content = "Card" } } in card
                    button { onClick { delay(1000L) }; text { content = "Important" } } in important
                    button { onClick { delay(1000L) }; text { content = "Critical" } } in critical
                    button { onClick { delay(1000L) }; text { content = "Warning" } } in warning
                    button { onClick { delay(1000L) }; text { content = "Danger" } } in danger
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card

            col {
                h2 { content = "Toggle Buttons" }
                row {
                    space {} in weight(1f)
                    toggleButton { checked bind booleanContent; text { content = "Sample" } }
                    toggleButton { checked bind booleanContent; text { content = "Card" } } in card
                    toggleButton { checked bind booleanContent; text { content = "Important" } } in important
                    toggleButton { checked bind booleanContent; text { content = "Critical" } } in critical
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card

            card - col {
                h2 { content = "Switches" }
                col {
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { checked bind booleanContent; }
                        }
                    } in withDefaultPadding
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { checked bind booleanContent; }
                        }
                    } in card
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { checked bind booleanContent; }
                        }
                    } in important
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { checked bind booleanContent; }
                        }
                    } in critical
                }
            }

            col {
                h2 { content = "Checkboxes" }
                col {
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { checked bind booleanContent }
                        }
                    } in withDefaultPadding
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { checked bind booleanContent }
                        }
                    } in card
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { checked bind booleanContent }
                        }
                    } in important
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { checked bind booleanContent }
                        }
                    } in critical
                }
            } in card

            col {
                h2 { content = "Activity Indicators" }
                row {
                    space {} in weight(1f)
                    stack { activityIndicator { } } in withDefaultPadding
                    stack { activityIndicator { } } in card
                    stack { activityIndicator { } } in important
                    stack { activityIndicator { } } in critical
                    stack { activityIndicator { } } in warning
                    stack { activityIndicator { } } in danger
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card

            col {
                h2 { content = "Drop Downs" }
                val options = shared { listOf("Apple", "Banana", "Crepe") }
                val value = Property("Apple")
                select { bind(value, data = options, render = { it }) } in withDefaultPadding
                select { bind(value, data = options, render = { it }) } in card
                select { bind(value, data = options, render = { it }) } in important
                select { bind(value, data = options, render = { it }) } in critical
                select { bind(value, data = options, render = { it }) } in warning
                select { bind(value, data = options, render = { it }) } in danger
            } in card

            col {
                val date = Property<LocalDate?>(null)
                h2 { content = "Date Fields" }
                localDateField { content bind date }
                localDateField { content bind date } in card
                localDateField { content bind date } in important
                localDateField { content bind date } in critical
            } in card

            col {
                val date = Property<LocalTime?>(null)
                h2 { content = "Time Fields" }
                localTimeField { content bind date }
                localTimeField { content bind date } in card
                localTimeField { content bind date } in important
                localTimeField { content bind date } in critical
            } in card

            col {
                val date = Property<LocalDateTime?>(null)
                h2 { content = "Date Time Fields" }
                localDateTimeField { content bind date }
                localDateTimeField { content bind date } in card
                localDateTimeField { content bind date } in important
                localDateTimeField { content bind date } in critical
            } in card

            col {
                val number = Property(1)
                val text = Property("text")
                h2 { content = "Text Fields" }
                textField { content bind number.asString() }
                text { ::content { "Value: ${number.await()}" }}
                textField { content bind text } in card
                text { ::content { "Text: ${text.await()}" }}
                textField { content bind text } in important
                textField { content bind text } in critical
            } in card

            col {
                val text = Property("Longer form text\n with newlines goes here")
                h2 { content = "Text Areas" }
                textArea { content bind text }
                textArea { content bind text } in card
                textArea { content bind text } in important
                textArea { content bind text } in critical
            } in card

            col {
                h2 { content = "Images" }
                row {
                    repeat(5) {
                        image { source = ImageRemote("https://picsum.photos/seed/${it}/200/300") } in sizedBox(
                            SizeConstraints(
                                width = 5.rem
                            )
                        )
                    }
                } in scrollsHorizontally
            } in card
        } in scrolls
    }
}