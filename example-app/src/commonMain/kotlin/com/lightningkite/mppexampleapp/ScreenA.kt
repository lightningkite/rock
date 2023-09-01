package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class ScreenA: RockScreen {
    override fun ViewContext.render() {
        column {
            text { content = "Screen A" }
            image {
                source = ImageRemote("https://picsum.photos/seed/a/300/300")
            }
        }
    }

    override fun createPath(): String {
        return PATH
    }

    companion object {
        const val PATH = "/screenA"
    }

}