package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewWriter

@Routable("/platform-specific")
object PlatformSpecificScreen : RockScreen {
    override fun ViewWriter.render() {
        platformSpecific()
    }
}

expect fun ViewWriter.platformSpecific()