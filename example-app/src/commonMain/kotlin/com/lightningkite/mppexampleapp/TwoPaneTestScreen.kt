package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("twopane")
object TwoPaneTestScreen : RockScreen {
    override fun ViewWriter.render() {
        this.twoPane(
            setup = {},
            leftPaneSize = 25.rem,
            rightPaneMinSize = 25.rem,
            right = {
                col {
                    repeat(100) {
                        text("Right Pane")
                    }
                }
            },
        ) {
            col {
                repeat(100) {
                    text("Left Pane")
                }
            }
        }
    }
}