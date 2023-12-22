package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.reactive.shared
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Routable("sample/websockets")
object WebSocketScreen : RockScreen {
    override fun ViewWriter.render() {
        val socket = websocket("wss://socketsbay.com/wss/v2/1/demo/")
        val counter = Property("")
        socket.onMessage {
            counter.value = it
        }
        socket.onOpen {
            socket.send("BLAHSDEIOHJOREF")
        }
        col {
            calculationContext.onRemove { socket.close(1000, "") }
            h1 { content = "WS time!" }
            text { ::content { counter.await() } }
            button {
                text("Send junk")
                onClick {
                    socket.send(Random.nextInt().toString())
                }
            }
        }
    }
}

