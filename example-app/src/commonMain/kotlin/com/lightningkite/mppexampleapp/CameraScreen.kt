package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.CameraPreview
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.barcodeHandler
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.cameraPreview
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.hasPermissions
import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.dp
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("camera")
object CameraScreen : RockScreen, UseFullScreen {
    override fun ViewWriter.render() {
        val barcodeContent = Property("")
        val cameraPermissions = Property(false)
        stack {
            var cp: CameraPreview? = null
            cameraPreview {
                cp = this
                // One-way binding with a Readable on one side would be great here (feature request)
                cameraPermissions bind hasPermissions
                ::opacity { if (hasPermissions.await()) 1.0 else 0.0 }
                barcodeHandler {
                    it.firstOrNull()?.let { barcodeContent.value = it }
                }
            }
            col {
                text {
                    ::content { barcodeContent.await() }
                    ::exists { barcodeContent.await().isNotEmpty() }
                }
                text {
                    content = "Please allow camera access in device settings to use the camera."
                    ::exists { !cameraPermissions.await() }
                }
                button {
                    text { content = "Capture" }
                    onClick {
                        cp!!.capture {
                            println("Image stored successfully")
                        }
                    }
                }
            } in card in sizeConstraints(height = 200.dp) in gravity(Align.Stretch, Align.End)
        } in marginless
    }
}