package com.lightningkite.mppexample

interface RockNavigator {
    var currentPath: String
    fun navigate(
        screen: RockScreen,
        options: NavigationOptions = NavigationOptions()
    )
}

expect class PlatformNavigator(
    router: Router,
    context: ViewContext
) : RockNavigator

class DummyRockNavigator : RockNavigator {
    override var currentPath: String
        get() = throw NotImplementedError()
        set(value) = throw NotImplementedError()

    override fun navigate(screen: RockScreen, options: NavigationOptions) {
        throw NotImplementedError()
    }
}

data class NavigationOptions(
    val transitions: ScreenTransitions? = null,
    val pushState: Boolean = true,
    val reverse: Boolean = false,
)