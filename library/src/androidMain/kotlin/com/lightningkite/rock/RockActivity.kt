package com.lightningkite.rock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.views.AndroidAppContext

open class RockActivity : AppCompatActivity() {
    open lateinit var navigator: RockNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidAppContext.applicationCtx = this.applicationContext
        PlatformStorage.initialize(this)
    }
}