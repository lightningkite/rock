package com.lightningkite.kiteui

expect object PlatformStorage {
    fun get(key: String): String?
    fun set(key: String, value: String)
    fun remove(key: String)
}
