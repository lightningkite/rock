package com.lightningkite.mppexample

import kotlinx.browser.window

actual typealias UUID = String

actual fun uuid(): UUID = window.asDynamic().crypto.randomUUID() as String
