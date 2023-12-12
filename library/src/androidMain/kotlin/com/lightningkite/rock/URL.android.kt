package com.lightningkite.rock

actual fun decodeURIComponent(content: String): String = android.net.Uri.decode(content)
actual fun encodeURIComponent(content: String): String  = android.net.Uri.encode(content)
actual fun decodeURI(content: String): String = android.net.Uri.decode(content)
actual fun encodeURI(content: String): String = android.net.Uri.encode(content)
