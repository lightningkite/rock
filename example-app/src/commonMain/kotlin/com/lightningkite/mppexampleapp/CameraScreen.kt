package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.cameraPreview
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.hasPermissions
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
        val barcodeContent = Property("")
        val cameraPermissions = Property(false)

        val showGuidance = Property(true)
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
                /*barcodeHandler {
                    it.firstOrNull()?.let { barcodeContent.value = it }
                }*/
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
                        image {
                            val currentTheme = currentTheme
                            ::source { Icon.done.toImageSource(currentTheme().foreground) }
                            // TODO: Make this appear when a picture has been taken for a specific step
                            exists = false
                        }
                    }
                }
            } in important in centered in sizeConstraints(height = 200.dp, width = 200.dp)

            button {
                text { content = "Capture" }
                onClick { capture() }
            } in card in gravity(Align.Center, Align.End)
            button {
                text { content = "Show/Hide" }
                onClick { showGuidance.value = !showGuidance.value }
            } in card in gravity(Align.End, Align.End)
        } in marginless
    }
}