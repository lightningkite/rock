package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*
import kotlinx.serialization.Serializable

abstract class AuthenticatedScreen : RockScreen {
    override fun ViewContext.render() {
        if (currentUser.once == null) navigator.replace(Login(redirect = navigator.currentPath))

        renderAuthenticated()
    }

    abstract fun ViewContext.renderAuthenticated()
}

class MyApp : RockApp {
    override fun ViewContext.render() {
        withTheme(appTheme) {

//            val list = Property(listOf("1","2","3"))
//
//            box {
//                text("outside")
//                forEach(
//                    data = { list.current },
//                    render = { it ->
//                        text(it)
//                    }
//                )
//                text("outside")
//                button(onClick = {
//                    list.modify { it + "4" }
//                    println(list.once)
//                }) {
//                    text("CLICK")
//                }
//            }

            navigationView(
                showNavigation = {
                    currentUser.current != null
                },
                navigationItems = listOf(
                    NavigationItem(
                        title = "Home", screen = Dashboard(),
                        icon = RockIcon.Home,
                    ),
                    NavigationItem(
                        title = "Favorites", screen = Favorites(),
                        icon = RockIcon.HeartOutlined,
                    ),
                    NavigationItem(
                        title = "Search", screen = Search(),
                        icon = RockIcon.Search
                    ),
                    NavigationItem(
                        title = "Account",
                        screen = Account(),
                        icon = RockIcon.Account
                    ),
                    NavigationItem(
                        title = "Cart",
                        screen = Cart(),
                        icon = RockIcon.Cart
                    )
                ),
                router = Router(
                    routes = listOf(
                        Route(Login.PATH) { _, params -> Login.create(params) },
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
                        Route(Favorites.PATH) { _, _ -> Favorites() },
                        Route(Search.PATH) { _, _ -> Search() },
                        Route(Account.PATH) { _, _ -> Account() },
                    ), fallback = NotFound()
                )
            )
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

fun searchProducts(query: String, category: Category): List<Product> {
    val result = ArrayList<Product>()
    for (product in category.products) {
        if (product.name.contains(query, ignoreCase = true)) {
            result.add(product)
        }
    }
    for (sub in category.subcategories) {
        result.addAll(searchProducts(query, sub))
    }
    return result.map { it.key to it }
        .toMap().values.toList() // filter out duplicates, not necessary if products are unique to a single category
}

@Serializable
data class CartItem(
    val product: Product, val quantity: Int
)

val ViewContext.cartItems by viewContextAddon(
    PersistentProperty<List<CartItem>>(
        "rock.demo.cart",
        listOf(
            CartItem(product = findProduct("ic-surge-device", rootCategory)!!, quantity = 1),
        )
    )
)

val ViewContext.favorites by viewContextAddon(
    PersistentProperty<List<Product>>(
        "rock.demo.favorites",
        listOf(
            findProduct("ic-surge-device", rootCategory)!!,
        )
    )
)

@Serializable
data class AuthenticatedUser(
    val email: String,
)

val ViewContext.currentUser by viewContextAddon(
    PersistentProperty<AuthenticatedUser?>("rock.demo.auth", defaultValue = null)
)
