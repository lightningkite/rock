package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.lightningkite.rock.FileReference
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import java.io.File

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

    // TODO: Implement one-way binding, expose this in an externally immutable way
    val cameraPermission = Property(false)

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
                cameraPermission.value = result.accepted
            })
        } else {
            cameraPermission.value = true
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

    actual fun capture(error: () -> Unit, success: (ImageLocal) -> Unit) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "capture")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

        val captureOptions =
            ImageCapture.OutputFileOptions.Builder(
                File(AndroidAppContext.applicationCtx.filesDir, "image.jpg")
            ).build()

        val internalCallback = object: ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let {
                    val image = ImageLocal(FileReference(it))
                    success(image)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                error()
            }
        }

        cameraController.takePicture(captureOptions, AndroidAppContext.executor, internalCallback)
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

actual fun CameraPreview.barcodeHandler(action: (List<String>) -> Unit) =
    setBarcodeResultHandler(action)

actual val CameraPreview.hasPermissions: Writable<Boolean>
    get() = cameraPermission