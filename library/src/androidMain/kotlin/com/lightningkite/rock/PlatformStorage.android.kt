package com.lightningkite.rock

import android.content.Context
import android.content.SharedPreferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object PlatformStorage {

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.applicationContext.getSharedPreferences("Rock.PLatformStorage", Context.MODE_PRIVATE)
    }
    actual fun get(key: String): String? {
        return preferences.getString(key, null)
    }

    actual fun set(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }
}