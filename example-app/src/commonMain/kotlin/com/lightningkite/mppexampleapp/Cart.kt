package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class Cart : AuthenticatedScreen() {
    override fun ViewContext.renderAuthenticated() {
        column {
            appBar(title = "Cart")
            val cartProducts = SharedReadable {
                cartItems.current.map { it.product }
            }
            forEach(
                data = { cartProducts.current },
                render = { it ->
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
