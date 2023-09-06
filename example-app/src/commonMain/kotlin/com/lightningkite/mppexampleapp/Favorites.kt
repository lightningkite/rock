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
    override val icon = null
    override val title = "Favorites"
    override val showInNavigation = true

    companion object {
        const val PATH = "/favorites"
    }
}