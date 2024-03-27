package com.lightningkite.kiteui

import com.lightningkite.kiteui.views.extensionStrongRef
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume

actual object ExternalServices {
    actual fun openTab(url: String) {
        UIApplication.sharedApplication.openURL(
            url = NSURL(string = url),
            options = mapOf<Any?, Any?>(),
            completionHandler = {})
    }

    var currentPresenter: (UIViewController) -> Unit = {}
    actual fun requestFile(mimeTypes: List<String>, onResult: (FileReference?) -> Unit): Unit {
        val imagePickerCompat = mimeTypes.all { it.startsWith("image/") || it.startsWith("video/") }
        if (imagePickerCompat) {
            val controller = PHPickerViewController(PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary()).apply {
                filter = PHPickerFilter.anyFilterMatchingSubfilters(
                    listOfNotNull(
                        PHPickerFilter.imagesFilter.takeIf { mimeTypes.any { it.startsWith("image/") } },
                        PHPickerFilter.videosFilter.takeIf { mimeTypes.any { it.startsWith("video/") } },
                    )
                )
                preferredAssetRepresentationMode = PHPickerConfigurationAssetRepresentationModeCompatible
                selectionLimit = 1
            })
            val delegate =
                object : NSObject(), PHPickerViewControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                        picker.dismissViewControllerAnimated(true) {
                            dispatch_async(queue = dispatch_get_main_queue(), block = {
                                (didFinishPicking.firstOrNull() as? PHPickerResult)?.let { result ->
                                    onResult(FileReference(result.itemProvider))
                                } ?: onResult(null)
                            })
                        }
                    }
                }
            controller.delegate = delegate
            controller.extensionStrongRef = delegate
            currentPresenter(controller)
        } else {
            println("Docs picker not implemented yet")
        }
    }

    actual fun requestFiles(mimeTypes: List<String>, onResult: (List<FileReference>) -> Unit): Unit {
        val imagePickerCompat = mimeTypes.all { it.startsWith("image/") || it.startsWith("video/") }
        if (imagePickerCompat) {
            val controller = PHPickerViewController(PHPickerConfiguration(PHPhotoLibrary.sharedPhotoLibrary()).apply {
                filter = PHPickerFilter.anyFilterMatchingSubfilters(
                    listOfNotNull(
                        PHPickerFilter.imagesFilter.takeIf { mimeTypes.any { it.startsWith("image/") } },
                        PHPickerFilter.videosFilter.takeIf { mimeTypes.any { it.startsWith("video/") } },
                    )
                )
                preferredAssetRepresentationMode = PHPickerConfigurationAssetRepresentationModeCompatible
                selectionLimit = Int.MAX_VALUE.toLong()
            })
            val delegate =
                object : NSObject(), PHPickerViewControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                        picker.dismissViewControllerAnimated(true) {
                            dispatch_async(queue = dispatch_get_main_queue(), block = {
                                didFinishPicking.filterIsInstance<PHPickerResult>()
                                    .map { result -> FileReference(result.itemProvider) }
                                    .let(onResult)
                            })
                        }
                    }
                }
            controller.delegate = delegate
            controller.extensionStrongRef = delegate
            currentPresenter(controller)
        } else {
            println("Docs picker not implemented yet")
        }
    }

    actual fun requestCaptureSelf(mimeTypes: List<String>, onResult: (FileReference?) -> Unit): Unit {
        if (mimeTypes.all { it.startsWith("image/") }) {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceFront,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
                onResult
            )
        } else if(mimeTypes.all { it.startsWith("video/") }) {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceFront,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModeVideo,
                onResult
            )
        } else {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceFront,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
                onResult
            )
        }
    }

    actual fun requestCaptureEnvironment(mimeTypes: List<String>, onResult: (FileReference?) -> Unit): Unit {
        if (mimeTypes.all { it.startsWith("image/") }) {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
                onResult
            )
        } else if(mimeTypes.all { it.startsWith("video/") }) {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModeVideo,
                onResult
            )
        } else {
            requestCapture(
                UIImagePickerControllerCameraDevice.UIImagePickerControllerCameraDeviceRear,
                UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto,
                onResult
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun requestCapture(
        camera: UIImagePickerControllerCameraDevice,
        mode: UIImagePickerControllerCameraCaptureMode,
        onResult: (FileReference?) -> Unit
    ): Unit {
        val controller = UIImagePickerController()
        controller.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        controller.cameraDevice = camera
        controller.cameraCaptureMode = mode
        val delegate =
            object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                override fun imagePickerController(
                    picker: UIImagePickerController,
                    didFinishPickingMediaWithInfo: Map<Any?, *>
                ) {
                    val url = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
                        ?: didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL

                    url?.let {
                        dispatch_async(queue = dispatch_get_main_queue(), block = {
                            onResult(FileReference(NSItemProvider(contentsOfURL = it)))
                        })
                        return
                    }

                    val image = didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage] as? UIImage
                        ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

                    val asFile = image?.let {
                        val p = NSURL(fileURLWithPath = NSTemporaryDirectory())
                        val u = NSURL(string = "${NSUUID()}", relativeToURL = p)
                        NSFileManager.defaultManager.createDirectoryAtPath(
                            path = p.path!!,
                            withIntermediateDirectories = true,
                            attributes = null,
                            error = null
                        )
                        UIImageJPEGRepresentation(it, 0.98)!!.writeToURL(url = u, atomically = true)
                        FileReference(NSItemProvider(contentsOfURL = u))
                    }

                    picker.dismissViewControllerAnimated(true) {
                        dispatch_async(queue = dispatch_get_main_queue(), block = {
                            onResult(asFile)
                        })
                    }
                }
            }
        controller.delegate = delegate
        controller.extensionStrongRef = delegate
        currentPresenter(controller)
        return
    }
    actual fun setClipboardText(value: String) {
        UIPasteboard.generalPasteboard.string = value
    }
}