package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.models.ImageRaw
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.contracts.*

expect class NImageCrop : NView

expect class ImageCrop(native: NImageCrop) : RView<NImageCrop> {
    override val native: NImageCrop
    var source: ImageLocal?
    suspend fun crop(): ImageRaw?
}

@ViewDsl
expect fun ViewWriter.imageCropActual(setup: ImageCrop.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.imageCrop(noinline setup: ImageCrop.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; imageCropActual(setup) }