package com.lightningkite.rock.views.direct

import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.lightningkite.rock.R
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.LoadRemoteImageScope
import com.lightningkite.rock.views.Path.PathDrawable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import timber.log.Timber
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView

actual typealias NImage = ImageView

actual var Image.source: ImageSource?
    get() = TODO()
    set(value) {
        native.tag = value
        when (value) {
            null -> {
                native.setImageDrawable(null)
            }
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
                native.setImageDrawable(null)

                LoadRemoteImageScope.bitmapFromUrl(value.url, onBitmapLoaded = { bitmap ->
                    Handler(Looper.getMainLooper()).post {
                        Timber.d("REMOTE IMAGE SET DRAWABLE")
                        native.setImageDrawable(BitmapDrawable(native.resources, bitmap))
                    }
                }) {
                    Handler(Looper.getMainLooper()).post {
                        native.setImageResource(R.drawable.baseline_broken_image_24)
                    }
                }
            }

            is ImageResource -> {
                Timber.d("HITHER AND THITHER IMAGE RESOURCE ${value.resource}")
                native.setImageResource(value.resource)
            }

            is ImageVector -> {
                native.setImageDrawable(PathDrawable(value))
            }

            is ImageLocal -> {
                native.setImageURI(value.file.uri)
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
actual fun ViewWriter.imageActual(setup: Image.() -> Unit) {
    return viewElement(factory = ::ImageView, wrapper = ::Image) {
        handleTheme(native, viewDraws = true, viewLoads = true)
        setup(this)
    }
}


@ViewDsl
actual fun ViewWriter.zoomableImageActual(setup: Image.() -> Unit) {
    return viewElement(::ZoomClass, wrapper = ::Image){
        handleTheme(native, viewDraws = true)
        setup(this)
    }
}


class ZoomClass : AppCompatImageView, View.OnTouchListener,
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    //shared constructing
    private var mContext: Context? = null
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null
    var mMatrix: Matrix? = null
    private var mMatrixValues: FloatArray? = null
    var mode = NONE

    // Scales
    var mSaveScale = 1f
    var mMinScale = 1f
    var mMaxScale = 4f

    // view dimensions
    var origWidth = 0f
    var origHeight = 0f
    var viewWidth = 0
    var viewHeight = 0
    private var mLast = PointF()
    private var mStart = PointF()

    constructor(context: Context) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private fun resetZoom(){
        mSaveScale = 1f
        mMinScale = 1f
        mMaxScale = 4f

        // view dimensions
        origWidth = 0f
        origHeight = 0f
        viewWidth = 0
        viewHeight = 0
        mLast = PointF()
        mStart = PointF()

        mMatrix = Matrix()
        mMatrixValues = FloatArray(9)
        imageMatrix = mMatrix
    }

    override fun setImageDrawable(drawable: Drawable?) {
        resetZoom()
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        resetZoom()
        super.setImageResource(resId)
    }

    init{
        super.setClickable(true)
        mContext = context
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mMatrix = Matrix()
        mMatrixValues = FloatArray(9)
        imageMatrix = mMatrix
        scaleType = ScaleType.MATRIX
        mGestureDetector = GestureDetector(context, this)
        setOnTouchListener(this)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val prevScale = mSaveScale
            mSaveScale *= mScaleFactor
            if (mSaveScale > mMaxScale) {
                mSaveScale = mMaxScale
                mScaleFactor = mMaxScale / prevScale
            } else if (mSaveScale < mMinScale) {
                mSaveScale = mMinScale
                mScaleFactor = mMinScale / prevScale
            }
            if (origWidth * mSaveScale <= viewWidth
                || origHeight * mSaveScale <= viewHeight) {
                mMatrix!!.postScale(mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(),
                    viewHeight / 2.toFloat())
            } else {
                mMatrix!!.postScale(mScaleFactor, mScaleFactor,
                    detector.focusX, detector.focusY)
            }
            fixTranslation()
            return true
        }
    }

    private  fun fitToScreen() {
        mSaveScale = 1f
        val scale: Float
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        scale = scaleX.coerceAtMost(scaleY)
        mMatrix!!.setScale(scale, scale)

        // Center the image
        var redundantYSpace = (viewHeight.toFloat()
                - scale * imageHeight.toFloat())
        var redundantXSpace = (viewWidth.toFloat()
                - scale * imageWidth.toFloat())
        redundantYSpace /= 2.toFloat()
        redundantXSpace /= 2.toFloat()
        mMatrix!!.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = viewWidth - 2 * redundantXSpace
        origHeight = viewHeight - 2 * redundantYSpace
        imageMatrix = mMatrix
    }

    fun fixTranslation() {
        mMatrix!!.getValues(mMatrixValues) //put matrix values into a float array so we can analyze
        val transX = mMatrixValues!![Matrix.MTRANS_X] //get the most recent translation in x direction
        val transY = mMatrixValues!![Matrix.MTRANS_Y] //get the most recent translation in y direction
        val fixTransX = getFixTranslation(transX, viewWidth.toFloat(), origWidth * mSaveScale)
        val fixTransY = getFixTranslation(transY, viewHeight.toFloat(), origHeight * mSaveScale)
        if (fixTransX != 0f || fixTransY != 0f) mMatrix!!.postTranslate(fixTransX, fixTransY)
    }

    private fun getFixTranslation(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) { // case: NOT ZOOMED
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else { //CASE: ZOOMED
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) { // negative x or y translation (down or to the right)
            return -trans + minTrans
        }
        if (trans > maxTrans) { // positive x or y translation (up or to the left)
            return -trans + maxTrans
        }
        return 0F
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) {
            0F
        } else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mSaveScale == 1f) {

            // Fit to screen.
            fitToScreen()
        }
    }

    /*
        Ontouch
     */
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)
        mGestureDetector!!.onTouchEvent(event)
        val currentPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLast.set(currentPoint)
                mStart.set(mLast)
                mode = DRAG
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                val dx = currentPoint.x - mLast.x
                val dy = currentPoint.y - mLast.y
                val fixTransX = getFixDragTrans(dx, viewWidth.toFloat(), origWidth * mSaveScale)
                val fixTransY = getFixDragTrans(dy, viewHeight.toFloat(), origHeight * mSaveScale)
                mMatrix!!.postTranslate(fixTransX, fixTransY)
                fixTranslation()
                mLast[currentPoint.x] = currentPoint.y
            }
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
        }
        imageMatrix = mMatrix
        return false
    }

    /*
        GestureListener
     */
    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {}
    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(motionEvent: MotionEvent) {}
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    /*
        onDoubleTap
     */
    override fun onSingleTapConfirmed(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(motionEvent: MotionEvent): Boolean {
        fitToScreen()
        return false
    }

    override fun onDoubleTapEvent(motionEvent: MotionEvent): Boolean {
        return false
    }

    companion object {

        // Image States
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
    }
}