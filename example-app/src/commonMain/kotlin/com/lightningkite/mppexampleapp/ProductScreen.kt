package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

data class Product(
    val name: String,
    val key: String,
    val image: String,
    val description: String,
    val price: Double
)

class ProductScreen(
    private val product: Product
) : RockScreen {
    override fun ViewContext.render() {
        val quantity = Property(1)
        val favorited = Property(false)

        column {
            val quantityInCart = SharedReadable {
                cartItems.current.filter { it.product.key == product.key }.sumOf { it.quantity }
            }
            withTheme(theme.primaryTheme()) {
                h3 {
                    content = product.name
                } in background(theme.normal.background)
            } in margin(Insets(bottom = 16.px))

            row {
                column {
                    image {
                        source = ImageRemote(product.image)
                        scaleType = ImageMode.Fit
                    } in sizedBox(
                        SizeConstraints(
                            maxWidth = 256.px,
                            maxHeight = 256.px,
                            minWidth = 256.px,
                            minHeight = 256.px,
                        )
                    )
                    text {
                        textStyle = TextStyle(
                            color = Color.green.darken(0.5f),
                            size = 24.0,
                        )
                        ::visible { quantityInCart.current > 0 }
                        ::content { "${quantityInCart.current} in cart" }
                    } in padding(Insets(top = 8.px))
                }
                column {
                    h4 {
                        content = product.name
                    }
                    text {
                        content = product.description
                    } in padding(Insets.symmetric(vertical = 12.px))
                    h5 {
                        content = "$${product.price}"
                    }
                } in alignCenter() in padding(Insets(left = 16.px))
            } in alignCenter() in sizedBox(SizeConstraints(maxWidth = 968.px)) in padding(16.px)

            row {
                gravity = StackGravity.End
                integerInput(
                    label = { "Quantity" },
                    value = quantity,
                )
                button(
                    onClick = {
                        cartItems.modify {
                            val existing = it.find { it.product.key == product.key }
                            if (existing != null) {
                                it.filter { it.product.key != product.key } + CartItem(
                                    product = product,
                                    quantity = existing.quantity + quantity.once
                                )
                            } else {
                                it + CartItem(product = product, quantity = quantity.once)
                            }
                        }
//                        quantityInCart.modify { it + quantity.once }
                        quantity set 1
                    }
                ) {
                    text {
                        content = "Add to Cart"
                    }
                } in padding(Insets.symmetric(horizontal = 8.px))

                textButton(
                    onClick = {
                        favorited.modify { !it }
                    }
                ) {
                    text {
                        ::content { if (favorited.current) "Remove from Favorites" else "Add to Favorites" }
                    }
                }
            } in alignCenter()
        }
    }

    override fun createPath(): String = "/product/${product.key}"

    companion object {
        const val PATH = "/product/{productKey}"
    }
}