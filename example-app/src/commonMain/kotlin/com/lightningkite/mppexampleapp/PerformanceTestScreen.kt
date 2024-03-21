package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("performance")
object PerformanceTestScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 { content = "Performance Test" }
            val items = Property((0..5000).toList())
            reactiveScope {
                var i = 0
                while(true) {
                    delay(100L)
                    items.value = (i..(5000 + i)).toList()
                    i++
                }
            }
            scrolls - col  {
                forEach(items) {
                    row {
                        icon { source = Icon.add }
                        text("This is a test")
                    }
                }
            }
        }
    }
}