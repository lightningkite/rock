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
import platform.CoreMedia.CMGetAttachment
import platform.CoreMedia.CMSampleBufferGetImageBuffer
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreMedia.kCMSampleBufferAttachmentKey_CameraIntrinsicMatrix
import platform.Foundation.NSError
import platform.ImageIO.kCGImagePropertyOrientationDownMirrored
import platform.ImageIO.kCGImagePropertyOrientationLeftMirrored
import platform.ImageIO.kCGImagePropertyOrientationRightMirrored
import platform.ImageIO.kCGImagePropertyOrientationUpMirrored
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIView
import platform.Vision.*
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
        captureSession.configure {
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
        }
    }

    private val rawVideoOutputQueue by lazy { dispatch_queue_create("raw video objects queue", null) }
    private val rawVideoOutput by lazy {
        AVCaptureVideoDataOutput().apply {
            alwaysDiscardsLateVideoFrames = true
        }
    }
    private var rawVideoDelegate: AVCaptureVideoDataOutputSampleBufferDelegateProtocol? = null

    fun enableOCR(resultHandler: (String, Long) -> Unit) = dispatch_async(sessionQueue) {
        captureSession.configure {
            if (captureSession.canAddOutput(rawVideoOutput)) {
                captureSession.addOutput(rawVideoOutput)
                rawVideoOutput.apply {
                    rawVideoDelegate = VNImageRequestCaptureBufferDelegate(resultHandler).also {
                        setSampleBufferDelegate(it, rawVideoOutputQueue)
                    }
                }
            }
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
            val barcodes = didOutputMetadataObjects
                .filterIsInstance<AVMetadataMachineReadableCodeObject>()
                .mapNotNull { it.stringValue }
            dispatch_async(dispatch_get_main_queue()) {
                barcodeHandler(barcodes, 0)
                barcodeResultHandlerMutex.unlock()
            }
        }
    }
}

class VNImageRequestCaptureBufferDelegate(private val ocrHandler: (String, Long) -> Unit) : NSObject(),
    AVCaptureVideoDataOutputSampleBufferDelegateProtocol {
    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(output: AVCaptureOutput, didOutputSampleBuffer: CMSampleBufferRef?,
                               fromConnection: AVCaptureConnection) {
        // Prepare CaptureSession data for VNImageRequestHandler
        val cameraIntrinsicData = CMGetAttachment(didOutputSampleBuffer,
            kCMSampleBufferAttachmentKey_CameraIntrinsicMatrix, null)
        val requestHandlerOptions: Map<Any?, *> = mapOf(VNImageOptionCameraIntrinsics to cameraIntrinsicData)
        val orientation = when (UIDevice.currentDevice.orientation) {
            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> kCGImagePropertyOrientationRightMirrored
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft -> kCGImagePropertyOrientationDownMirrored
            UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> kCGImagePropertyOrientationUpMirrored
            else -> kCGImagePropertyOrientationLeftMirrored
        }
        val pixelBuffer = CMSampleBufferGetImageBuffer(didOutputSampleBuffer)

        val imageRequestHandler = VNImageRequestHandler(pixelBuffer, orientation, requestHandlerOptions)
        val ocrRequest = VNRecognizeTextRequest { vnRequest: VNRequest?, nsError: NSError? ->
            val ocrResults: List<VNRecognizedTextObservation> =
                vnRequest?.results?.filterIsInstance<VNRecognizedTextObservation>() ?: emptyList()
            val ocrString = ocrResults
                .flatMap { it.topCandidates(1u).filterIsInstance<VNRecognizedText>() }
                .map { it.string }
                .joinToString("\n")
            dispatch_async(dispatch_get_main_queue()) {
                ocrHandler(ocrString, 0)
            }
        }

        memScoped {
            var error = alloc<ObjCObjectVar<NSError?>>()
            imageRequestHandler.performRequests(listOf(ocrRequest), error.ptr)
        }
    }
}

fun AVCaptureSession.configure(configure: () -> Unit) {
    beginConfiguration()
    try {
        configure()
    } finally {
        commitConfiguration()
    }
}

@ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) = element(PreviewView()) {
    setup(CameraPreview(this))
}

actual fun CameraPreview.onBarcode(action: (List<String>, Long) -> Unit) = enableBarcodeScanning(action)

actual fun CameraPreview.onOCR(action: (String, Long) -> Unit) = enableOCR(action)

actual val CameraPreview.hasPermissions: Writable<Boolean>
    get() = cameraPermission