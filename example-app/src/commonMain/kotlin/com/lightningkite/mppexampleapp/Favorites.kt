package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Favorites : RockScreen {
    override fun ViewContext.render() {
        column {
            appBar(title = "Favorites")

            forEach(
                data = { favorites.current },
                render = { product ->
                    card(
                        header = product.name,
                        description = product.description,
                        image = ImageRemote(product.image),
                        onClick = {
                            navigator.navigate(ProductScreen(product))
                        }
                    )
                },
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