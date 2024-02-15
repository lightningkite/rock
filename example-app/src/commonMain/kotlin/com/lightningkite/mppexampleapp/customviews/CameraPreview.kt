package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

expect class NCameraPreview : NView

/*
* Implementation of CameraPreview on any platform is likely to be non-trivial
* and more than a one-to-one mapping of functionality from the Rock view to
* an underlying native view.
*
* For example, the Rock view provides functionality such as barcode scanning
* and OCR that is likely to require additional native code outside the native
* view class.
*
* As such, each platform will define their own Rock view to implement these
* features in a platform-specific manner.
*/
expect class CameraPreview(native: NCameraPreview) : RView<NCameraPreview> {
    override val native: NCameraPreview
}

@ViewDsl
expect fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit = {}): Unit
expect fun CameraPreview.barcodeHandler(action: (List<String>) -> Unit)
expect val CameraPreview.hasPermissions: Writable<Boolean>