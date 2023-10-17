package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.direct.*

@Routable("arguments-example/{id}")
class ArgumentsExampleScreen(val id: String): RockScreen {
    override fun ViewContext.render() = col {
        h1 { content = "Hello world!" }
        text { content = "My item ID is ${id}" }
        text { content = "This is a demonstration of how you can use classes and properties to navigate to different views." }
        link {
            text { content = "Append '-plus'" }
            to = ArgumentsExampleScreen("$id-plus")
        }
    }
}

//globalState = "test"
//text { content = globalState }
//
//var ViewContext.globalState: String by viewContextAddon("test")