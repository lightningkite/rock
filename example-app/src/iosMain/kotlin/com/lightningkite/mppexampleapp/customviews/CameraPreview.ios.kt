package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.handleThemeControl
import platform.AVFoundation.AVCaptureSession
import platform.UIKit.UIView

actual typealias NCameraPreview = PreviewView

actual class CameraPreview actual constructor(actual override val native: NCameraPreview) :
    RView<NCameraPreview> {

    private val captureSession = AVCaptureSession().also {
        native.previewLayer.session = it
    }

    actual fun capture(error: () -> Unit, success: (ImageLocal) -> Unit) {
    }
}

@ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) = element(PreviewView()) {
    setup(CameraPreview(this))
}

actual fun CameraPreview.barcodeHandler(
    action: (List<String>, Long) -> Unit
) {
}

actual fun CameraPreview.ocrHandler(action: (String, Long) -> Unit) {
}

actual val CameraPreview.hasPermissions: Writable<Boolean>
    get() = TODO("Not yet implemented")