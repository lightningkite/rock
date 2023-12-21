package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.reactive.shared
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Routable("sample/data")
object DataLoadingExampleScreen : RockScreen {
    @Serializable data class Post(val userId: Int, val id: Int, val title: String, val body: String)

    override fun ViewWriter.render() {
        val data = shared {
            delay(1000)
            val response: RequestResponse = fetch("https://jsonplaceholder.typicode.com/posts")
            Json.decodeFromString<List<Post>>(response.text())
        }
        col {
            h1 { content = "This example loads some data." }
            text { content = "It's also faking a lot of loading so you can see what it looks like." }
            text { ::content { fetch("/").text().take(100) } }
            col {
                forEachUpdating(data) {
                    col {
                        val f = shared { delay(Random.nextLong(0, 1000)); "" }
                        h3 { ::content { it.await().title + f.await() } }
                        text { ::content { it.await().body + f.await() } }
                    } in card
                }
            } in scrolls
        }
    }
}

