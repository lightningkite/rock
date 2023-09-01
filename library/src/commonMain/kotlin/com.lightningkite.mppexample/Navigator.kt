package com.lightningkite.mppexample

interface RockNavigator {
    var currentPath: String
    fun navigate(screen: RockScreen)
    fun replace(screen: RockScreen)
}

expect class PlatformNavigator(
    router: Router,
    onScreenChanged: (RockScreen, Boolean) -> Unit
) : RockNavigator

class DummyRockNavigator : RockNavigator {
    override var currentPath: String
        get() = throw NotImplementedError()
        set(value) = throw NotImplementedError()

    override fun navigate(screen: RockScreen) {
        throw NotImplementedError()
    }

    override fun replace(screen: RockScreen) {
        throw NotImplementedError()
    }
}

data class NavigationOptions(
    val transitions: ScreenTransitions? = null,
    val pushState: Boolean = true,
    val reverse: Boolean = false,
)
