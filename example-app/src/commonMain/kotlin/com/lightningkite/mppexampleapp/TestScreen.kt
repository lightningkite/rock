package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.row

@Routable("test")
class TestScreen: RockScreen {
    override fun ViewWriter.render() {
        col {
            row {
            }
        }
    }
}