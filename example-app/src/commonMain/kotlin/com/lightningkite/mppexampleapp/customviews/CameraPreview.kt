package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

expect class NCameraPreview : NView

class CameraPreview(override val native: NCameraPreview) : RView<NCameraPreview> {
    val barcode = Property("")
}

@ViewDsl
expect fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit = {}): Unit
