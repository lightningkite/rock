package com.lightningkite.kiteui

enum class Platform {
    iOS, Android, Web, Desktop
    ;
    companion object
}
expect val Platform.Companion.current: Platform
expect val Platform.Companion.probablyAppleUser: Boolean
expect val Platform.Companion.usesTouchscreen: Boolean
