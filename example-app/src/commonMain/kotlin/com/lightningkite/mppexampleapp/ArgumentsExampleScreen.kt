package com.lightningkite.mppexampleapp

import com.lightningkite.rock.QueryParameter
import com.lightningkite.rock.Routable
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*

@Routable("arguments-example/{id}")
class ArgumentsExampleScreen(val id: String): RockScreen {

    @QueryParameter
    val toAdd = Property("")

    @QueryParameter
    val list = Property(listOf("sample"))

    override fun ViewWriter.render() = col {
        h1 { content = "Hello world!" }
        text { content = "My item ID is ${id}" }
        text { content = "This is a demonstration of how you can use classes and properties to navigate to different views." }
        link {
            text { content = "Append '-plus'" }
            ::to {
                ArgumentsExampleScreen("$id-plus").also {
                    it.toAdd.value = toAdd.await()
                    it.list.value = list.await()
                }
            }
        }
        h2 { content = "The list so far" }
        col {
            forEachUpdating(list) {
                text { ::content { it.await() } }
            }
        }
        h2 { content = "Add more" }
        textField { content bind toAdd }
        button {
            text { content = "Add" }
            onClick {
                list.value += toAdd.value
                toAdd.value = ""
            }
        }
    }
}

//globalState = "test"
//text { content = globalState }
//
//var ViewContext.globalState: String by viewContextAddon("test")