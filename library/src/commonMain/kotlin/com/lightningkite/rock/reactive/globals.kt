package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowStatistics

expect object AnimationFrame: Listenable
expect object WindowInfo: Readable<WindowStatistics>
expect object InForeground: Readable<Boolean>
expect object SoftInputOpen: Readable<Boolean>