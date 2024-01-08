package com.lightningkite.rock.views.direct

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.ActionBar
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.lparams

@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {
    viewElement(factory = ::NSeparator, wrapper = ::Separator) {
        native.lparams.run {
            width = ActionBar.LayoutParams.MATCH_PARENT
            height = (2 * native.resources.displayMetrics.density).toInt()
        }
        handleTheme(native) { it, v ->
            v.background = ColorDrawable(it.foreground.closestColor().colorInt())
            v.alpha = 0.25f
        }
        native.minimumWidth = 1
        native.minimumHeight = 1
        setup(this)
    }
}

actual class NSeparator : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}