package com.lightningkite.rock.reactive

import com.lightningkite.rock.afterTimeout
import com.lightningkite.rock.models.WindowInfo
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

actual object AnimationFrame: Listenable {
    override fun addListener(listener: () -> Unit): () -> Unit {
        var end = false
        var sub: (Double) -> Unit = {}
        sub = label@{
            listener()
            if (end) {
                // Done!
            } else {
                afterTimeout(16) { sub(0.0016) }
            }
        }
        afterTimeout(16) { sub(0.0016) }
        return {
            end = true
        }
    }
}
@OptIn(ExperimentalForeignApi::class)
actual object WindowInfo: Readable<WindowInfo> by Property(
    WindowInfo(
        width = UIScreen.mainScreen.bounds.useContents { size.width }.toInt(),
        height = UIScreen.mainScreen.bounds.useContents { size.height }.toInt(),
        density = UIScreen.mainScreen.scale.toFloat()
    )
)
internal val InForegroundProperty = Property(true)
actual object InForeground: Readable<Boolean> by InForegroundProperty
