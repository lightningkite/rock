@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.handleThemeControl
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.readValue
import platform.AVFoundation.*
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

    fun setCaptureSession(session: AVCaptureSession) {
        // For testing
        val blueSquare = CALayer()
        blueSquare.frame = CGRectMake(50.0, 50.0, 100.0, 100.0)
        blueSquare.backgroundColor = UIColor.blueColor.CGColor
        layer.addSublayer(blueSquare)

        // The docs suggest using this as the backing layer of the view; however, I can't find a way
        // to set the layerClass class property through the Kotlin/Obj-c interop so this is a workaround
        val videoPreviewLayer = AVCaptureVideoPreviewLayer.layerWithSession(session)
        videoPreviewLayer.frame = CGRectMake(50.0, 150.0, 100.0, 100.0)
        videoPreviewLayer.backgroundColor = UIColor.darkGrayColor.CGColor
        layer.addSublayer(videoPreviewLayer)
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