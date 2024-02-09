package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.VideoSource
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT") actual typealias NVideo = HTMLElement
@ViewDsl
actual fun ViewWriter.video(setup: Video.() -> Unit): Unit = todo("Video")
actual inline var Video.source: VideoSource
    get() = TODO()
    set(value) { }
actual val Video.time: Writable<Double>
    get() = TODO("Not yet implemented")
actual val Video.playing: Writable<Boolean>
    get() = TODO("Not yet implemented")