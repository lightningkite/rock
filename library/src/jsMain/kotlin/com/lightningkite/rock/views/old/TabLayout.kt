package com.lightningkite.rock.views.old

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.ReactiveScope
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.navigator


@ViewDsl
actual fun ViewContext.tabLayout(tabs: ReactiveScope.() -> List<NavigationTab>, exists: ReactiveScope. () -> Boolean): Unit {
    box {
        ::exists { exists() }
        forEach(
            data = tabs,
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
}

private fun ViewContext.navButton(
    text: String,
    icon: Icon,
    screen: RockScreen
) {
    button(
        options = ButtonOptions(variant = ButtonVariant.Text),
        disabled = { false },
        onClick = { navigator.navigate(screen) }
    ) {
        row {
            gravity = RowGravity.Center
            image {
                ::source {
                    icon.toVector(
                        width = 32.px,
                        height = 32.px,
                        color = theme.normal.foreground.closestColor(),
                    )
                }
            } in padding(Insets(right = 6.px))
            text { content = text }
        }
    }
}
