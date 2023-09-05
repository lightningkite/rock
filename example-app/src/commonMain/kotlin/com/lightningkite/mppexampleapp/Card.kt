package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.card(
    header: String,
    description: String? = null,
    background: ImageSource? = null,
    image: ImageSource? = null,
    sizeConstraints: SizeConstraints = SizeConstraints(),
    onClick: (() -> Unit)? = null,
) {
    box {
        stack {
            row {
                if (image != null) image {
                    source = image
                    scaleType = ImageMode.Fit
                } in sizedBox(
                    SizeConstraints(
                        maxWidth = 64.px,
                        maxHeight = 64.px,
                        minWidth = 64.px,
                        minHeight = 64.px,
                    )
                ) in alignCenter() in padding(Insets(right = 16.px))
                column {
                    h5 { content = header } in padding(Insets(bottom = 4.px))
                    text { content = description ?: "" }
                } in alignCenter()
                if (onClick != null) {
                    space() in weight(1f)
                    text { content = "->" } in stackCenter() in stackRight()
                }
            } in padding(Insets.symmetric(horizontal = 12.px))
            if (background != null) image {
                alpha = 0.25
                source = background
                scaleType = ImageMode.Crop
            } in fullWidth() in sizedBox(
                SizeConstraints(
                    maxHeight = 62.px,
                )
            ) in stackRight()
        }
    } in interactive(
        background = Background(
            fill = Color.white, stroke = Color.gray, strokeWidth = 1.px, corners = CornerRadii(8.px)
        ),
        hoverBackground = if (onClick == null) null else Background(
            fill = Color.gray(0.9f),
            stroke = Color.gray,
            strokeWidth = 1.px,
        ),
        downBackground = if (onClick == null) null else Background(
            fill = Color.gray(0.8f),
            stroke = Color.gray,
            strokeWidth = 1.px,
        ),
    ) in sizedBox(
        sizeConstraints
    ) in margin(8.px) in (if (onClick == null) ViewWrapper else clickable(onClick = onClick)) in padding(
        if (background != null) Insets.zero() else Insets(12.px)
    )
}


fun ViewContext.cartCard(
    product: Product
) {
    val quantityInput = Property(cartItems.once.find { it.product == product }?.quantity ?: 0)

    reactiveScope {
        val newQuantity = quantityInput.current
//        println("Rewriting")
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
                }
            } in alignCenter()
        } in padding(Insets.symmetric(horizontal = 12.px))
    } in background(
        background = Background(
            fill = Color.white, stroke = Color.gray, strokeWidth = 1.px, corners = CornerRadii(8.px)
        ),
    ) in margin(8.px)
}
