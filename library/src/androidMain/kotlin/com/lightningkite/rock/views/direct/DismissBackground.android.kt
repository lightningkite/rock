package com.lightningkite.rock.views.direct

import android.content.Context
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

actual fun DismissBackground.onClick(action: suspend () -> Unit) {}
actual class NDismissBackground(c: Context) : NView(c)

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) {
    currentView.background = null
}