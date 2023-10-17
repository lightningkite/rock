package com.lightningkite.rock.reactive

import com.lightningkite.rock.GeolocationCoordinates
import com.lightningkite.rock.models.WindowInfo
import kotlinx.browser.window
import org.w3c.dom.Window

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