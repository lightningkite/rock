package com.lightningkite.kiteui.views.direct

import android.view.View
import com.lightningkite.kiteui.Blob
import com.lightningkite.kiteui.models.ImageLocal
import com.lightningkite.kiteui.models.ImageRaw
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageCrop = View
actual class ImageCrop actual constructor(native: NImageCrop) : RView<NImageCrop> {
    actual override val native: NImageCrop
        get() = TODO("Not yet implemented")
    actual var source: ImageLocal?
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var aspectRatio: Pair<Int, Int>?
        get() = TODO("Not yet implemented")
        set(value) {}

    actual suspend fun crop(): ImageRaw? {
        TODO("Not yet implemented")
    }
}

@ViewDsl
actual fun ViewWriter.imageCropActual(setup: ImageCrop.() -> Unit) {
}