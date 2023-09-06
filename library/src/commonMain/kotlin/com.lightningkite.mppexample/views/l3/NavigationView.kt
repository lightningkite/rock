package com.lightningkite.mppexample

data class NavigationItem(
    val title: String,
    val screen: RockScreen,
    val icon: Icon? = null,
)

fun ViewContext.navigationView(
    router: Router,
    navigationItems: List<NavigationItem>,
    showNavigation: ReactiveScope.() -> Boolean
) {
    column {
        box {
            routerView(router)
        } in background(paint = Color.fromHex(0xfafafa), padding = Insets.none) in weight(1f)

        box {
            ::exists { showNavigation() }

            forEach(
                direction = ForEachDirection.Horizontal,
                data = { navigationItems },
                render = { it ->
                    navButton(
                        text = it.title, icon = it.icon, screen = it.screen
                    )
                },
                separator = { space() in weight(1f) }
            ) in fullWidth()

        } in background(paint = Color.white) in sizedBox(
            SizeConstraints(
                minHeight = 96.px, maxHeight = 96.px
            )
        )
    } in fullWidth() in fullHeight()
}

private fun ViewContext.navButton(
    text: String,
    icon: Icon?,
    screen: RockScreen
) {
    button(
        options = ButtonOptions(variant = ButtonVariant.Text),
        disabled = { false },
        onClick = { navigator.navigate(screen) }
    ) {
        row {
            gravity = RowGravity.Center
            if (icon != null)
                image {
                    ::source {
                        icon.toVector(
                            width = 32.px,
                            height = 32.px,
                            strokeColor = theme.normal.foreground.closestColor(),
                            strokeWidth = 1.0,
                            fillColor = theme.normal.foreground.closestColor(),
                        )
                    }
                } in padding(Insets(right = 6.px))
            text { content = text }
        }
    }
}
