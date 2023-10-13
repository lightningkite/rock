package com.lightningkite.rock

// TODO replace this with a proper implementation of URL encoding. this is just a quick proof of concept to get things working

val reverseCharMap by lazy {
    val charMap = mutableMapOf<String, Char>()
    "0123456789ABCDEF".forEachIndexed { index, c ->
        charMap["%${index.toString(16)}"] = c
    }
    charMap["%20"] = ' '
    charMap["%21"] = '!'
    charMap["%22"] = '"'
    charMap["%23"] = '#'
    charMap["%24"] = '$'
    charMap["%25"] = '%'
    charMap["%26"] = '&'
    charMap["%27"] = '\''
    charMap["%28"] = '('
    charMap["%29"] = ')'
    charMap["%2A"] = '*'
    charMap["%2B"] = '+'
    charMap["%2C"] = ','
    charMap["%2D"] = '-'
    charMap["%2E"] = '.'
    charMap["%2F"] = '/'
    charMap["%3A"] = ':'
    charMap["%3B"] = ';'
    charMap["%3C"] = '<'
    charMap["%3D"] = '='
    charMap["%3E"] = '>'
    charMap["%3F"] = '?'
    charMap["%40"] = '@'
    charMap["%5B"] = '['
    charMap["%5C"] = '\\'
    charMap["%5D"] = ']'
    charMap["%5E"] = '^'
    charMap["%5F"] = '_'
    charMap["%60"] = '`'
    charMap["%7B"] = '{'
    charMap["%7C"] = '|'
    charMap["%7D"] = '}'
    charMap["%7E"] = '~'
    charMap
}

val charMap by lazy {
    val charMap = mutableMapOf<Char, String>()
    "0123456789ABCDEF".forEachIndexed { index, c ->
        charMap[c] = "%${index.toString(16)}"
    }
    charMap[' '] = "%20"
    charMap['!'] = "%21"
    charMap['"'] = "%22"
    charMap['#'] = "%23"
    charMap['$'] = "%24"
    charMap['%'] = "%25"
    charMap['&'] = "%26"
    charMap['\''] = "%27"
    charMap['('] = "%28"
    charMap[')'] = "%29"
    charMap['*'] = "%2A"
    charMap['+'] = "%2B"
    charMap[','] = "%2C"
    charMap['-'] = "%2D"
    charMap['.'] = "%2E"
    charMap['/'] = "%2F"
    charMap[':'] = "%3A"
    charMap[';'] = "%3B"
    charMap['<'] = "%3C"
    charMap['='] = "%3D"
    charMap['>'] = "%3E"
    charMap['?'] = "%3F"
    charMap['@'] = "%40"
    charMap['['] = "%5B"
    charMap['\\'] = "%5C"
    charMap[']'] = "%5D"
    charMap['^'] = "%5E"
    charMap['_'] = "%5F"
    charMap['`'] = "%60"
    charMap['{'] = "%7B"
    charMap['|'] = "%7C"
    charMap['}'] = "%7D"
    charMap['~'] = "%7E"
    charMap
}

fun String?.uriEncode(): String {
    if (this == null) return ""
    return buildString {
        for (c in this@uriEncode) {
            charMap[c]?.let { append(it) } ?: append(c)
        }
    }
}

fun String?.uriDecode(): String {
    if (this == null) return ""
    return buildString {
        var i = 0
        while (i < this@uriDecode.length) {
            val c = this@uriDecode[i]
            if (c == '%') {
                val code = this@uriDecode.substring(i, i + 3)
                i += 3
                reverseCharMap[code]?.let { append(it) }
            } else {
                append(c)
                i++
            }
        }
    }
}

fun String?.decodeURLParams(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    if (this == null) return map
    val items = this.split("&")
    items.forEach { item ->
        val parts = item.split("=")
        if (parts.size == 2) map[parts[0]] = parts[1].uriDecode()
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
            append(entry.key.uriEncode())
            append("=")
            append(entry.value.toString().uriEncode())
        }
    }
}
