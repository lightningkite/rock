package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.cinterop.*
import kotlinx.coroutines.sync.Mutex
import platform.AVFoundation.*
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.darwin.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCameraPreview = PreviewView

actual class CameraPreview actual constructor(actual override val native: NCameraPreview) :
    RView<NCameraPreview> {

    private val sessionQueue = dispatch_queue_create("session queue", null)
    private val captureSession = AVCaptureSession().also(native::setCaptureSession)
    private val captureDevice = AVCaptureDevice.defaultDeviceWithDeviceType(AVCaptureDeviceTypeBuiltInWideAngleCamera,
        AVMediaTypeVideo, AVCaptureDevicePositionBack)

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
            captureDevice?.let {
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

    private val metadataOutputQueue by lazy { dispatch_queue_create("metadata objects queue", null) }
    private val metadataOutput by lazy { AVCaptureMetadataOutput() }
    private var metadataDelegate: AVCaptureMetadataOutputObjectsDelegateProtocol? = null

    fun enableBarcodeScanning(resultHandler: (List<String>, Long) -> Unit) = dispatch_async(sessionQueue) {
        captureSession.beginConfiguration()

        try {
            if (captureSession.canAddOutput(metadataOutput)) {
                captureSession.addOutput(metadataOutput)
                metadataOutput.apply {
                    metadataDelegate = MetadataBarcodeDelegate(resultHandler).also {
                        setMetadataObjectsDelegate(it, metadataOutputQueue)
                    }

                    // Only set object types from the pool of available object types to avoid fatal exception when
                    // camera access is not established
                    val requestedObjectTypes = setOf(AVMetadataObjectTypeCode39Code,
                        AVMetadataObjectTypeCode93Code,
                        AVMetadataObjectTypeCode128Code)
                    metadataObjectTypes = availableMetadataObjectTypes.intersect(requestedObjectTypes).toList()
                }
            }
        } finally {
            captureSession.commitConfiguration()
        }
    }

    actual fun capture(error: () -> Unit, success: (ImageLocal) -> Unit) {
    }
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("ACTUAL_WITHOUT_EXPECT")
class PreviewView() : UIView(CGRectZero.readValue()) {

    private var videoPreviewLayer: AVCaptureVideoPreviewLayer? = null

    override fun layoutSubviews() {
        super.layoutSubviews()

        videoPreviewLayer?.apply {
            frame = this@PreviewView.bounds
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun setCaptureSession(session: AVCaptureSession) {
        // The docs suggest using this as the backing layer of the view; however, I can't find a way
        // to set the layerClass class property through the Kotlin/Obj-c interop so this is a workaround
        videoPreviewLayer = AVCaptureVideoPreviewLayer.layerWithSession(session).apply {
            frame = this@PreviewView.bounds
            videoGravity = AVLayerVideoGravityResizeAspectFill
            this@PreviewView.layer.addSublayer(this)
        }
    }
}

// IntelliJ was having weird issues with an anonymous object implementing an Obj-C protocol so this class is a workaround
class MetadataBarcodeDelegate(private val barcodeHandler: (List<String>, Long) -> Unit) : NSObject(),
    AVCaptureMetadataOutputObjectsDelegateProtocol {

    private val barcodeResultHandlerMutex = Mutex()

    override fun captureOutput(output: AVCaptureOutput, didOutputMetadataObjects: List<*>,
                               fromConnection: AVCaptureConnection) {
        // Ignore and discard new results while results are being processed
        if (barcodeResultHandlerMutex.tryLock()) {
            dispatch_async(dispatch_get_main_queue()) {
                val barcodes = didOutputMetadataObjects
                    .filterIsInstance<AVMetadataMachineReadableCodeObject>()
                    .mapNotNull { it.stringValue }
                barcodeHandler(barcodes, 0)
                barcodeResultHandlerMutex.unlock()
            }
        }
    }
}

@ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) = element(PreviewView()) {
    setup(CameraPreview(this))
}

actual fun CameraPreview.barcodeHandler(action: (List<String>, Long) -> Unit) =
    enableBarcodeScanning(action)

actual fun CameraPreview.ocrHandler(action: (String, Long) -> Unit) {
}

actual val CameraPreview.hasPermissions: Writable<Boolean>
    get() = cameraPermission