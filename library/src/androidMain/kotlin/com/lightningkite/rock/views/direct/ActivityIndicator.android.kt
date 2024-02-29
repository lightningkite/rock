package com.lightningkite.rock.views.direct

import android.content.res.ColorStateList
import android.widget.ProgressBar
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

actual typealias NActivityIndicator = ProgressBar

@ViewDsl
actual inline fun ViewWriter.activityIndicatorActual(crossinline setup: ActivityIndicator.() -> Unit) {
    return viewElement(factory = { ProgressBar(it, null, android.R.attr.progressBarStyleSmall) }, wrapper = ::ActivityIndicator) {
        handleTheme(native, foreground = {
            theme: Theme, progressBar: ProgressBar ->
            progressBar.indeterminateTintList = ColorStateList.valueOf(theme.foreground.colorInt())
        })
        setup(this)
    }
}