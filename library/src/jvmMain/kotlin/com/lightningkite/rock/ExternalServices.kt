package com.lightningkite.rock

actual object ExternalServices {
    actual fun openTab(url: String) = Unit
    actual fun requestFile(mimeTypes: List<String>, onResult: (FileReference?)->Unit): Unit = TODO()
    actual fun requestFiles(mimeTypes: List<String>, onResult: (List<FileReference>)->Unit): Unit = TODO()
    actual fun requestCaptureSelf(mimeTypes: List<String>, onResult: (FileReference?)->Unit): Unit = TODO()
    actual fun requestCaptureEnvironment(mimeTypes: List<String>, onResult: (FileReference?)->Unit): Unit = TODO()
}