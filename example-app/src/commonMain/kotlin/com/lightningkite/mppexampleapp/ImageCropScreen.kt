package com.lightningkite.mppexampleapp

import com.lightningkite.rock.ExternalServices
import com.lightningkite.rock.FileReference
import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.awaitNotNull
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("image-crop")
object ImageCropScreen : RockScreen {
    override fun ViewWriter.render() {
        val image = Property<FileReference?>(null)

        expanding - col {
            h1("Image Crop")
            button {
                text("Upload Image")
                onClick {
                    ExternalServices.requestFile(listOf("image/*")) { uploaded ->
                        image.value = uploaded
                    }
                }
            }
            centered  - sizeConstraints(width = 20.rem, height = 20.rem) - imageCrop {
                reactiveScope {
                    val imageSource = ImageLocal(image.awaitNotNull())
                    source = imageSource
                }
            }
        }
    }
}