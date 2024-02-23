package com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews

import kotlinx.cinterop.BetaInteropApi
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.UIKit.UIView

class PreviewView() : UIView() {

    @OptIn(BetaInteropApi::class)
    // Untested: unsure if the value of this (overridden?) property will be respected through the Kotlin Obj-C interop
    val layerClass = AVCaptureVideoPreviewLayer.`class`()!!

    val previewLayer: AVCaptureVideoPreviewLayer
        get() = layer as AVCaptureVideoPreviewLayer
}