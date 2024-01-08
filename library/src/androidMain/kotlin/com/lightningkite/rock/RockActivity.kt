package com.lightningkite.rock

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.views.AndroidAppContext
import timber.log.Timber

abstract class RockActivity : AppCompatActivity() {
    open lateinit var navigator: RockNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidAppContext.applicationCtx = this.applicationContext
        Timber.plant(Timber.DebugTree())
    }

    private var currentNum = 0
    private val onResults = HashMap<Int, (Int, Intent?)->Unit>()
    fun startActivityForResult(intent: Intent, options: Bundle? = null, onResult: (Int, Intent?)->Unit) {
        val requestCode = currentNum++
        onResults[requestCode] = onResult
        ActivityCompat.startActivityForResult(this, intent, requestCode, options)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onResults[requestCode]?.invoke(resultCode, data)
        onResults.remove(requestCode)
        super.onActivityResult(requestCode, resultCode, data)
    }
    private val onPermissions = HashMap<Int, (PermissionResult)->Unit>()
    data class PermissionResult(val map: Map<String, Int>) {
        val accepted: Boolean get() = map.values.all { it == PackageManager.PERMISSION_GRANTED }
    }
    fun requestPermissions(vararg permissions: String, onResult: (PermissionResult)->Unit) {
        val ungranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if(ungranted.isEmpty()) return onResult(PermissionResult(mapOf()))
        val requestCode = currentNum++
        onPermissions[requestCode] = onResult
        ActivityCompat.requestPermissions(this, ungranted.toTypedArray(), requestCode)
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT >= 23) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            onPermissions[requestCode]?.invoke(PermissionResult(permissions.indices.associate { permissions[it] to grantResults[it] }))
            onPermissions.remove(requestCode)
        }
    }
}