package com.lightningkite.mppexample

interface RockNavigator {
    var currentPath: String
    fun navigate(screen: RockScreen)
    fun replace(screen: RockScreen)
    fun goBack()
}

expect class PlatformNavigator(
    router: Router,
    onScreenChanged: (RockScreen, Boolean) -> Unit
) : RockNavigator

class DummyRockNavigator : RockNavigator {
    override var currentPath: String
        get() = throw NotImplementedError()
        set(value) = throw IllegalStateException("Cannot navigate without a navigator.")

    override fun navigate(screen: RockScreen) {
        throw IllegalStateException("Cannot navigate without a navigator.")
    }

    override fun replace(screen: RockScreen) {
        throw IllegalStateException("Cannot navigate without a navigator.")
    }

    override fun goBack() {
        throw IllegalStateException("Cannot navigate without a navigator.")
    }
}

data class NavigationOptions(
    val transitions: ScreenTransitions? = null,
    val pushState: Boolean = true,
    val reverse: Boolean = false,
)
