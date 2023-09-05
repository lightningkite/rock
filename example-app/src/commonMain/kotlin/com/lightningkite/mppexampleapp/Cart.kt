package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Cart : RockScreen {
    override fun ViewContext.render() {
        column {
            withTheme(theme.primaryTheme()) {
                h3 {
                    content = "Cart"
                } in background(theme.normal.background)
            } in margin(Insets(bottom = 16.px))
            val cartProducts = SharedReadable {
                cartItems.current.map { it.product }
            }
            forEach(
                data = { cartProducts.current },
                render = { it ->
                    println("Rendering card")
                    cartCard(
                        product = it
                    )
                },
                fallback = {
                    text { content = "Your cart is empty." } in padding(8.px)
                }
            )
        }
    }

    override fun createPath(): String = PATH

    companion object {
        const val PATH = "/cart"
    }
}
