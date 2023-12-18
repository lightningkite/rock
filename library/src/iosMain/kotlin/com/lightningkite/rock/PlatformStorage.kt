package com.lightningkite.rock

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual object PlatformStorage {
    actual fun get(key: String): String? = NSUserDefaults.standardUserDefaults.stringForKey(key)
    actual fun set(key: String, value: String) {
        NSUserDefaults.standardUserDefaults.setObject(value, key)
    }
    actual fun remove(key: String) {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(key)
    }
}
