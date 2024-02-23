package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.row
import com.lightningkite.rock.views.l2.navLink

@Routable("test")
class TestScreen: RockScreen {
    override fun ViewWriter.render() {
        col {
            row {
                navLink {
                    to = TestScreen()
                    content = "Test"
                    icon = Icon.star
                }
                navLink {
                    to = TestScreen()
                    content = "Test"
                    icon = Icon.star
                    count = 1
                }
                navLink {
                    to = TestScreen()
                    content = "Test"
                    icon = Icon.star
                    count = 0
                }
            }
        }
    }
}