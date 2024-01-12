package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch

actual fun DismissBackground.onClick(action: suspend () -> Unit) {
    native.setOnClickListener { _ ->
        launch { action() }
    }
}
actual class NDismissBackground(c: Context) : NView(c) {

}

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) = element(NDismissBackground(context)) {
    handleTheme(this) { it, view ->
        view.setBackgroundColor(it.background.closestColor().copy(alpha = 0.5f).toInt())
    }
    setup(DismissBackground(this))
}