package com.lightningkite.rock.views.direct

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.lparams

@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {
    viewElement(factory = ::NSeparator, wrapper = ::Separator) {
        handleTheme(native) { it, v ->
            v.background = ColorDrawable(it.foreground.closestColor().colorInt())
            v.alpha = 0.25f
            val size = it.outlineWidth.value.coerceAtMost(1f).toInt()
            (v.parent as? LinearLayout)?.let {
                v.lparams.run {
                    width = if(it.orientation == LinearLayout.HORIZONTAL) size else ViewGroup.LayoutParams.MATCH_PARENT
                    height = if(it.orientation == LinearLayout.HORIZONTAL) ViewGroup.LayoutParams.MATCH_PARENT else size
                }
            }
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