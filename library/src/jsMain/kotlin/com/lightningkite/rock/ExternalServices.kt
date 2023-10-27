package com.lightningkite.rock

import kotlinx.browser.window

actual object ExternalServices {
    actual fun openTab(url: String) {
        window.open(url, "_blank")
    }
}