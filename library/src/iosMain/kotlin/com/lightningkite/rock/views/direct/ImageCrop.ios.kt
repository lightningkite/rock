package com.lightningkite.rock.views.direct

import com.lightningkite.rock.Blob
import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.models.ImageRaw
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageCrop = UIView
actual class ImageCrop actual constructor(native: NImageCrop) : RView<NImageCrop> {
    actual override val native: NImageCrop
        get() = TODO("Not yet implemented")
    actual var source: ImageLocal?
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var aspectRatio: Pair<Int, Int>?
        get() = TODO("Not yet implemented")
        set(value) {}

    actual suspend fun crop(): Blob? {
        TODO("Not yet implemented")
    }
}

@ViewDsl
actual fun ViewWriter.imageCropActual(setup: ImageCrop.() -> Unit) {
}