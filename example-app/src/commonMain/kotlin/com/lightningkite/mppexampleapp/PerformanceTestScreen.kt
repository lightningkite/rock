package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("performance")
object PerformanceTestScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 { content = "Performance Test" }
            text("This screen is hammering the UI by adding and removing thousands of views and updating content.")
            val items = Property((0..5000).toList())
            val property = Property(0)
            reactiveScope {
                var i = 0
                while(true) {
                    delay(400L)
                    items.value = (i..(5000 + i)).toList()
                    i++
                }
            }
            reactiveScope {
                while(true) {
                    delay(50L)
                    property.value++
                }
            }
            scrolls - col  {
                forEach(items) {
                    row {
                        icon { source = Icon.add }
                        text { ::content { property.await().toString() } }
                    }
                }
            }
        }
    }
}