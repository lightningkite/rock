package com.lightningkite.mppexampleapp

import com.lightningkite.rock.ExternalServices
import com.lightningkite.rock.FileReference
import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.models.ImageRaw
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.awaitNotNull
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("image-crop")
object ImageCropScreen : RockScreen {
    override fun ViewWriter.render() {
        val image = Property<FileReference?>(null)
        val croppedImage = Property<ImageRaw?>(null)
        val imageCrop: ImageCrop

        col {
            h1("Image Crop")
            button {
                text("Upload Image")
                onClick {
                    ExternalServices.requestFile(listOf("image/*")) { uploaded ->
                        image.value = uploaded
                    }
                }
            }
            centered - sizeConstraints(width = 20.rem, height = 20.rem) - imageCrop {
                imageCrop = this
                reactiveScope {
                    val imageSource = ImageLocal(image.awaitNotNull())
                    source = imageSource
                }
            }
            button {
                text("Crop")
                onClick {
                    croppedImage.value = imageCrop.crop()
                }
            }
            centered - sizeConstraints(width = 20.rem, height = 20.rem) - image {
                reactiveScope {
                    source = croppedImage.await()
                }
            }
        }
    }
}