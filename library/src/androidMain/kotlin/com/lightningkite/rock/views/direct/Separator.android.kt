package com.lightningkite.rock.views.direct

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.lparams


@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit {
    val c = currentView
    if(c !is LinearLayoutCompat) throw IllegalStateException("Separators can only be used inside rows or columns")
    viewElement(factory = { NSeparator(it, c.orientation == LinearLayoutCompat.HORIZONTAL) }, wrapper = ::Separator) {
        handleTheme(native) { it, v ->
            v.background = ColorDrawable(it.foreground.closestColor().colorInt())
            v.alpha = 0.25f
            val size = it.outlineWidth.value.coerceAtLeast(1f).toInt()
            v.thickness = size
            (v.parent as? LinearLayoutCompat)?.let {
                v.lparams.run {
                    width = if(it.orientation == LinearLayoutCompat.HORIZONTAL) size else LinearLayoutCompat.LayoutParams.MATCH_PARENT
                    height = if(it.orientation == LinearLayoutCompat.HORIZONTAL) LinearLayoutCompat.LayoutParams.MATCH_PARENT else size
                }
            }
        }
        native.minimumWidth = 1
        native.minimumHeight = 1
        setup(this)
    }
}

actual class NSeparator(context: Context, val containerHorizontal: Boolean) : View(context) {
    var thickness: Int = 1
        set(value) {
            field = value
            requestLayout()
        }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val thicknessMode = MeasureSpec.getMode(if(!containerHorizontal) widthMeasureSpec else heightMeasureSpec)
        var newThickness = if(!containerHorizontal) measuredWidth else measuredHeight
        if (thicknessMode == MeasureSpec.AT_MOST || thicknessMode == MeasureSpec.UNSPECIFIED) {
            if (thickness > 0 && newThickness != thickness) {
                newThickness = thickness
            }
            setMeasuredDimension(if(!containerHorizontal) newThickness else measuredWidth, if(!containerHorizontal) measuredHeight else newThickness)
        }
    }
}