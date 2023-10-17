package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.content
import com.lightningkite.rock.views.direct.text
import com.lightningkite.rock.views.viewContextAddon

@Routable("item/{id}")
class ItemDetailScreen(val id: String): RockScreen {
    override fun ViewContext.render() = col {
        text { content = "Hello world!" }
        text { content = "My item ID is ${id}" }
        customComponent {
            src = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        }
        globalState = "test"
        text { content = globalState }
    }
}

var ViewContext.globalState: String by viewContextAddon("test")