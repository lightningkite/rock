package com.lightningkite.mppexampleapp

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import kotlinx.browser.window
import org.w3c.dom.events.Event
import kotlin.time.Duration

actual fun ViewWriter.platformSpecific() {
    val current = Property(0)
    col {
        val showExtra = Property(true)
        button {
            onClick { showExtra.value = !showExtra.value }
            text("Toggle")
//            ::exists { native.assistAnimateHidden(); current.await() != 4 }
        }
        button {
            text("Animation Test Toggle")
            reactiveScope {
                println("Animating? $animationsEnabled")
                exists = !(showExtra.await())
            }
//            ::exists { native.assistAnimateHidden(); current.await() != 4 }
        }
        button {
            onClick { current set 1 }
            text("Animation Test 1")
            reactiveScope { exists = current.await() != 1 }
        }
        button {
            onClick { current set 2 }
            text("Animation Test 2")
            reactiveScope { exists = current.await() != 2 }
        }
        button {
            onClick { current set 3 }
            text("Animation Test 3")
            reactiveScope { exists = current.await() != 3 }
        }
        button {
            onClick { current set 4 }
            text("Animation Test 4")
            reactiveScope { exists = current.await() != 4 }
        }
    }
}
