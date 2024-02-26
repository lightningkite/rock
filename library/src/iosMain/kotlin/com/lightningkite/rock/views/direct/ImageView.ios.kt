package com.lightningkite.rock.views.direct

import com.lightningkite.rock.fetch
import com.lightningkite.rock.models.*
import com.lightningkite.rock.toNSData
import com.lightningkite.rock.views.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.objc.sel_registerName

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageView = UIImageView

@ViewDsl
actual fun ViewWriter.imageActual(setup: ImageView.() -> Unit): Unit = element(NImageView()) {
    handleTheme(this, viewDraws = true, viewLoads = true)
    this.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
    setup(ImageView(this))
}

actual inline var ImageView.source: ImageSource?
    get() = TODO()
    set(value) {
        when (value) {
            null -> {
                native.image = null
                native.informParentOfSizeChange()
            }
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
                        dispatch_async(queue = dispatch_get_main_queue(), block = {
                            native.image = UIImage(data = data)
                            native.informParentOfSizeChange()
                        })
                    }
                }
            }

            else -> {}
        }
    }
actual inline var ImageView.scaleType: ImageScaleType
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
actual inline var ImageView.description: String?
    get() = TODO()
    set(value) {
        native.accessibilityLabel = value
    }

@ViewDsl
actual fun ViewWriter.zoomableImageActual(setup: ImageView.() -> Unit)  = element(PanZoomImageView()) {
    handleTheme(this, viewDraws = true)
    setup(ImageView(imageView))
    imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
}

@OptIn(ExperimentalForeignApi::class)
private class MyImageView: UIImageView(CGRectZero.readValue()) {

    var onImageChange: ((UIImage?)->Unit)? = null

    override fun setImage(image: UIImage?) {
        super.setImage(image)
        onImageChange?.invoke(image)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class PanZoomImageView: UIScrollView(CGRectZero.readValue()), UIScrollViewDelegateProtocol {

    val imageView = MyImageView()

    init {

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
        imageView.onImageChange = {
            setZoomScale(minimumZoomScale, false)
        }
        calculationContext.onRemove { imageView.onImageChange = null }
        addSubview(imageView)

        NSLayoutConstraint.activateConstraints(listOf(
            imageView.widthAnchor.constraintEqualToAnchor(widthAnchor),
            imageView.heightAnchor.constraintEqualToAnchor(heightAnchor),
            imageView.centerXAnchor.constraintEqualToAnchor(centerXAnchor),
            imageView.centerYAnchor.constraintEqualToAnchor(centerYAnchor),
        ))


        val doubleTapRecognizer = UITapGestureRecognizer(this, sel_registerName("handleDoubleTap:"))
        doubleTapRecognizer.numberOfTapsRequired = 2UL
        addGestureRecognizer(doubleTapRecognizer)

        minimumZoomScale = 1.0
        maximumZoomScale = 4.0
        showsHorizontalScrollIndicator = false
        showsVerticalScrollIndicator = false

        delegate = this

    }


    override fun viewForZoomingInScrollView(scrollView: UIScrollView): UIView? {
        return imageView
    }

    @ObjCAction
    fun handleDoubleTap(sender: UITapGestureRecognizer){
        val midZoom = (maximumZoomScale - minimumZoomScale) / 2.0 + minimumZoomScale
        if (zoomScale < midZoom) {
            setZoomScale(maximumZoomScale, true)
        } else {
            setZoomScale(minimumZoomScale, true)
        }
    }

}
