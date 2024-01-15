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

@Routable("sample/websockets")
object WebSocketScreen : RockScreen {
    override fun ViewWriter.render() {
        val socket = shared { retryWebsocket("wss://socketsbay.com/wss/v2/1/demo/").also { use(it) } }
        val mostRecent = shared { socket.await().mostRecentMessage }
        col {
            h1 { content = "WS time!" }
            text { ::content { mostRecent.await().await() ?: "Nothing yet" } }
            button {
                text("Send junk")
                onClick {
                    println("Preparing to send...")
                    socket.await().send("From Rock (Kotlin): ${clockMillis()}")
                    println("Sent!")
                }
            }
            button {
                text("Kill")
                onClick {
                    socket.await().close(1000, "OK")
                }
            }
        }
    }
}

