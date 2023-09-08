package com.lightningkite.mppexample

@ViewDsl
fun ViewContext.icon(
    icon: Icon,
    width: Dimension = 24.px,
    height: Dimension = 24.px,
    color: Color? = null,
) {
    image {
        source = icon.toVector(width = width, height = height, color = color ?: theme.normal.foreground.closestColor())
    }
}

@ViewDsl
fun ViewContext.icon(
    icon: ReactiveScope.() -> Icon,
    width: Dimension = 24.px,
    height: Dimension = 24.px,
    color: (ReactiveScope.() -> Color)? = null,
) {
    image {
        ::source {
            icon().toVector(
                width = width,
                height = height,
                color = color?.let { it() } ?: theme.normal.foreground.closestColor())
        }
    }
}

@ViewDsl
fun ViewContext.icon(
    icon: ReactiveScope.() -> Icon,
    width: Dimension = 24.px,
    height: Dimension = 24.px,
    color: Color? = null,
) = icon(icon = icon, width = width, height = height, color = { color ?: theme.normal.foreground.closestColor() })

enum class RockIcon : Icon {
    Home, HeartOutlined, HeartFilled, Search, Account, Cart;

    override fun toVector(
        width: Dimension,
        height: Dimension,
        color: Color,
    ): ImageVector {
        return when (this) {
            Home -> ImageVector(
                width = width, height = height, paths = listOf(
                    ImageVector.Path(
                        fillColor = color,
                        path = "M21.4498 10.275L11.9998 3.1875L2.5498 10.275L2.9998 11.625H3.7498V20.25H20.2498V11.625H20.9998L21.4498 10.275ZM5.2498 18.75V10.125L11.9998 5.0625L18.7498 10.125V18.75H14.9999V14.3333L14.2499 13.5833H9.74988L8.99988 14.3333V18.75H5.2498ZM10.4999 18.75H13.4999V15.0833H10.4999V18.75Z",
                    )
                )
            )

            HeartOutlined -> ImageVector(
                width = width, height = width, paths = listOf(
                    ImageVector.Path(
                        strokeColor = color,
                        strokeWidth = 2.0,
                        path = "M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z",
                    )
                )
            )

            HeartFilled -> ImageVector(
                width = width, height = width, paths = listOf(
                    ImageVector.Path(
                        fillColor = color,
                        path = "M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z",
                    )
                )
            )

            Search -> ImageVector(
                width = width, height = height, paths = listOf(
                    ImageVector.Path(
                        strokeColor = color,
                        strokeWidth = 2.0,
                        path = "M15.7955 15.8111L21 21M18 10.5C18 14.6421 14.6421 18 10.5 18C6.35786 18 3 14.6421 3 10.5C3 6.35786 6.35786 3 10.5 3C14.6421 3 18 6.35786 18 10.5Z",
                    )
                )
            )

            Account -> ImageVector(
                viewBoxMinX = -16, viewBoxMinY = -16,
                viewBoxWidth = 128, viewBoxHeight = 128,
                width = width, height = height, paths = listOf(
                    ImageVector.Path(
                        fillColor = color,
                        path = "M69.3677,51.0059a30,30,0,1,0-42.7354,0A41.9971,41.9971,0,0,0,0,90a5.9966,5.9966,0,0,0,6,6H90a5.9966,5.9966,0,0,0,6-6A41.9971,41.9971,0,0,0,69.3677,51.0059ZM48,12A18,18,0,1,1,30,30,18.02,18.02,0,0,1,48,12ZM12.5977,84A30.0624,30.0624,0,0,1,42,60H54A30.0624,30.0624,0,0,1,83.4023,84Z",
                    )
                )
            )

            Cart -> ImageVector(
                width = width, height = height, paths = listOf(
                    ImageVector.Path(
                        strokeColor = color,
                        strokeWidth = 2.0,
                        path = "M6.29977 5H21L19 12H7.37671M20 16H8L6 3H3M9 20C9 20.5523 8.55228 21 8 21C7.44772 21 7 20.5523 7 20C7 19.4477 7.44772 19 8 19C8.55228 19 9 19.4477 9 20ZM20 20C20 20.5523 19.5523 21 19 21C18.4477 21 18 20.5523 18 20C18 19.4477 18.4477 19 19 19C19.5523 19 20 19.4477 20 20Z",
                    )
                )
            )
        }
    }
}
