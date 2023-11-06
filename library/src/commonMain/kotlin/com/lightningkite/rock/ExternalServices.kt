package com.lightningkite.rock

expect object ExternalServices {
    fun openTab(url: String)
    fun requestFile(mimeTypes: List<String> = listOf("*/*"), onResult: (FileReference?)->Unit)
    fun requestFiles(mimeTypes: List<String> = listOf("*/*"), onResult: (List<FileReference>)->Unit)
    fun requestCaptureSelf(mimeTypes: List<String> = listOf("image/*"), onResult: (List<FileReference>)->Unit)
    fun requestCaptureEnvironment(mimeTypes: List<String> = listOf("image/*"), onResult: (List<FileReference>)->Unit)
}
