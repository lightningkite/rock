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
//                    val navItems = listOf(
//                        "Home" to Dashboard(),
//                        "Favorites" to Dashboard(),
//                    )

//                    forEach(direction = ForEachDirection.Horizontal, data = { navItems }, render = { it ->
//                        button(options = ButtonOptions(
//                            variant = ButtonVariant.Text, size = ButtonSize.Large, fullWidth = true
//                        ), disabled = { false }, onClick = {
//                            navigator.replace(it.second)
//                        }) {
//                            text { content = it.first }
//                        }
//                    })
//
                    navButton(text = { "Home" }, onClick = { navigator.replace(Dashboard()) })
                    navButton(text = { "Favorites" }, onClick = { navigator.replace(Dashboard()) })
                    navButton(text = {
                        val size = cartItems.current.size
                        if (size == 0) "Cart" else "Cart ($size)"
                    }, onClick = { navigator.replace(Cart()) })

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
    onClick: () -> Unit,
) {
    button(
        options = ButtonOptions(
            variant = ButtonVariant.Text, size = ButtonSize.Large, fullWidth = true
        ), disabled = { false }, onClick = onClick
    ) {
        text {
            ::content { text() }
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

fun <A, B> Readable<A>.distinctUntilChanged(map: (A)->B): Readable<B> {
    return object: Readable<B> {
        override var once: B = map(this@distinctUntilChanged.once)
            private set

        override fun addListener(listener: () -> Unit): () -> Unit {
            return this@distinctUntilChanged.addListener {
                val newValue = map(this@distinctUntilChanged.once)
                if(newValue != once) {
                    once = newValue
                    listener()
                }
            }
        }
    }
}
