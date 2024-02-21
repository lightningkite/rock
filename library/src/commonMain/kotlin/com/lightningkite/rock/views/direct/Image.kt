package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ImageScaleType
import com.lightningkite.rock.models.ImageSource
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NImage : NView

@JvmInline
value class Image(override val native: NImage) : RView<NImage>

@ViewDsl
expect fun ViewWriter.imageActual(setup: Image.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.image(noinline setup: Image.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; imageActual(setup) }
expect var Image.source: ImageSource?
expect var Image.scaleType: ImageScaleType
expect var Image.description: String?

@ViewDsl
expect fun ViewWriter.zoomableImageActual(setup: Image.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.zoomableImage(noinline setup: Image.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; zoomableImageActual(setup) }