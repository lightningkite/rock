package com.lightningkite.kiteui.reactive

import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.models.WindowStatistics

actual object AnimationFrame : Listenable {
    fun frame() {
        listeners.toList().forEach { it() }
    }

    private val listeners = ArrayList<()->Unit>()
    override fun addListener(listener: () -> Unit): () -> Unit {
        listeners.add(listener)
        return { listeners.remove(listener) }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual object WindowInfo: BaseImmediateReadable<WindowStatistics>(WindowStatistics(Dimension(1920f), Dimension(1080f), 1f))

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual object InForeground: BaseImmediateReadable<Boolean>(true)

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual object SoftInputOpen : BaseImmediateReadable<Boolean>(false)