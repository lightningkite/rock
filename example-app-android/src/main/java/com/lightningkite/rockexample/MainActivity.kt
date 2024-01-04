package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.mppexampleapp.*
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.views.*

class MainActivity : RockActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).app()
    }
}