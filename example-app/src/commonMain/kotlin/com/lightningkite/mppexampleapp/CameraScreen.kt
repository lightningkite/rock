package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.barcodeHandler
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.cameraPreview
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.onPermissionRejected
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
        val errorMessage = Property("")
        stack {
            cameraPreview {
                onPermissionRejected {
                    errorMessage.value = "Please accept camera permissions to use the camera."
                }
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
                    ::content { errorMessage.await() }
                    ::exists { errorMessage.await().isNotEmpty() }
                }
            } in card in sizeConstraints(height = 200.dp) in gravity(Align.Stretch, Align.End)
        } in marginless
    }
}