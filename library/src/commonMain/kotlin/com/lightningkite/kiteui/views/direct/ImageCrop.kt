package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.ImageLocal
import com.lightningkite.kiteui.models.ImageRaw
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.contracts.*

expect class NImageCrop : NView

expect class ImageCrop(native: NImageCrop) : RView<NImageCrop> {
    override val native: NImageCrop
    var source: ImageLocal?
    var aspectRatio: Pair<Int, Int>?
    suspend fun crop(): ImageRaw?
}

@ViewDsl
expect fun ViewWriter.imageCropActual(setup: ImageCrop.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.imageCrop(noinline setup: ImageCrop.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; imageCropActual(setup) }