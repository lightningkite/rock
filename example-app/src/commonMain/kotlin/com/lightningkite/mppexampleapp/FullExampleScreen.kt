package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.RootScreen
import com.lightningkite.kiteui.QueryParameter
import com.lightningkite.kiteui.Routable
import com.lightningkite.kiteui.navigation.KiteUiScreen
import com.lightningkite.kiteui.reactive.Property
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.reactive.bind
import com.lightningkite.kiteui.reactive.invoke
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.direct.*

@Routable("full-screen")
class FullExampleScreen: KiteUiScreen, UseFullScreen {

    override fun ViewWriter.render() = col {
        h1 { content = "Full Screen!" }
        link {
            text { content = "Go back to root" }
            to = RootScreen
        }
    }
}
interface UseFullScreen