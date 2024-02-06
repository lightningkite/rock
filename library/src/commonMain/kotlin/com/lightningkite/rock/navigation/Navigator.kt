package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.*

interface RockNavigator {
    val dialog: RockNavigator
    val routes: Routes
    val currentScreen: Readable<RockScreen?>
    val canGoBack: Readable<Boolean>
    fun navigate(screen: RockScreen)
    fun replace(screen: RockScreen)
    fun saveStack(): Array<String>
    fun ready(): Boolean
    fun reset(screen: RockScreen)
    fun goBack(): Boolean
    fun dismiss(): Boolean
    val direction: Direction?
    enum class Direction { Back, Neutral, Forward }
}

class LocalNavigator(val routesGetter: ()->Routes, dialog: RockNavigator? = null): RockNavigator {
    constructor(routes: Routes, dialog: RockNavigator? = null):this({routes}, dialog)
    override val routes: Routes by lazy { routesGetter() }
    override val dialog: RockNavigator = dialog ?: this
    override var direction: RockNavigator.Direction? = null
        protected set
    val stack = Property<List<RockScreen>>(listOf())
    override val canGoBack: Readable<Boolean>
        get() = shared { stack.await().size > 1 }
    override val currentScreen: Readable<RockScreen?>
        get() = shared { stack.await().lastOrNull() }
    override fun goBack(): Boolean {
        direction = RockNavigator.Direction.Back
        if(stack.value.size > 1) {
            stack.value = stack.value.dropLast(1)
            return true
        } else return false
    }
    override fun dismiss(): Boolean {
        direction = RockNavigator.Direction.Back
        if(stack.value.isNotEmpty()) {
            stack.value = stack.value.dropLast(1)
            return true
        } else return false
    }
    override fun navigate(screen: RockScreen) {
        direction = RockNavigator.Direction.Forward
        stack.value = stack.value.plus(screen)
    }
    override fun replace(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        stack.value = stack.value.dropLast(1).plus(screen)
    }
    override fun saveStack(): Array<String> =
        stack.value.mapNotNull { it::class.qualifiedName }.toTypedArray()
    override fun ready(): Boolean = stack.value.isNotEmpty()
    override fun reset(screen: RockScreen) {
        direction = RockNavigator.Direction.Neutral
        stack.value = listOf(screen)
    }
}

expect object PlatformNavigator : RockNavigator {
    override var routes: Routes
}
