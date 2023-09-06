package com.lightningkite.mppexample

import kotlinx.browser.window

actual object PlatformStorage {
    actual fun get(key: String): String? {
        return window.localStorage.getItem(key)
    }

    actual fun set(key: String, value: String) {
        window.localStorage.setItem(key, value)
    }

    actual fun remove(key: String) {
        window.localStorage.removeItem(key)
    }
}
