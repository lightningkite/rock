package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.RootScreen
import com.lightningkite.rock.QueryParameter
import com.lightningkite.rock.Routable
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.bind
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*

@Routable("full-screen")
class FullExampleScreen: RockScreen, UseFullScreen {

    override fun ViewWriter.render() = col {
        h1 { content = "Full Screen!" }
        link {
            text { content = "Go back to root" }
            to = RootScreen
        }
    }
}
interface UseFullScreen