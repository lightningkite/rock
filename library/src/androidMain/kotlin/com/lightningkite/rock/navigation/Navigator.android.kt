package com.lightningkite.rock.navigation
import com.lightningkite.rock.reactive.Readable
import android.content.Context


actual class PlatformNavigator actual constructor(routes: Routes) : RockNavigator {
    private val localNavigator = LocalNavigator(routes)
    override val dialog: RockNavigator
        get() = localNavigator.dialog
    override val routes: Routes
        get() = localNavigator.routes
    override val currentScreen: Readable<RockScreen?>
        get() = localNavigator.currentScreen
    override val canGoBack: Readable<Boolean>
        get() = localNavigator.canGoBack

    override fun navigate(screen: RockScreen) {
        localNavigator.navigate(screen)
    }

    override fun replace(screen: RockScreen) {
        localNavigator.replace(screen)
    }

    override fun goBack() {
        localNavigator.goBack()
    }

    override fun dismiss() {
        localNavigator.dismiss()
    }

    override val direction: RockNavigator.Direction? = localNavigator.direction

}