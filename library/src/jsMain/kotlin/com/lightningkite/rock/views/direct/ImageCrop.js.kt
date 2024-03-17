package com.lightningkite.rock.views.direct

import com.lightningkite.rock.await
import com.lightningkite.rock.models.ImageLocal
import com.lightningkite.rock.models.ImageRaw
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.clear
import kotlinx.browser.window
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.pointerevents.PointerEvent
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise
import kotlin.math.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImageCrop = HTMLCanvasElement

actual class ImageCrop actual constructor(actual override val native: NImageCrop) : RView<NImageCrop> {
    actual var source: ImageLocal? = null
        set(value) {
            field = value
            launch {
                bitmap = source?.let { window.createImageBitmap(it.file).await() }
                context.draw()
            }
        }

    private val context = (native.getContext("2d") as CanvasRenderingContext2D).apply {
        imageSmoothingQuality = ImageSmoothingQuality.HIGH
    }
    private var bitmap: ImageBitmap? = null

    init {
        // TODO: How to determine the dimens of the HTML element? When set by CSS, this is different from the properties below
        native.width = 320
        native.height = 320

        native.addEventListener("pointerdown", ::handleTouchStart)
        native.addEventListener("pointerup", ::handleTouchEnd)
        native.addEventListener("pointercancel", ::handleTouchCancel)
        native.addEventListener("pointermove", ::handleTouchMove)
    }

    private val THUMB_RADIUS = 10.0
    private var cropX: Double = 40.0
    private var cropY: Double = 40.0
    private var cropWidth: Double = 150.0
    private var cropHeight: Double = 80.0

    private fun CanvasRenderingContext2D.draw() {
        clear()

        bitmap?.let {
            val imageMinDimen = min(it.width, it.height).toDouble()
            val imageMaxDimen = max(it.width, it.height).toDouble()
            val imageAspectRatio = imageMinDimen / imageMaxDimen

            // TODO: Proper fitting of image to canvas
            val width = native.width
            val height = imageAspectRatio * width

            drawImage(it, 0.0, 0.0, width.toDouble(), height.toDouble())
        }

        drawThumbs()
    }

    private fun CanvasRenderingContext2D.drawThumbs(
        dx: Double = cropX,
        dy: Double = cropY,
        dWidth: Double = cropWidth,
        dHeight: Double = cropHeight
    ) {
        for (i in 0..3) {
            val offsetX = (i / 2) * dWidth
            val offsetY = (i % 2) * dHeight

            fillStyle = "white"
            strokeStyle = "cornflowerblue"
            lineWidth = 3.0
            beginPath()
            arc(dx + offsetX, dy + offsetY, THUMB_RADIUS, 0.0, 2 * PI)
            fill()
            stroke()
        }
    }

    private fun handleThumbMove(thumbIndex: Int, x: Double, y: Double) {
        val directX = thumbIndex / 2 == 0
        val directY = thumbIndex % 2 == 0

        val deltaX = x - cropX
        if (directX) {
            cropWidth -= deltaX
            cropX += deltaX
        } else {
            cropWidth = deltaX
        }

        val deltaY = y - cropY
        if (directY) {
            cropHeight -= deltaY
            cropY += deltaY
        } else {
            cropHeight = deltaY
        }

        context.draw()
    }

    private val touchHandlers = mutableMapOf<Int, Int>()

    private fun handleTouchStart(event: Event) {
        event as PointerEvent

        for (i in 0..3) {
            val offsetX = (i / 2) * cropWidth
            val offsetY = (i % 2) * cropHeight

            val thumbX = cropX + offsetX
            val thumbY = cropY + offsetY

            val distance = sqrt((event.offsetX - thumbX).pow(2) + (event.offsetY - thumbY).pow(2))
            if (distance <= THUMB_RADIUS) {
                touchHandlers[event.pointerId] = i
                break
            }
        }
    }

    private fun handleTouchEnd(event: Event) {
        event as PointerEvent
        touchHandlers.remove(event.pointerId)
    }

    private fun handleTouchCancel(event: Event) {
        event as PointerEvent
        touchHandlers.remove(event.pointerId)
    }

    private fun handleTouchMove(event: Event) {
        event as PointerEvent
        touchHandlers[event.pointerId]?.let {
            handleThumbMove(it, event.offsetX, event.offsetY)
        }
    }

    actual suspend fun crop(): ImageRaw? {
        val bitmap = bitmap ?: return null

        val scale = native.width / bitmap.width.toDouble()

        val resultWidth = (cropWidth / scale).toInt()
        val resultHeight = (cropHeight / scale).toInt()

        val cropCanvas = OffscreenCanvas(resultWidth, resultHeight)
        val cropContext = cropCanvas.getContext("2d") as OffscreenCanvasRenderingContext2D

        cropContext.drawImage(
            bitmap,
            sx = cropX / scale,
            sy = cropY / scale,
            sw = cropWidth / scale,
            sh = cropHeight / scale,
            dx = 0.0,
            dy = 0.0,
            dw = resultWidth.toDouble(),
            dh = resultHeight.toDouble()
        )

        val result = cropCanvas.convertToBlob().await()
        val blobUrl = URL.Companion.createObjectURL(result)
        println("Check cropped photo at $blobUrl")

        val arrayBuffer = FileReader().readAsArrayBufferSync(result)
        val jsArray = Uint8Array(arrayBuffer)
        val byteArray = ByteArray(jsArray.length)
        for (i in 0..<jsArray.length) {
            byteArray[i] = jsArray[i]
        }
        return ImageRaw(byteArray)
    }
}

external class OffscreenCanvas(width: Int, height: Int) {
    var width: Int
    var height: Int
    fun getContext(contextType: String, vararg arguments: Any): Any?
    fun convertToBlob(): Promise<Blob>
}

external class OffscreenCanvasRenderingContext2D {
    fun drawImage(image: CanvasImageSource, sx: Double, sy: Double, sw: Double, sh: Double, dx: Double, dy: Double, dw: Double, dh: Double)
}

suspend fun FileReader.readAsArrayBufferSync(blob: Blob): ArrayBuffer = suspendCoroutine { continuation ->
    onload = {
        continuation.resume(result as ArrayBuffer)
    }
    onerror = {
        continuation.resumeWithException(Exception())
    }

    readAsArrayBuffer(blob)
}

@ViewDsl
actual inline fun ViewWriter.imageCropActual(crossinline setup: ImageCrop.() -> Unit): Unit =
    element<HTMLCanvasElement>("canvas") {
        setup(ImageCrop(this))
    }
