package com.lightningkite.rockexample

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.mppexampleapp.*
import com.lightningkite.rock.RockActivity
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
            col {
                text {
                    content = "A"
                    native.background = ColorDrawable(Color.RED)
                }
                text {
                    content = "B"
                }
                text {
                    content = "C"
                    native.background = ColorDrawable(Color.RED)
                }
                expanding - text {
                    content = "D"
                }
                text {
                    content = "E"
                    native.background = ColorDrawable(Color.RED)
                }
                text {
                    content = "F"
                }
                text {
                    content = "G"
                    native.background = ColorDrawable(Color.RED)
                }
            }
        }
    }

    override val theme: suspend () -> Theme
        get() = { appTheme.await() }
}