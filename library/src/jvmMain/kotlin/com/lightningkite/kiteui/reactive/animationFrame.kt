package com.lightningkite.kiteui.reactive

import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.models.WindowStatistics

actual object AnimationFrame: Listenable by Property("")
actual object WindowInfo: Readable<WindowStatistics> by Property(
    WindowStatistics(
        width = Dimension("100%"),
        height = Dimension("100%"),
        density = 1f
    )
)
actual object InForeground: Readable<Boolean> by Property(false)