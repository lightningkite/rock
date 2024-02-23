package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.*
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.AnimationFrame
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.withWrite
import com.lightningkite.rock.views.*
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.AVKit.AVPlayerViewControllerDelegateProtocol
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGRectZero
import platform.CoreGraphics.CGSize
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.*
import platform.UniformTypeIdentifiers.UTTypeVideo
import platform.UniformTypeIdentifiers.loadFileRepresentationForContentType
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

@OptIn(ExperimentalForeignApi::class)
actual class NVideo: UIView(CGRectZero.readValue()), UIViewWithSizeOverridesProtocol, AVPlayerViewControllerDelegateProtocol {
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    private val sizeCache: MutableMap<Size, List<Size>> = HashMap()
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(1.0, size, sizeCache)
    override fun layoutSubviews() = frameLayoutLayoutSubviews(1.0, sizeCache)
    override fun subviewDidChangeSizing(view: UIView?) = frameLayoutSubviewDidChangeSizing(1.0, view, sizeCache)
    override fun didAddSubview(subview: UIView) {
        super.didAddSubview(subview)
        sizeCache.clear()
    }
    override fun willRemoveSubview(subview: UIView) {
        // Fixes a really cursed crash where "this" is null
        this?.sizeCache?.clear()
        super.willRemoveSubview(subview)
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }

    val controller = AVPlayerViewController()
    init {
        controller.delegate = this
        addSubview(controller.view)
    }

    val time = Property(0.0)
    val playing = Property(false)
    private var animationFrameRateClose: (()->Unit)? = null
    private var playerRateObservationClose: (()->Unit)? = null

    @OptIn(ExperimentalNativeApi::class)
    var player: AVPlayer?
        get() = controller.player
        set(value) {
            playerRateObservationClose?.invoke()
            playerRateObservationClose = null
            controller.player = value
            value?.let { player ->
                val weakPlayer = WeakReference(player)
                playerRateObservationClose = player.observe("rate") {
                    val player = weakPlayer.get() ?: return@observe
                    val value = player.rate > 0f
                    if(playing.value != value) playing.value = value
                    if (player.rate > 0f) {
                        animationFrameRateClose = AnimationFrame.addListener {
                            time.value = CMTimeGetSeconds(player.currentTime())
                        }
                    } else {
                        animationFrameRateClose?.invoke()
                        animationFrameRateClose = null
                    }
                }
            }
        }
}


@ViewDsl
actual fun ViewWriter.videoActual(setup: Video.() -> Unit): Unit = element(NVideo()) {
    handleTheme(this, viewDraws = false)
    context.addChildViewController(controller)
    calculationContext.onRemove {
        controller.removeFromParentViewController()
    }
    setup(Video(this))
}

actual inline var Video.source: VideoSource?
    get() = TODO()
    set(value) {
        when (value) {
            null -> {
                native.player = null
                native.informParentOfSizeChange()
            }
            is VideoRaw -> {
                TODO()
            }

            is VideoRemote -> {
                native.player = AVPlayer(NSURL(string = value.url))
                native.informParentOfSizeChange()
            }

            is VideoResource -> {
                native.player = AVPlayer(NSBundle.mainBundle.URLForResource(value.name, value.extension)!!)
                native.informParentOfSizeChange()
            }

            is VideoLocal -> {
                native.controller.player = null
                native.informParentOfSizeChange()
                value.file.provider.loadFileRepresentationForContentType(
                    value.file.suggestedType ?: UTTypeVideo,
                    openInPlace = true
                ) { url, b, err ->
                    if (url != null) {
                        dispatch_async(queue = dispatch_get_main_queue(), block = {
                            native.player = AVPlayer(url)
                            native.informParentOfSizeChange()
                        })
                    }
                }
            }

            else -> {}
        }
    }
@OptIn(ExperimentalForeignApi::class)
actual val Video.time: Writable<Double> get() = native.time
    .withWrite {
        native.controller.player?.seekToTime(CMTimeMake((it * 1000.0).toLong(), 1000))
    }
@OptIn(ExperimentalForeignApi::class)
actual val Video.playing: Writable<Boolean> get() = native.playing
    .withWrite {
        if(it)
            native.controller.player?.play()
        else
            native.controller.player?.pause()
    }
