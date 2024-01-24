package com.lightningkite.rock.views.direct

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch

actual fun DismissBackground.onClick(action: suspend () -> Unit) {
    native.setOnClickListener { _ ->
        launch { action() }
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = ViewGroup

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit) = element(FrameLayout(context)) {
    handleTheme(this) { it, view ->
        view.setBackgroundColor(it.background.closestColor().copy(alpha = 0.5f).toInt())
    }
    setup(DismissBackground(this))
}