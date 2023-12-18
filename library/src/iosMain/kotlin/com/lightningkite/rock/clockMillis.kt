package com.lightningkite.rock

import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970

actual fun clockMillis(): Double = NSDate.date().timeIntervalSince1970 * 1000.0