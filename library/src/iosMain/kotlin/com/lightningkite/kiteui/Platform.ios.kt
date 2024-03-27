package com.lightningkite.kiteui

actual val Platform.Companion.probablyAppleUser: Boolean
    get() = true
actual val Platform.Companion.usesTouchscreen: Boolean
    get() = true