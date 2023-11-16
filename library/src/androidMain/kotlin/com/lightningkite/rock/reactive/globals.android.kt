package com.lightningkite.rock.reactive

import com.lightningkite.rock.models.WindowInfo

actual object AnimationFrame : Listenable {
    override fun addListener(listener: () -> Unit): () -> Unit {
        TODO("Not yet implemented")
    }
}

actual object WindowInfo: Readable<WindowInfo> {
    override fun addListener(listener: () -> Unit): () -> Unit {
        TODO("Not yet implemented")
    }

    override suspend fun awaitRaw(): WindowInfo {
        TODO("Not yet implemented")
    }
}

actual object InForeground: Readable<Boolean> {
    override fun addListener(listener: () -> Unit): () -> Unit {
        TODO("Not yet implemented")
    }

    override suspend fun awaitRaw(): Boolean {
        TODO("Not yet implemented")
    }
}