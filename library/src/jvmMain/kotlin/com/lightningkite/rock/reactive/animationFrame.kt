package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowInfo
import org.w3c.dom.events.Event

actual object AnimationFrame: Listenable by Property("")
actual object WindowInfo: Readable<WindowInfo> by Property(
    WindowInfo(
        width = 1920,
        height = 1080,
        density = 1f
    )
)
actual object InForeground: Readable<Boolean> by Property(false)