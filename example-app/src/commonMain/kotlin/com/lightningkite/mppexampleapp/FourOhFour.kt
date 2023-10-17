package com.lightningkite.mppexampleapp

import com.lightningkite.rock.FallbackRoute
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.content
import com.lightningkite.rock.views.direct.h1
import com.lightningkite.rock.views.direct.text

@FallbackRoute
class FourOhFour() : RockScreen {
    override fun ViewContext.render() = col {
        h1 { content = "Not Found" }
        text { content = "Sorry, couldn't find what you were looking for." }
    }
}