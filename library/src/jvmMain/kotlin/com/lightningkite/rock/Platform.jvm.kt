package com.lightningkite.rock

actual val Platform.Companion.current: Platform
    get() = Platform.Desktop
actual val Platform.Companion.probablyAppleUser: Boolean
    get() = false
actual val Platform.Companion.usesTouchscreen: Boolean
    get() = false