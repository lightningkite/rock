package com.lightningkite.kiteui.navigation

import com.lightningkite.kiteui.reactive.*

interface KiteUiNavigator {
    val dialog: KiteUiNavigator
    val routes: Routes
    val currentScreen: Readable<KiteUiScreen?>
    val canGoBack: Readable<Boolean>
    fun navigate(screen: KiteUiScreen)
    fun replace(screen: KiteUiScreen)
    fun reset(screen: KiteUiScreen)
    fun goBack(): Boolean
    fun dismiss(): Boolean
    fun isStackEmpty(): Boolean
    fun saveStack(): Array<String>
    fun restoreStack(navStack: Array<String>)
    val direction: Direction?
    enum class Direction { Back, Neutral, Forward }
}

class LocalNavigator(val routesGetter: ()->Routes, dialog: KiteUiNavigator? = null): KiteUiNavigator {
    constructor(routes: Routes, dialog: KiteUiNavigator? = null):this({routes}, dialog)
    override val routes: Routes by lazy { routesGetter() }
    override val dialog: KiteUiNavigator = dialog ?: this
    override var direction: KiteUiNavigator.Direction? = null
        protected set
    val stack = Property<List<KiteUiScreen>>(listOf())
    override val canGoBack: Readable<Boolean>
        get() = shared { stack.await().size > 1 }
    override val currentScreen: Readable<KiteUiScreen?>
        get() = shared { stack.await().lastOrNull() }
    override fun goBack(): Boolean {
        direction = KiteUiNavigator.Direction.Back
        if(stack.value.size > 1) {
            stack.value = stack.value.dropLast(1)
            return true
        } else return false
    }
    override fun dismiss(): Boolean {
        direction = KiteUiNavigator.Direction.Back
        if(stack.value.isNotEmpty()) {
            stack.value = stack.value.dropLast(1)
            return true
        } else return false
    }
    override fun navigate(screen: KiteUiScreen) {
        direction = KiteUiNavigator.Direction.Forward
        stack.value = stack.value.plus(screen)
    }
    override fun replace(screen: KiteUiScreen) {
        direction = KiteUiNavigator.Direction.Neutral
        stack.value = stack.value.dropLast(1).plus(screen)
    }
    override fun reset(screen: KiteUiScreen) {
        direction = KiteUiNavigator.Direction.Neutral
        stack.value = listOf(screen)
    }
    override fun isStackEmpty() = stack.value.isEmpty()
    override fun saveStack(): Array<String> =
        stack.value.mapNotNull { routes.render(it)?.urlLikePath?.render() }.toTypedArray()
    override fun restoreStack(navStack: Array<String>) {
        direction = KiteUiNavigator.Direction.Forward
        stack.value = navStack.map(UrlLikePath::fromUrlString).mapNotNull(routes::parse)
    }
}

expect object PlatformNavigator : KiteUiNavigator {
    override var routes: Routes
}
