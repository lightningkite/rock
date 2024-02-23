package com.lightningkite.rock

import kotlinx.browser.window

actual val Platform.Companion.current: Platform
    get() = Platform.Web
actual val Platform.Companion.probablyAppleUser: Boolean
    get() = window.navigator.userAgent.contains("Safari")