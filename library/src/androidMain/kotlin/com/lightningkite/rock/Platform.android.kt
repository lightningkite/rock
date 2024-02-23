package com.lightningkite.rock

actual val Platform.Companion.current: Platform
    get() = Platform.Android
actual val Platform.Companion.probablyAppleUser: Boolean
    get() = false