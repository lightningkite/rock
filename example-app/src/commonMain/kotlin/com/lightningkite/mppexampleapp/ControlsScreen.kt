package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.ImageRemote
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.WidgetOption
import com.lightningkite.rock.models.px
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("controls")
object ControlsScreen : RockScreen {
    override fun ViewContext.render() {
        col {

            val stringContent = PersistentProperty("test-data", "Test")
//            val stringContent = Property("Test")
            val booleanContent = Property(false)

            h1 { content = "Controls" } in withPadding
            col {
                h2 { content = "Buttons" }
                row {
                    space {} in weight(1f)
                    button { text { content = "Sample" } }
                    button { text { content = "Card" } } in card
                    button { text { content = "Important" } } in important
                    button { text { content = "Critical" } } in critical
                    button { text { content = "Warning" } } in warning
                    button { text { content = "Danger" } } in danger
                    space {} in weight(1f)
                } in scrollsHorizontally()
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
                } in scrollsHorizontally()
            } in card

            col {
                h2 { content = "Switches" }
                row {
                    space {} in weight(1f)
                    switch { checked bind booleanContent; }
                    switch { checked bind booleanContent; } in card
                    switch { checked bind booleanContent; } in important
                    switch { checked bind booleanContent; } in critical
                    space {} in weight(1f)
                } in scrollsHorizontally()
            } in card

            col {
                h2 { content = "Activity Indicators" }
                row {
                    space {} in weight(1f)
                    stack { activityIndicator { } } in withPadding
                    stack { activityIndicator { } } in card
                    stack { activityIndicator { } } in important
                    stack { activityIndicator { } } in critical
                    stack { activityIndicator { } } in warning
                    stack { activityIndicator { } } in danger
                    space {} in weight(1f)
                } in scrollsHorizontally()
            } in card

            col {
                h2 { content = "Drop Downs" }
                val options = listOf("Apple", "Banana", "Crepe").map { WidgetOption(it, it) }
                dropDown { this.options = options } in withPadding
                dropDown { this.options = options } in card
                dropDown { this.options = options } in important
                dropDown { this.options = options } in critical
                dropDown { this.options = options } in warning
                dropDown { this.options = options } in danger
            } in card

            col {
                h2 { content = "Text Fields" }
                text{ ::content { "Content: " + stringContent.current } }
                text{
                    reactiveScope {
                        content = "Reactive scope style: " + stringContent.current
                    }
                }
                textField { content bind stringContent }
                textField { content bind stringContent } in card
                textField { content bind stringContent } in important
                textField { content bind stringContent } in critical
            } in card

            col {
                h2 { content = "Text Areas" }
                textArea { content bind stringContent }
                textArea { content bind stringContent } in card
                textArea { content bind stringContent } in important
                textArea { content bind stringContent } in critical
            } in card

            col {
                h2 { content = "Images" }
                row {
                    repeat(5) {
                        image { source = ImageRemote("https://picsum.photos/seed/${it}/200/300") } in sizedBox(
                            SizeConstraints(
                                maxWidth = 100.px,
                                minWidth = 100.px
                            )
                        )
                    }
                } in scrollsHorizontally()
            } in card
        } in scrolls()
    }
}