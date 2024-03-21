package com.lightningkite.rock.views.direct

import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLVideoElement
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.url.URL
import org.w3c.files.Blob

@Suppress("ACTUAL_WITHOUT_EXPECT") 
actual class NVideo(override val js: HTMLVideoElement): NView2<HTMLVideoElement>()

@ViewDsl
actual inline fun ViewWriter.videoActual(crossinline setup: Video.() -> Unit): Unit =
    themedElement("video", ::NVideo) {
        setup(Video(this))
        js.controls = true
    }
actual inline var Video.source: VideoSource?
    get() = TODO()
    set(value) {
        when(value) {
            null -> native.js.src = ""
            is VideoRemote -> native.js.src = value.url
            is VideoRaw -> TODO()
            is VideoResource -> native.js.src = PlatformNavigator.basePath + value.relativeUrl
            is VideoLocal -> native.js.src = URL.createObjectURL(value.file)
            else -> {}
        }
    }
actual val Video.time: Writable<Double> get() = native.js.vprop("timeupdate", { this.currentTime }, { this.currentTime = it })
actual val Video.playing: Writable<Boolean> get() = native.js.vprop("timeupdate", { !this.paused }, { if(it) play() else pause() })