package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.view.updateLayoutParams
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCameraPreview = PreviewView

@OptIn(ExperimentalGetImage::class) @ViewDsl
actual fun ViewWriter.cameraPreview(setup: CameraPreview.() -> Unit) {
    viewElement(factory = ::PreviewView, wrapper = ::CameraPreview) {
        native.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val cameraController = LifecycleCameraController(native.context)
        AndroidAppContext.activityCtx!!.let(cameraController::bindToLifecycle)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraController.isPinchToZoomEnabled = false

        native.controller = cameraController

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
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { this.barcode.value = it }
                        }
                    }
            }
        }

        cameraController.setImageAnalysisAnalyzer(AndroidAppContext.executor, analyzer)
        setup(this)
    }
}