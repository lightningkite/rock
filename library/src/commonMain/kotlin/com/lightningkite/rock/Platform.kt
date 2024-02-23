package com.lightningkite.rock

enum class Platform {
    iOS, Android, Web, Desktop
    ;
    companion object
}
expect val Platform.Companion.current: Platform
expect val Platform.Companion.probablyAppleUser: Boolean
