package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.barcodeHandler
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.cameraPreview
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.hasPermissions
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.ocrHandler
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.utilities.isValidVin
import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("camera")
object CameraScreen : RockScreen, UseFullScreen {
    override fun ViewWriter.render() {
        val vin = Property("")
        val cameraPermissions = Property(false)

        val showGuidance = Property(false)
        val currentStep = Property(0)
        val products = Property(mutableListOf<ImageLocal>())
        val steps = Constant(listOf(
            "Take a picture of the front of the car diagonally from the driver's side",
            "Open the driver's door and take a picture",
            "Sit down in the driver's seat, turn the car on, and take a picture of the dashboard"
        ))

        stack {
            var capture: () -> Unit = {}
            cameraPreview {
                // One-way binding with a Readable on one side would be great here (feature request)
                cameraPermissions bind hasPermissions
                ::opacity { if (hasPermissions.await()) 1.0 else 0.0 }
                barcodeHandler { barcodeResult, _ ->
                    // First, remove leading "I" for 18 digit "import" VINs
                    barcodeResult.map { if (it.length == 18) it.substring(1) else it }
                        .firstOrNull(String::isValidVin)
                        ?.let{ vin.value = it }
                }
                ocrHandler { ocrResult, _ ->
                    ocrResult.split('\n', ' ')
                        .filter { it.length == 17 }
                        .firstOrNull(String::isValidVin)
                        ?.let { vin.value = it }
                }
                stack@capture = {
                    capture {
                        products.value += it
                        val step = currentStep.value
                        currentStep.value = (step + 1) % steps.value.size
                        showGuidance.value = true
                    }
                }
            }

            viewPager {
                index bind currentStep
                ::opacity { if (showGuidance.await()) 1.0 else 0.0 }
                children(steps) {
                    col {
                        h3 { ::content { it.await() } }
                    }
                }
            } in important in centered in sizeConstraints(height = 200.dp, width = 200.dp)

            col {
                stack {
                    button {
                        text { content = "Capture" }
                        onClick { capture() }
                    } in card in gravity(Align.Center, Align.Stretch)
                    button {
                        text { content = "Show/Hide" }
                        onClick { showGuidance.value = !showGuidance.value }
                    } in card in gravity(Align.End, Align.Stretch)
                }
                text {
                    ::content { "VIN: ${vin.await()}" }
                } in card in marginless
            } in marginless in gravity(Align.Stretch, Align.End)
        } in marginless
    }
}