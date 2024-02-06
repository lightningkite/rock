package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("reactivity")
object ReactivityScreen : RockScreen {
    override val title: Readable<String>
        get() = super.title

    override fun ViewWriter.render() {
        val local = Property("Local")
        val persist = PersistentProperty("persistent-example", "Persistent")
        val fetching = shared {
            delay(1000)
            "Loaded!"
        }
        col {
            col {
                h1 { content = "This screen demonstrates various forms of reactivity." }
                text { content = "Note the use of the multi-layer 'Readable' in `fetching`." }
            } in withDefaultPadding

            col {
                h2 { content = "Data" }
                label {
                    content = "Locally Stored Value"
                    textField { content bind local }
                }
                label {
                    content = "Persistent Value - this will stay between refreshes"
                    textField { content bind persist }
                }
                button {
                    text { content = "Reload 'fetching'" }
                    onClick {
                        // TODO
                    }
                } in important
            } in card

            col {
                h2 { content = "Using reactiveScope()" }
                text { reactiveScope { content = local.await() } }
                text { reactiveScope { content = persist.await() } }
                text { reactiveScope { content = fetching.await() } }
            } in card

            col {
                h2 { content = "Using ::content {}" }
                text { ::content { local.await() } }
                text { ::content { persist.await() } }
                text { ::content { fetching.await() } }
            } in card
        }
    }
}