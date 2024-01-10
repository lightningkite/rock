package com.lightningkite.rock.views.direct

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.accessibility.AccessibilityManager
import android.widget.FrameLayout
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.canvas.DrawingContext2D
import com.lightningkite.rock.views.canvas.DrawingContext2DImpl
import kotlin.math.min

actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {
    native.renderFun = action
}
actual val Canvas.width: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@width.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@width.native.width.toDouble()
            }
        }
    }
actual val Canvas.height: Readable<Double>
    get() {
        return object : Readable<Double> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                return this@height.native.addLayoutChangeListener(listener)
            }

            override suspend fun awaitRaw(): Double {
                return this@height.native.height.toDouble()
            }
        }
    }

actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerDown.add(action)
}
actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerMove.add(action)
}
actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerCancel.add(action)
}
actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerUp.add(action)
}

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit) {
    return viewElement(factory = ::NCanvas, wrapper = ::Canvas, setup = setup)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        println("NCanvas init")
        setWillNotDraw(false)

    }

//    var accessibilityView: View? = null

    private data class Touch(
        var x: Float,
        var y: Float,
        var id: Int
    )

    var renderFun: DrawingContext2D.()->Unit = {}
        set(value) {
            field = value
            invalidate()
        }

    @SuppressLint("UseSparseArrays")
    private val touches = HashMap<Int, Touch>()

    val onPointerDown = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerMove = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerCancel = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()
    val onPointerUp = ArrayList<(id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit>()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var takenCareOf = true
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerId = event.getPointerId(event.actionIndex)
                val touch = Touch(
                    x = event.getX(event.actionIndex),
                    y = event.getY(event.actionIndex),
                    id = pointerId
                )
                touches[pointerId] = touch
                onPointerDown.forEach { it(touch.id, touch.x.toDouble(), touch.y.toDouble(), width.toDouble(), height.toDouble()) }
            }

            MotionEvent.ACTION_MOVE -> {
                for (pointerIndex in 0 until event.pointerCount) {
                    val pointerId = event.getPointerId(pointerIndex)
                    val touch = touches[pointerId]
                    if (touch != null) {
                        touch.x = event.getX(pointerIndex)
                        touch.y = event.getY(pointerIndex)
                        onPointerMove.forEach { it(touch.id, touch.x.toDouble(), touch.y.toDouble(), width.toDouble(), height.toDouble()) }
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                val pointerId = event.getPointerId(event.actionIndex)
                touches.remove(pointerId)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val pointerId = event.getPointerId(event.actionIndex)
                val touch = touches.remove(pointerId)
                if (touch != null) {
                    onPointerUp.forEach { it(touch.id, touch.x.toDouble(), touch.y.toDouble(), width.toDouble(), height.toDouble()) }
                }
            }
        }
        return takenCareOf
    }

    private val metrics = context.resources.displayMetrics
    override fun onDraw(canvas: android.graphics.Canvas) {
        println("ondraw")
        super.onDraw(canvas)
        renderFun(DrawingContext2DImpl(canvas))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> min(
                100,
                widthSize
            )
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> min(
                100,
                heightSize
            )
        }
        setMeasuredDimension(
            width,
            height
        )
    }
}