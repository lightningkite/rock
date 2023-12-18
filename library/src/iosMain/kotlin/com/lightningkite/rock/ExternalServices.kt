package com.lightningkite.rock

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual object ExternalServices {
    actual fun openTab(url: String) {
        UIApplication.sharedApplication.openURL(
            url = NSURL(string = url),
            options = mapOf<Any?, Any?>(),
            completionHandler = {})
    }
    actual fun requestFile(mimeTypes: List<String>, onResult: (FileReference?)->Unit): Unit = TODO()
    actual fun requestFiles(mimeTypes: List<String>, onResult: (List<FileReference>)->Unit): Unit = TODO()
    actual fun requestCaptureSelf(mimeTypes: List<String>, onResult: (List<FileReference>)->Unit): Unit = TODO()
    actual fun requestCaptureEnvironment(mimeTypes: List<String>, onResult: (List<FileReference>)->Unit): Unit = TODO()
}