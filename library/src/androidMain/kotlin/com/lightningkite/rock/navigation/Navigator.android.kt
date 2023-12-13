package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.Readable

actual class PlatformNavigator actual constructor(routes: Routes) : RockNavigator {
    override val dialog: RockNavigator
        get() = TODO("Not yet implemented")
    override val routes: Routes
        get() = TODO("Not yet implemented")
    override val currentScreen: Readable<RockScreen?>
        get() = TODO("Not yet implemented")
    override val canGoBack: Readable<Boolean>
        get() = TODO("Not yet implemented")

    override fun navigate(screen: RockScreen) {
        TODO("Not yet implemented")
    }

    override fun replace(screen: RockScreen) {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun dismiss() {
        TODO("Not yet implemented")
    }

    override val direction: RockNavigator.Direction?
        get() = TODO("Not yet implemented")

}