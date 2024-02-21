package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.VideoSource
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NVideo : NView

@JvmInline
value class Video(override val native: NVideo) : RView<NVideo>

@ViewDsl
expect fun ViewWriter.videoActual(setup: Video.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.video(noinline setup: Video.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; videoActual(setup) }
expect var Video.source: VideoSource?
expect val Video.time: Writable<Double>
expect val Video.playing: Writable<Boolean>