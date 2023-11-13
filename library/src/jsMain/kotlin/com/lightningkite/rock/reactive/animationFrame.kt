package com.lightningkite.rock.reactive

import com.lightningkite.rock.GeolocationCoordinates
import com.lightningkite.rock.models.WindowInfo
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Window
import org.w3c.dom.events.Event

actual object AnimationFrame: Listenable {
    override fun addListener(listener: () -> Unit): () -> Unit {
        var end = false
        var sub: (Double) -> Unit = {}
        sub = label@{
            listener()
            if (end) {
                // Done!
            } else {
                window.requestAnimationFrame(sub)
            }
        }
        window.requestAnimationFrame(sub)
        return {
            end = true
        }
    }
}
actual object WindowInfo: Readable<WindowInfo> by Property(
    WindowInfo(
        width = window.innerWidth,
        height = window.innerHeight,
        density = 1f
    )
)
actual object InForeground: Readable<Boolean> {
    override val once: Boolean get() = (document.asDynamic().visibilityState as? String) != "hidden"

    override fun addListener(listener: () -> Unit): () -> Unit {
        val l = { _: Event -> listener(); Unit }
        document.addEventListener("visibilitychange", l)
        return { document.removeEventListener("visibilitychange", l) }
    }
}