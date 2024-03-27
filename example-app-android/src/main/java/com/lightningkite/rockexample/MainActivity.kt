package com.lightningkite.kiteuiexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.mppexampleapp.*
import com.lightningkite.kiteui.KiteUiActivity
import com.lightningkite.kiteui.models.Align
import com.lightningkite.kiteui.models.Theme
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*

class MainActivity : KiteUiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCacheDir.setReadOnly()
        val frame = FrameLayout(this)
        setContentView(frame)
        with(ViewWriter(frame)) {
            app()
//            stack {
//                gravity(Align.Stretch, Align.Start) - row {
//                    card - text("A")
//                    card - text("B")
//                    card - text("C")
//                }
//                gravity(Align.Stretch, Align.Center) - row {
//                    space()
//                    card - text("A")
//                    card - text("B")
//                    card - text("C")
//                    space()
//                }
//            }
        }
    }

    override val theme: suspend () -> Theme
        get() = { appTheme.await() }
}