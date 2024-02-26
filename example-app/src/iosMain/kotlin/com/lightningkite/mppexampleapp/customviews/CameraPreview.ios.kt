@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.cinterop.*
import platform.AVFoundation.*
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.QuartzCore.CALayer
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.darwin.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCameraPreview = PreviewView

actual class CameraPreview actual constructor(actual override val native: NCameraPreview) :
    RView<NCameraPreview> {

    private val sessionQueue = dispatch_queue_create("session queue", null)
    private val captureSession = AVCaptureSession().also(native::setCaptureSession)

    val cameraPermission = Property(false)

    init {
        checkPermissions()
    }

    private fun checkPermissions() {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                cameraPermission.value = true
            }
            AVAuthorizationStatusNotDetermined -> {
                dispatch_suspend(sessionQueue)
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) {
                    granted ->
                    cameraPermission.value = true
                    dispatch_resume(sessionQueue)
                }
            }
            else -> {
                cameraPermission.value = false
            }
        }

        dispatch_async(sessionQueue) {
            setupCaptureSession()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun setupCaptureSession() {
        if (!cameraPermission.value) return

        captureSession.beginConfiguration()

        try {
            AVCaptureDevice.defaultDeviceWithDeviceType(AVCaptureDeviceTypeBuiltInWideAngleCamera,
                    AVMediaTypeVideo, AVCaptureDevicePositionBack)?.let {
                AVCaptureDeviceInput.deviceInputWithDevice(it, null)?.let {
                    if (captureSession.canAddInput(it)) {
                        captureSession.addInput(it)
                    }
                }
            }
        } finally {
            captureSession.commitConfiguration()
        }
        captureSession.startRunning()
    }

    actual fun capture(error: () -> Unit, success: (ImageLocal) -> Unit) {
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Suppress("ACTUAL_WITHOUT_EXPECT")
class PreviewView() : UIView(CGRectZero.readValue()) {

    private var videoPreviewLayer: AVCaptureVideoPreviewLayer? = null

    override fun layoutSubviews() {
        super.layoutSubviews()

        videoPreviewLayer?.apply {
            frame = this@PreviewView.bounds
        }
    }

    fun setCaptureSession(session: AVCaptureSession) {
        // The docs suggest using this as the backing layer of the view; however, I can't find a way
        // to set the layerClass class property through the Kotlin/Obj-c interop so this is a workaround
        videoPreviewLayer = AVCaptureVideoPreviewLayer.layerWithSession(session).apply {
            frame = this@PreviewView.bounds
            backgroundColor = UIColor.darkGrayColor.CGColor // For testing
            this@PreviewView.layer.addSublayer(this)
        }
    }
}

@ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) = element(PreviewView()) {
    setup(CameraPreview(this))
}

actual fun CameraPreview.barcodeHandler(action: (List<String>, Long) -> Unit) {
}

actual fun CameraPreview.ocrHandler(action: (String, Long) -> Unit) {
}

actual val CameraPreview.hasPermissions: Writable<Boolean>
    get() = cameraPermission