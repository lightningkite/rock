package com.lightningkite.rock

actual object ExternalServices {
    actual fun openTab(url: String) {
    }

    actual fun requestFile(
        mimeTypes: List<String>,
        onResult: (FileReference?) -> Unit,
    ) {
    }

    actual fun requestFiles(
        mimeTypes: List<String>,
        onResult: (List<FileReference>) -> Unit,
    ) {
    }

    actual fun requestCaptureSelf(
        mimeTypes: List<String>,
        onResult: (List<FileReference>) -> Unit,
    ) {
    }

    actual fun requestCaptureEnvironment(
        mimeTypes: List<String>,
        onResult: (List<FileReference>) -> Unit,
    ) {
    }
}