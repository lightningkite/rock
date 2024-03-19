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
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.pointerevents.PointerEvent
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.and
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

    actual var aspectRatio: Pair<Int, Int>? = null
        set(value) {
            field = value

            value?.let {
                // For any aspect ratio, there are two options that preserve one of the current dimensions
                val widthOfFirstOption = cropHeight * (value.first / value.second)
                val heightOfSecondOption = cropWidth * (value.second / value.first)

                // Pick the one that results in making a dimension smaller
                if (widthOfFirstOption < cropWidth) {
                    cropWidth = widthOfFirstOption
                } else {
                    cropHeight = heightOfSecondOption
                }
            }
        }

    private val context = (native.getContext("2d") as CanvasRenderingContext2D).apply {
        imageSmoothingQuality = ImageSmoothingQuality.HIGH
    }
    private var bitmap: ImageBitmap? = null

    init {
        native.width = native.offsetWidth
        native.height = native.offsetHeight

        native.addEventListener("mousemove", ::handleMouseMove)
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
    private var fitHorizontal = true    // If false, fit image vertically
    private var imageWidth: Double = 0.0
    private var imageHeight: Double = 0.0

    private fun CanvasRenderingContext2D.draw() {
        clear()

        bitmap?.let {
            val imageAspectRatio: Double = it.width.toDouble() / it.height
            val canvasAspectRatio: Double = native.width.toDouble() / native.height
            fitHorizontal = imageAspectRatio > canvasAspectRatio

            if (fitHorizontal) {
                imageWidth = native.width.toDouble()
                imageHeight = imageWidth / imageAspectRatio
            } else {
                imageHeight = native.height.toDouble()
                imageWidth = imageHeight * imageAspectRatio
            }

            drawImage(it, 0.0, 0.0, imageWidth, imageHeight)
        }

        drawThumbs()
    }

    private fun CanvasRenderingContext2D.drawThumbs(
        dx: Double = cropX,
        dy: Double = cropY,
        dWidth: Double = cropWidth,
        dHeight: Double = cropHeight
    ) {
        strokeStyle = "white"
        lineWidth = 2.0
        beginPath()
        rect(cropX, cropY, cropWidth, cropHeight)
        stroke()

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

    private fun handleMouseMove(event: Event) {
        event as MouseEvent

        event.preventDefault()
        event.stopPropagation()

        val pointerDown = (event.buttons and 1).toInt() == 1

        if (withinThumbTouchTarget(event.offsetX, event.offsetY) >= 0 ||
            withinCropRegion(event.offsetX, event.offsetY)) {
            native.style.cursor = if (pointerDown) "grabbing" else "grab"
        } else {
            native.style.cursor = "default"
        }
    }

    private fun handleThumbMove(thumbIndex: Int, x: Double, y: Double) {
        val directX = thumbIndex / 2 == 0
        val directY = thumbIndex % 2 == 0

        val cornerX = cropX + if (!directX) cropWidth else 0.0
        val cornerY = cropY + if (!directY) cropHeight else 0.0

        var deltaWidth = (x - cornerX) * if (directX) -1 else 1
        var deltaHeight = (y - cornerY) * if (directY) -1 else 1

        aspectRatio?.let { aspectRatio ->
            if (deltaWidth > deltaHeight) {
                deltaHeight = deltaWidth * (aspectRatio.second / aspectRatio.first)
            } else {
                deltaWidth = deltaHeight * (aspectRatio.first / aspectRatio.second)
            }
        }

        val newCropWidth = cropWidth + deltaWidth
        val newCropHeight = cropHeight + deltaHeight
        val newCropX = if (directX) cropX - deltaWidth else cropX
        val newCropY = if (directY) cropY - deltaHeight else cropY

        // Check that the current corner and diagonal corner are in bounds before accepting a new crop region
        val cornerInBounds = withinImage(
            if (directX) newCropX else newCropX + newCropWidth,
            if (directY) newCropY else newCropY + newCropHeight,
        )

        val diagonalInBounds = withinImage(
            if (!directX) newCropX else newCropX + newCropWidth,
            if (!directY) newCropY else newCropY + newCropHeight,
        )

        if (cornerInBounds && diagonalInBounds) {
            cropWidth = newCropWidth
            cropHeight = newCropHeight
            cropX = newCropX
            cropY = newCropY
        }

        context.draw()
    }

    private fun withinThumbTouchTarget(x: Double, y: Double): Int {
        for (i in 0..3) {
            val offsetX = (i / 2) * cropWidth
            val offsetY = (i % 2) * cropHeight

            val thumbX = cropX + offsetX
            val thumbY = cropY + offsetY

            val distance = sqrt((x - thumbX).pow(2) + (y - thumbY).pow(2))
            if (distance <= THUMB_RADIUS) {
                return i
            }
        }
        return -1
    }

    private fun withinImage(x: Double, y: Double): Boolean =
        x in 0.0..imageWidth && y in 0.0..imageHeight

    private fun rangeBetween(a: Double, b: Double) = min(a, b) .. max(a, b)
    private fun withinCropRegion(x: Double, y: Double): Boolean =
        x in rangeBetween(cropX, cropX + cropWidth) && y in rangeBetween(cropY, cropY + cropHeight)

    private val touchHandlers = mutableMapOf<Int, Int>()

    private fun handleTouchStart(event: Event) {
        event as PointerEvent

        val thumbIndex = withinThumbTouchTarget(event.offsetX, event.offsetY)
        if (thumbIndex >= 0) {
            touchHandlers[event.pointerId] = thumbIndex
        }
    }

    private fun handleTouchEnd(event: Event) {
        event as PointerEvent
        touchHandlers.remove(event.pointerId)
        native.style.cursor = "grab"
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
        val scale: Double = if (fitHorizontal) {
            native.width.toDouble() / bitmap.width
        } else {
            native.height.toDouble() / bitmap.height
        }

        val resultWidth = (cropWidth.absoluteValue / scale).toInt()
        val resultHeight = (cropHeight.absoluteValue / scale).toInt()

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
