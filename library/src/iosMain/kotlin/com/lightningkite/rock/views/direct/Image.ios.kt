package com.lightningkite.rock.views.direct

import com.lightningkite.rock.backToMainThread
import com.lightningkite.rock.fetch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.toNSData
import com.lightningkite.rock.views.*
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import platform.UIKit.accessibilityLabel
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImage = UIImageView

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit): Unit = element(NImage()) {
    handleTheme(this, viewDraws = true)
    this.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
    setup(Image(this))
}

actual inline var Image.source: ImageSource
    get() = TODO()
    set(value) {
        when (value) {
            is ImageRaw -> {
                native.image = UIImage(data = value.data.toNSData())
                native.informParentOfSizeChange()
            }

            is ImageRemote -> {
                native.image = null
                launch {
                    native.image = UIImage(data = fetch(value.url).blob().data)
                    native.informParentOfSizeChange()
                }
            }

            is ImageResource -> {
                native.image = UIImage.imageNamed(value.name)
                native.informParentOfSizeChange()
            }

            is ImageVector -> {
                native.image = value.render()
                native.informParentOfSizeChange()
            }

            is ImageLocal -> {
                native.image = null
                value.file.provider.loadDataRepresentationForContentType(
                    value.file.suggestedType ?: UTTypeImage
                ) { data, err ->
                    if (data != null) {
                        //TODO There has to be a better way to handle this. This lambda is run on a background thread.
                        launch {
                            backToMainThread()
                            native.image = UIImage(data = data)
                            native.informParentOfSizeChange()
                        }
                    }
                }
            }

            else -> {}
        }
    }
actual inline var Image.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        when (value) {
            ImageScaleType.Fit -> native.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            ImageScaleType.Crop -> {
                native.clipsToBounds = true
                native.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
            }

            ImageScaleType.Stretch -> native.contentMode = UIViewContentMode.UIViewContentModeScaleToFill
            ImageScaleType.NoScale -> native.contentMode = UIViewContentMode.UIViewContentModeCenter
        }
    }
actual inline var Image.description: String?
    get() = TODO()
    set(value) {
        native.accessibilityLabel = value
    }