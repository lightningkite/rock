package com.lightningkite.kiteui

actual val Platform.Companion.current: Platform
    get() = Platform.Android
actual val Platform.Companion.probablyAppleUser: Boolean
    get() = false
actual val Platform.Companion.usesTouchscreen: Boolean
    get() = true