package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowInfo

expect object AnimationFrame: Listenable
expect object WindowInfo: Readable<WindowInfo>