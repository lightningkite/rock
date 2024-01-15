package com.lightningkite.rock.views.direct

import android.widget.ProgressBar
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

actual typealias NActivityIndicator = ProgressBar

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit) {
    return viewElement(factory = { ProgressBar(it, null, android.R.attr.progressBarStyleSmall) }, wrapper = ::ActivityIndicator) {
        handleTheme(native)
        setup(this)
    }
}