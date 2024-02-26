package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ImageScaleType
import com.lightningkite.rock.models.ImageSource
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NImageView : NView

@JvmInline
value class ImageView(override val native: NImageView) : RView<NImageView>

@ViewDsl
expect fun ViewWriter.imageActual(setup: ImageView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.image(noinline setup: ImageView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; imageActual(setup) }
expect var ImageView.source: ImageSource?
expect var ImageView.scaleType: ImageScaleType
expect var ImageView.description: String?

@ViewDsl
expect fun ViewWriter.zoomableImageActual(setup: ImageView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.zoomableImage(noinline setup: ImageView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; zoomableImageActual(setup) }