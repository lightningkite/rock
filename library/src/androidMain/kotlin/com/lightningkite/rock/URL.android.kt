package com.lightningkite.rock

actual fun decodeURIComponent(content: String): String = android.net.Uri.decode(content)
actual fun encodeURIComponent(content: String): String  = android.net.Uri.encode(content)
