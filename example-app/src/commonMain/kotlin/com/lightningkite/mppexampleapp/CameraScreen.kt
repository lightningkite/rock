package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.customviews.cameraPreview
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
        stack {
            cameraPreview {
                barcode bind barcodeContent
            }
            col {
                text {
                    ::content { barcodeContent.await() }
                }
            } in card in sizeConstraints(height = 200.dp) in gravity(Align.Stretch, Align.End)
        } in marginless
    }
}