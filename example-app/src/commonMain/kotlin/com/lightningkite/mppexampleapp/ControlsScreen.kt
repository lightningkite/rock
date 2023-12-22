package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

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
                    button { text { content = "Sample" } }
                    button { text { content = "Card" } } in card
                    button { text { content = "Important" } } in important

                    button { text { content = "Warning" } } in warning
                    button { text { content = "Danger" } } in danger
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card

            col {
                h2 { content = "Toggle Buttons" }
                row {
                    space {} in weight(1f)
                    toggleButton { text { content = "Sample" } }
                    toggleButton { text { content = "Card" } } in card
                    toggleButton { text { content = "Important" } } in important
                    toggleButton { text { content = "Critical" } } in critical
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card

            col {
                h2 { content = "Switches" }
                col {
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { }
                        }
                    } in withDefaultPadding
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { }
                        }
                    } in card
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { }
                        }
                    } in important
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            switch { }
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
                            checkbox { }
                        }
                    } in withDefaultPadding
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { }
                        }
                    } in card
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { }
                        }
                    } in important
                    stack {
                        row {
                            h3 { content = "Example Setting" } in weight(1f)
                            checkbox { }
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
                val options = listOf("Apple", "Banana", "Crepe")
                select { bind(Property("Apple"), data = { options }, render = { it }) } in withDefaultPadding
                select { bind(Property("Apple"), data = { options }, render = { it }) } in card
                select { bind(Property("Apple"), data = { options }, render = { it }) } in important
                select { bind(Property("Apple"), data = { options }, render = { it }) } in critical
                select { bind(Property("Apple"), data = { options }, render = { it }) } in warning
                select { bind(Property("Apple"), data = { options }, render = { it }) } in danger
            } in card

            col {
                h2 { content = "Date Fields" }
                localDateField {  }
                localDateField {  } in card
                localDateField {  } in important
                localDateField {  } in critical
            } in card

            col {
                h2 { content = "Time Fields" }
                localTimeField {  }
                localTimeField {  } in card
                localTimeField {  } in important
                localTimeField {  } in critical
            } in card

            col {
                h2 { content = "Date Time Fields" }
                localDateTimeField {  }
                localDateTimeField {  } in card
                localDateTimeField {  } in important
                localDateTimeField {  } in critical
            } in card

            col {
                val number = Property(1)
                val text = Property("text")
                h2 { content = "Text Fields" }
                textField { content bind number.asString() }
                text { ::content { "Value: ${number.await()}" }}
                textField { content bind text } in card
                text { ::content { "Text: ${text.await()}" }}
                textField {  } in important
                textField {  } in critical
            } in card

            col {
                h2 { content = "Text Areas" }
                textArea {  }
                textArea {  } in card
                textArea {  } in important
                textArea {  } in critical
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