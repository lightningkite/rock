package com.lightningkite.rock.views.direct

import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.lightningkite.rock.R
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.LoadRemoteImageScope
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import timber.log.Timber

actual typealias NImage = ImageView

actual var Image.source: ImageSource
    get() {
        return native.tag as ImageResource
    }
    set(value) {
        native.tag = value
        when (value) {
            is ImageRaw -> {
                val imageData = value.data
                try {
                    native.setImageDrawable(
                        BitmapDrawable(
                            native.resources,
                            BitmapFactory.decodeByteArray(
                                /* data = */ imageData,
                                /* offset = */ 0,
                                /* length = */ imageData.size
                            )
                        )
                    )
                } catch (ex: Exception) {
                    native.setImageResource(R.drawable.baseline_broken_image_24)
                }
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                BitmapDrawable(native.resources, bitmap)
            }

            is ImageRemote -> {
                var nativeView: ImageView? = native
                native.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {}

                    override fun onViewDetachedFromWindow(v: View) {
                        nativeView = null
                    }
                })

                LoadRemoteImageScope.bitmapFromUrl(value.url, onBitmapLoaded = { bitmap ->
                    Handler(Looper.getMainLooper()).post {
                        Timber.d("REMOTE IMAGE SET DRAWABLE")
                        if (nativeView != null) {
                            nativeView!!.setImageDrawable(BitmapDrawable(nativeView!!.resources, bitmap))
                        } else {
                            bitmap.recycle()
                        }
                    }
                }) {
                    Handler(Looper.getMainLooper()).post {
                        native.setImageResource(R.drawable.baseline_broken_image_24)
                    }
                }
            }

            is ImageResource -> {
                Timber.d("HITHER AND THITHER IMAGE RESOURCE")
                native.setImageDrawable(value.drawable)
            }

            is ImageVector -> {
//                val drawable = VectorDrawableCreator.getVectorDrawable(
//                    context = native.context,
//                    width = value.width.value.toInt(),
//                    height = value.height.value.toInt(),
//                    viewportWidth = value.viewBoxWidth.toFloat(),
//                    viewportHeight = value.viewBoxHeight.toFloat(),
//                    paths = value.paths.map {
//                        val path = VectorDrawableCreator.PathData(
//                            it.path,
//                            it.fillColor?.closestColor()?.toInt() ?: 0
//                        )
//                        path
//                    }
//                )
//                native.setImageDrawable(drawable)
                native.setImageDrawable(
                    ContextCompat.getDrawable(
                        native.context,
                        android.R.drawable.ic_dialog_alert
                    )
                )
            }

            else -> {
                throw RuntimeException("Android View Tag is not an Image Source")
            }
        }
    }
actual var Image.scaleType: ImageScaleType
    get() {
        return when (this.native.scaleType) {
            ImageView.ScaleType.MATRIX -> ImageScaleType.NoScale
            ImageView.ScaleType.FIT_XY -> ImageScaleType.Stretch
            ImageView.ScaleType.FIT_START -> ImageScaleType.Fit
            ImageView.ScaleType.FIT_CENTER -> ImageScaleType.Fit
            ImageView.ScaleType.FIT_END -> ImageScaleType.Fit
            ImageView.ScaleType.CENTER -> ImageScaleType.Fit
            ImageView.ScaleType.CENTER_CROP -> ImageScaleType.Crop
            ImageView.ScaleType.CENTER_INSIDE -> ImageScaleType.NoScale
            else -> ImageScaleType.Fit
        }
    }
    set(value) {
        val scaleType: ImageView.ScaleType = when (value) {
            ImageScaleType.Fit -> ImageView.ScaleType.FIT_CENTER
            ImageScaleType.Crop -> ImageView.ScaleType.CENTER_CROP
            ImageScaleType.Stretch -> ImageView.ScaleType.FIT_XY
            ImageScaleType.NoScale -> ImageView.ScaleType.CENTER_INSIDE
        }
        this.native.scaleType = scaleType
    }
actual var Image.description: String?
    get() {
        return native.contentDescription.toString()
    }
    set(value) {
        native.contentDescription = value
    }

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit) {
    return viewElement(factory = ::ImageView, wrapper = ::Image) {
        handleTheme(native) { theme, view ->
            afterNextElementSetup {
                view.drawable?.colorFilter =
                    PorterDuffColorFilter(theme.foreground.colorInt(), PorterDuff.Mode.MULTIPLY)
            }
        }
        setup(this)
    }
}