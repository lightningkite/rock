package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import android.Manifest
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCameraPreview = PreviewView

actual class CameraPreview actual constructor(actual override val native: NCameraPreview) :
    RView<NCameraPreview> {

    private val cameraController = LifecycleCameraController(native.context)
    init {
        AndroidAppContext.activityCtx!!.let(cameraController::bindToLifecycle)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraController.isPinchToZoomEnabled = false
        native.controller = cameraController
    }

    private var permissionRejectionHandler: () -> Unit = {}
    fun setPermissionRejectionHandler(action: () -> Unit) {
        permissionRejectionHandler = action
    }

    private var barcodeResultHandler: (List<String>) -> Unit = {}
    fun setBarcodeResultHandler(action: (List<String>) -> Unit) {
        barcodeResultHandler = action
        setupImageAnalyzer()
    }

    fun ensurePermissions() {
        if (ContextCompat.checkSelfPermission(native.context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED) {
            AndroidAppContext.requestPermissions(Manifest.permission.CAMERA, onResult = {
                    result: RockActivity.PermissionResult ->
                if (!result.accepted) permissionRejectionHandler()
            })
        }
    }

    @OptIn(ExperimentalGetImage::class)
    fun setupImageAnalyzer() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93)
            .build()
        val scanner = BarcodeScanning.getClient(options)

        val analyzer = ImageAnalysis.Analyzer { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        imageProxy.close()
                        barcodeResultHandler(barcodes.mapNotNull(Barcode::getRawValue))
                    }
            }
        }

        cameraController.setImageAnalysisAnalyzer(AndroidAppContext.executor, analyzer)
    }
}

@OptIn(ExperimentalGetImage::class) @ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) {
    viewElement(factory = ::PreviewView, wrapper = ::CameraPreview) {
        native.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        setup(this)

        // We check and request permissions but don't do much here if permissions
        // are denied as CameraX fails safely without permissions
        ensurePermissions()
    }
}

actual fun CameraPreview.onPermissionRejected(action: () -> Unit) =
    setPermissionRejectionHandler(action)
actual fun CameraPreview.barcodeHandler(action: (List<String>) -> Unit) =
    setBarcodeResultHandler(action)