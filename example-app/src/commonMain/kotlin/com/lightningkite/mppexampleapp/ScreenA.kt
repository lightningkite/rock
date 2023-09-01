package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class ScreenA: RockScreen {
    override fun ViewContext.render() {
        val text = Property("Hello, world!")

        column {
            text { content = "Screen A" }
            image {
                source = ImageRemote("https://picsum.photos/seed/a/300/300")
            }

            textField(
                label = { "Text" },
                text = text,
            )
        }
    }

    override fun createPath(): String {
        return PATH
    }

    companion object {
        const val PATH = "/screenA"
    }

}