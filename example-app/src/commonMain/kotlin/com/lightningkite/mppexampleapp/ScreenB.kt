package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class ScreenB: RockScreen {
    override fun ViewContext.render() {
        column {
            text { content = "Screen B" }
            image {
                source = ImageRemote("https://picsum.photos/seed/b/300/300")
            }
        }
    }

    override fun createPath(): String {
        return PATH
    }

    companion object {
        const val PATH = "/screenB"
    }
}