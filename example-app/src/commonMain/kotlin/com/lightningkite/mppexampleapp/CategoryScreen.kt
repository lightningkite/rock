package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

data class Category(
    val name: String,
    val key: String,
    val image: String,
    val subcategories: List<Category> = listOf(),
    val products: List<Product> = listOf()
)

open class CategoryScreen(
    private val category: Category
) : RockScreen {
    override fun ViewContext.render() {
        box {
            withTheme(theme.primaryTheme()) {
                h3 {
                    content = category.name
                } in background(theme.normal.background)
            } in margin(Insets(bottom = 16.px))

            forEach(
                data = { category.subcategories },
                render = { it ->
                    card(
                        header = it.name,
                        background = ImageRemote(it.image),
                        onClick = {
                            navigator.navigate(CategoryScreen(it))
                        },
                        sizeConstraints = SizeConstraints(
                            maxHeight = 64.px,
                        )
                    )
                })

            forEach(
                data = { category.products },
                render = { it ->
                    card(
                        header = it.name,
                        description = it.description,
                        image = ImageRemote(it.image),
                        onClick = {
                            navigator.navigate(ProductScreen(it))
                        }
                    )
                }
            )
        } in fullHeight() in scrolls()
    }

    override fun createPath(): String = "/categories/${category.key}"

    companion object {
        const val PATH = "/categories/{categoryKey}"
    }
}