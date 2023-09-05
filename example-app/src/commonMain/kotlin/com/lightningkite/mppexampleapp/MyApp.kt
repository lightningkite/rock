package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

class MyApp : RockApp {
    override fun ViewContext.render() {
        val theme = Theme(
            titleFont = systemDefaultFont,
            bodyFont = systemDefaultFont,
            baseSize = 18.0,
            normal = PaintPair(
                foreground = Color.black, background = Color.white
            ),
            primary = PaintPair(
                foreground = Color.white, background = Color.fromHex(0x1566B7)
            ),
            accent = PaintPair(
                foreground = Color.white,
                background = Color.fromHex(0x9C27B0),
            ),
            normalDisabled = PaintPair(
                foreground = Color.fromHex(0x999999), background = Color.white
            ),
            primaryDisabled = PaintPair(
                foreground = Color.fromHex(0xededed), background = Color.fromHex(0x666666)
            ),
            accentDisabled = PaintPair(
                foreground = Color.fromHex(0x999999), background = Color.green
            ),
        )

        withTheme(theme) {
            column {
                box {
                    routerView(
                        Router(
                            routes = listOf(
                                Route(Login.PATH) { _, params -> Login(email = params["email"]) },
                                Route(NotFound.PATH) { _, _ -> NotFound() },
                                Route(Register.PATH) { _, _ -> Register() },
                                Route(PasswordRecovery.PATH) { _, _ -> PasswordRecovery() },
                                Route(Dashboard.PATH) { _, _ -> Dashboard() },
                                Route(CategoryScreen.PATH) { params, _ ->
                                    CategoryScreen(
                                        category = findCategory(params["categoryKey"]!!, rootCategory)!!
                                    )
                                },
                                Route(ProductScreen.PATH) { params, _ ->
                                    ProductScreen(
                                        product = findProduct(params["productKey"]!!, rootCategory)!!
                                    )
                                },
                                Route(Cart.PATH) { _, _ -> Cart() },
                            ), fallback = NotFound()
                        )
                    )
                } in background(paint = Color.fromHex(0xfafafa), padding = Insets.none) in weight(1f)

                row {
                    gravity = StackGravity.Center
                    navButton(text = { "Home" }, icon = ImageVector(
                        width = 24.px, height = 24.px, paths = listOf(
                            ImageVector.Path(
                                fillColor = Color.black,
                                path = "M21.4498 10.275L11.9998 3.1875L2.5498 10.275L2.9998 11.625H3.7498V20.25H20.2498V11.625H20.9998L21.4498 10.275ZM5.2498 18.75V10.125L11.9998 5.0625L18.7498 10.125V18.75H14.9999V14.3333L14.2499 13.5833H9.74988L8.99988 14.3333V18.75H5.2498ZM10.4999 18.75H13.4999V15.0833H10.4999V18.75Z",
                            )
                        )
                    ), onClick = { navigator.replace(Dashboard()) })
                    navButton(text = { "Favorites" }, icon = ImageVector(
                        width = 24.px, height = 24.px, paths = listOf(
                            ImageVector.Path(
                                strokeColor = Color.black,
                                strokeWidth = 2,
                                path = "M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z",
                            )
                        )
                    ), onClick = { navigator.replace(Dashboard()) })
                    navButton(text = {
                        val size = cartItems.current.size
                        if (size == 0) "Cart" else "Cart ($size)"
                    }, icon = ImageVector(
                        width = 24.px, height = 24.px, paths = listOf(
                            ImageVector.Path(
                                strokeColor = Color.black,
                                path = "M6.29977 5H21L19 12H7.37671M20 16H8L6 3H3M9 20C9 20.5523 8.55228 21 8 21C7.44772 21 7 20.5523 7 20C7 19.4477 7.44772 19 8 19C8.55228 19 9 19.4477 9 20ZM20 20C20 20.5523 19.5523 21 19 21C18.4477 21 18 20.5523 18 20C18 19.4477 18.4477 19 19 19C19.5523 19 20 19.4477 20 20Z",
                                strokeWidth = 2
                            )
                        )
                    ), onClick = { navigator.replace(Cart()) })

                } in background(paint = Color.white) in sizedBox(
                    SizeConstraints(
                        minHeight = 96.px, maxHeight = 96.px
                    )
                )
            }
        }
    }
}

private fun ViewContext.navButton(
    text: ReactiveScope.() -> String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    button(
        options = ButtonOptions(
            variant = ButtonVariant.Text, size = ButtonSize.Large, fullWidth = true
        ), disabled = { false }, onClick = onClick
    ) {
        row {
            gravity = StackGravity.Center
            image {
                ::source {
                    icon.copy(
                        width = 32.px,
                        height = 32.px,
                        paths = icon.paths.map {
                            it.copy(
                                strokeColor = if (it.strokeColor == null) null else theme.normal.foreground.closestColor(),
                                fillColor = if (it.fillColor == null) null else theme.normal.foreground.closestColor(),
                            )
                        })
                }
            } in padding(Insets(right = 6.px))
            text {
                ::content { text() }
            }
        }
    }
}

private fun findCategory(key: String, category: Category): Category? {
    if (category.key == key) return category
    for (sub in category.subcategories) {
        val result = findCategory(key, sub)
        if (result != null) return result
    }
    return null
}

private fun findProduct(key: String, category: Category): Product? {
    for (product in category.products) {
        if (product.key == key) return product
    }
    for (sub in category.subcategories) {
        val result = findProduct(key, sub)
        if (result != null) return result
    }
    return null
}

data class CartItem(
    val product: Product, val quantity: Int
)

val ViewContext.cartItems by viewContextAddon(
    Property<List<CartItem>>(
        listOf(
            CartItem(product = findProduct("ic-surge-device", rootCategory)!!, quantity = 1),
        )
    )
)

fun <A, B> Readable<A>.distinctUntilChanged(map: (A) -> B): Readable<B> {
    return object : Readable<B> {
        override var once: B = map(this@distinctUntilChanged.once)
            private set

        override fun addListener(listener: () -> Unit): () -> Unit {
            return this@distinctUntilChanged.addListener {
                val newValue = map(this@distinctUntilChanged.once)
                if (newValue != once) {
                    once = newValue
                    listener()
                }
            }
        }
    }
}
