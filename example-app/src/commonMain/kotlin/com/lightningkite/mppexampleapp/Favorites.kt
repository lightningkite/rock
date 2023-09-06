package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Favorites : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        column {
            appBar(title = "Favorites")

            listTileGroup(
                data = { favorites.current },
                render = { product -> productCard(product) },
                fallback = {
                    text {
                        content = "You have no favorites."
                    } in padding(8.px)
                }
            )
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/favorites"
    }
}