package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.mppexampleapp.*
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

class MainActivity : RockActivity() {
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