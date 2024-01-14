package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Routable("load-animation-test")
object LoadAnimationTestScreen : RockScreen {
    @Serializable data class Post(val userId: Int, val id: Int, val title: String, val body: String)

    override fun ViewWriter.render() {
        col {
            val loading = LateInitProperty<String>()
            val writable = Property<String>("")
            h1 { content = "Loading animation testing" }
            col {
                row {
                    button {
                        text("Load")
                        onClick { loading.value = "Test" }
                    }
                    button {
                        text("Unload")
                        onClick { loading.unset() }
                    }
                }
                button {
                    text("Do action")
                    onClick { delay(5000) }
                }
                text { ::content { loading.await() } }
                subtext { ::content { loading.await() } }
                h1 { ::content { loading.await() } }
                h2 { ::content { loading.await() } }
                h3 { ::content { loading.await() } }
                h4 { ::content { loading.await() } }
                h5 { ::content { loading.await() } }
                h6 { ::content { loading.await() } }
                select { bind(writable, shared { loading.await().let(::listOf) }, { it }) }
                textField { content bind loading.withWrite {  } }
                textArea { content bind loading.withWrite {  } }
            } in scrolls in weight(1f)
        }
    }
}

