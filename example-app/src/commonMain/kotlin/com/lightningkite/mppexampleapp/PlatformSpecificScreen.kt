package com.lightningkite.mppexampleapp

import com.lightningkite.kiteui.Routable
import com.lightningkite.kiteui.navigation.KiteUiScreen
import com.lightningkite.kiteui.views.ViewWriter

@Routable("/platform-specific")
object PlatformSpecificScreen : KiteUiScreen {
    override fun ViewWriter.render() {
        platformSpecific()
    }
}

expect fun ViewWriter.platformSpecific()