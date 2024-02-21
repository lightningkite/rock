package com.lightningkite.rock

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.DataTransfer
import org.w3c.dom.HTMLInputElement

actual object ExternalServices {
    actual fun openTab(url: String) {
        window.open(url, "_blank")
    }
    fun requestFileInput(mimeTypes: List<String>, setup: HTMLInputElement.()->Unit, onResult: (List<FileReference>) -> Unit) {
        (document.createElement("input") as HTMLInputElement).apply {
            type = "file"
            accept = mimeTypes.joinToString(",")
            setup()
            onchange = { e -> onResult(files?.let { (0 until it.length).map { index -> it.item(index)!! } } ?: listOf()) }
        }.click()
    }
    actual fun requestFile(mimeTypes: List<String>, onResult: (FileReference?)->Unit) = requestFileInput(mimeTypes, {}, { onResult(it.firstOrNull()) })
    actual fun requestFiles(mimeTypes: List<String>, onResult: (List<FileReference>)->Unit) = requestFileInput(mimeTypes, { multiple = true }, { onResult(it) })
    actual fun requestCaptureSelf(mimeTypes: List<String>, onResult: (FileReference?)->Unit) = requestFileInput(mimeTypes, { setAttribute("capture", "user") }, { onResult(it.firstOrNull()) })
    actual fun requestCaptureEnvironment(mimeTypes: List<String>, onResult: (FileReference?)->Unit) = requestFileInput(mimeTypes, { setAttribute("capture", "environment") }, { onResult(it.firstOrNull()) })
    actual fun setClipboardText(value: String) { window.navigator.clipboard.writeText(value) }
}