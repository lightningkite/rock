package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Search : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        val search = Property("")

        column {
            appBar(title = "Search")
            column {
                textField(
                    text = search,
                    hint = { "Search all categories" },
                    leadingIcon = ImageVector(
                        width = 18.px, height = 18.px, paths = listOf(
                            ImageVector.Path(
                                strokeColor = Color.gray,
                                path = "M15.7955 15.8111L21 21M18 10.5C18 14.6421 14.6421 18 10.5 18C6.35786 18 3 14.6421 3 10.5C3 6.35786 6.35786 3 10.5 3C14.6421 3 18 6.35786 18 10.5Z",
                                strokeWidth = 2.0
                            )
                        )
                    )
                )
            } in padding(Insets.symmetric(horizontal = 8.px))

            forEach(
                data = { searchProducts(search.current, rootCategory) },
                render = { product ->
                    card(
                        header = product.name,
                        description = product.description,
                        image = ImageRemote(product.image),
                        onClick = { navigator.navigate(ProductScreen(product)) }
                    )
                }
            )
        } in scrolls()
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/search"
    }
}