package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Fetching
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Routable("sample/data")
object DataLoadingExampleScreen : RockScreen {
    @Serializable data class Post(val userId: Int, val id: Int, val title: String, val body: String)

    override fun ViewContext.render() {
        val data: Fetching<List<Post>> = Fetching {
            delay(1000)
            val response: RequestResponse = fetch("https://jsonplaceholder.typicode.com/posts")
            Json.decodeFromString<List<Post>>(response.text())
        }
        col {
            h1 { content = "This example loads some data." }
            text { content = "It's also faking a lot of loading so you can see what it looks like." }
            col {
                forEachUpdating(data) {
                    col {
                        val f = Fetching { delay(Random.nextLong(0, 10000)); "" }
                        h3 { ::content{ it.current.title + f.current } }
                        text { ::content{ it.current.body + f.current } }
                    } in card
                }
            } in scrolls()
        }
    }
}

