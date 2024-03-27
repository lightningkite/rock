package com.lightningkite.kiteui.reactive

import com.lightningkite.kiteui.models.WindowStatistics

expect object AnimationFrame: Listenable
expect object WindowInfo: Readable<WindowStatistics>
expect object InForeground: Readable<Boolean>
expect object SoftInputOpen: Readable<Boolean>