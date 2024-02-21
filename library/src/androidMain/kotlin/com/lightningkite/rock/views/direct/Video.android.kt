package com.lightningkite.rock.views.direct

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.AnimationFrame
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT") 
actual typealias NVideo = PlayerView

@ViewDsl
actual fun ViewWriter.videoActual(setup: Video.() -> Unit): Unit = viewElement(factory = ::PlayerView, wrapper = ::Video) {
    handleTheme(native, viewDraws = true, viewLoads = true)
    native.player = ExoPlayer.Builder(context).build()
    setup(this)
}
actual inline var Video.source: VideoSource?
    get() = TODO()
    set(value) {
        when(value) {
            null -> {
                native.player!!.stop()
                native.player!!.clearMediaItems()
            }
            is VideoRemote -> {
                native.player!!.setMediaItem(MediaItem.fromUri(value.url))
                native.player!!.prepare()
            }
            is VideoRaw -> {
                TODO()
            }
            is VideoResource -> {
                native.player!!.setMediaItem(MediaItem.fromUri("android.resource://${native.context.packageName}/${value.resource}"))
                native.player!!.prepare()
            }
            is VideoLocal -> {
                native.player!!.setMediaItem(MediaItem.fromUri(value.file.uri))
                native.player!!.prepare()
            }
            else -> {}
        }
    }
actual val Video.time: Writable<Double> get() = object: Writable<Double> {
    override suspend fun set(value: Double) {
        native.player!!.seekTo((value * 1000.0).toLong())
    }

    override suspend fun awaitRaw(): Double {
        return native.player!!.currentPosition / 1000.0
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        var remover: (()->Unit)? = null
        val l = object: Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if(isPlaying) remover = AnimationFrame.addListener(listener)
                else {
                    remover?.invoke()
                    remover = null
                }
            }
        }
        native.player!!.addListener(l)
        return { native.player!!.removeListener(l) }
    }
}
actual val Video.playing: Writable<Boolean> get() = object: Writable<Boolean> {
    override suspend fun set(value: Boolean) {
        if(value) {
            native.player!!.play()
        } else {
            native.player!!.pause()
        }
    }

    override suspend fun awaitRaw(): Boolean {
        return native.player!!.isPlaying
    }

    override fun addListener(listener: () -> Unit): () -> Unit {
        val l = object: Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                listener()
            }
        }
        native.player!!.addListener(l)
        return { native.player!!.removeListener(l) }
    }
}