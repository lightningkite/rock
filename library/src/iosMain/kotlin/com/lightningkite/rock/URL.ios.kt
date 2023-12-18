package com.lightningkite.rock

import platform.Foundation.*
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters

private val component = NSCharacterSet.characterSetWithCharactersInString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~")
actual fun decodeURIComponent(content: String): String = (content as NSString).stringByRemovingPercentEncoding()!!
actual fun encodeURIComponent(content: String): String = (content as NSString).stringByAddingPercentEncodingWithAllowedCharacters(component)!!
