package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowInfo

expect object AnimationFrame: Listenable
expect object WindowInfo: Readable<WindowInfo>
expect object InForeground: Readable<Boolean>
@ReactiveB
fun ReactiveScope.blockIfBackground() {
    if(!InForeground.current) throw Loading
}