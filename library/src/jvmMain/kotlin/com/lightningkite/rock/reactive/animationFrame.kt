package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowStatistics

actual object AnimationFrame: Listenable by Property("")
actual object WindowInfo: Readable<WindowStatistics> by Property(
    WindowStatistics(
        width = 1920,
        height = 1080,
        density = 1f
    )
)
actual object InForeground: Readable<Boolean> by Property(false)