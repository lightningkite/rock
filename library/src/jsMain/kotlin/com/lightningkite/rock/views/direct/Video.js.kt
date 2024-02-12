package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.dom.HTMLVideoElement
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.url.URL
import org.w3c.files.Blob

@Suppress("ACTUAL_WITHOUT_EXPECT") 
actual typealias NVideo = org.w3c.dom.HTMLVideoElement

@ViewDsl
actual fun ViewWriter.video(setup: Video.() -> Unit): Unit =
    themedElement<NVideo>("video") {
        setup(Video(this))
        controls = true
    }
actual inline var Video.source: VideoSource?
    get() = TODO()
    set(value) {
        when(value) {
            null -> native.src = ""
            is VideoRemote -> native.src = value.url
            is VideoRaw -> native.src = URL.createObjectURL(Blob(arrayOf(value.data)))
            is VideoResource -> native.src = value.relativeUrl
            is VideoLocal -> native.src = URL.createObjectURL(value.file)
            else -> {}
        }
    }
actual val Video.time: Writable<Double> get() = native.vprop("timeupdate", { this.currentTime }, { this.currentTime = it })
actual val Video.playing: Writable<Boolean> get() = native.vprop("timeupdate", { !this.paused }, { if(it) play() else pause() })