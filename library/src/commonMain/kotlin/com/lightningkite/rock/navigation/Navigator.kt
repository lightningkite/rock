package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.*

interface RockNavigator {
    val dialog: RockNavigator
    val routes: Routes
    val currentScreen: Readable<RockScreen?>
    val canGoBack: Readable<Boolean>
    fun navigate(screen: RockScreen)
    fun replace(screen: RockScreen)
    fun goBack()
    fun dismiss()
    fun notifyParamUpdate()
    val direction: Direction?
    enum class Direction { Back, Neutral, Forward }
}

class LocalNavigator(override val routes: Routes, dialog: RockNavigator? = null): RockNavigator {
    override val dialog: RockNavigator = dialog ?: this
    override var direction: RockNavigator.Direction? = null
        private set
    val stack = Property(listOf((routes.parse(UrlLikePath.EMPTY) ?: routes.fallback)))
    override val canGoBack: Readable<Boolean>
        get() = shared { stack.await().size > 1 }
    override val currentScreen: Readable<RockScreen?>
        get() = shared { stack.await().lastOrNull() }
    override fun goBack() {
        direction = RockNavigator.Direction.Back
        if(stack.value.size > 1) stack.value = stack.value.dropLast(1)
    }
    override fun dismiss() {
        direction = RockNavigator.Direction.Back
        if(stack.value.isNotEmpty()) stack.value = stack.value.dropLast(1)
    }
    override fun notifyParamUpdate() {}
    override fun navigate(screen: RockScreen) {
        direction = RockNavigator.Direction.Forward
        stack.value = stack.value.plus(screen)
    }
    override fun replace(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        stack.value = stack.value.dropLast(1).plus(screen)
    }
}

expect class PlatformNavigator(
    routes: Routes
) : RockNavigator
