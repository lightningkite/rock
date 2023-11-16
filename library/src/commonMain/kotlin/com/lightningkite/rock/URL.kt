package com.lightningkite.rock

external fun decodeURIComponent(content: String): String
external fun encodeURIComponent(content: String): String
external fun decodeURI(content: String): String
external fun encodeURI(content: String): String

fun String?.decodeURLParams(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    if (this == null) return map
    val items = this.split("&")
    items.forEach { item ->
        val parts = item.split("=")
        if (parts.size == 2) map[parts[0]] = decodeURIComponent(parts[1])
        else map[parts[0]] = ""
    }
    return map
}

fun Map<String, Any?>.toURLParams(): String {
    return buildString {
        var first = true
        for (entry in this@toURLParams) {
            if (entry.value == null || entry.value.toString().isEmpty()) continue
            if (first) {
                first = false
                append("?")
            } else append("&")
            append(encodeURIComponent(entry.key))
            append("=")
            append(encodeURIComponent(entry.value.toString()))
        }
    }
}
