package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*


fun ViewContext.productCard(
    product: Product
) {
    listTile(onClick = {
        navigator.navigate(ProductScreen(product))
    }) {
        row {
            image {
                source = ImageRemote(product.image)
            } in sizedBox(
                SizeConstraints(
                    minWidth = 64.px,
                    maxWidth = 64.px,
                )
            ) in nativeBackground(Background(corners = CornerRadii(4.px)))
            column {
                h6 { content = product.name } in padding(bottom = 4.px)
                caption { content = product.description }
            } in alignCenter() in padding(left = 16.px)
        }
    }
}

fun ViewContext.cartCard(
    product: Product
) {
    val quantityInput = Property(cartItems.once.find { it.product == product }?.quantity ?: 0)

    reactiveScope {
        val newQuantity = quantityInput.current
        cartItems.modify { list ->
            list.map { item ->
                if (item.product.key == product.key) {
                    item.copy(quantity = newQuantity)
                } else {
                    item
                }
            }
        }
    }

    box {
        row {
            image {
                source = ImageRemote(product.image)
                scaleType = ImageMode.Fit
            } in sizedBox(
                SizeConstraints(
                    maxWidth = 64.px,
                    maxHeight = 64.px,
                    minWidth = 64.px,
                    minHeight = 64.px,
                )
            ) in alignCenter() in padding(Insets(right = 16.px))
            h5 { content = product.name } in padding(Insets(bottom = 4.px)) in alignCenter()
            space() in weight(1f)
            column {
                integerInput(
                    label = "Quantity",
                    value = quantityInput,
                    min = 1
                )
                button(onClick = {
                    cartItems set cartItems.once.filter { it.product.key != product.key }
                }) {
                    text { content = "Remove" }
                } in alignRight()
            } in alignCenter()
        } in padding(Insets.symmetric(horizontal = 12.px))
    } in background(
        background = Background(
            fill = Color.white, stroke = Color.gray, strokeWidth = 1.px, corners = CornerRadii(8.px)
        ),
    ) in margin(8.px)
}
