package com.lightningkite.rock.views.direct

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.updateLayoutParams
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.PopoverPreferredDirection
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.views.*
import java.util.*


internal val viewIsMarginless: WeakHashMap<View, Boolean> = WeakHashMap()

@ViewModifierDsl3
actual val ViewWriter.marginless: ViewWrapper
    get() {
        beforeNextElementSetup {
            viewIsMarginless[this] = true
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper
    get() {
        beforeNextElementSetup {
            val padding = (AndroidAppContext.density * 8).toInt()
            this.setPadding(
                padding,
                padding,
                padding,
                padding
            )
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    beforeNextElementSetup {
        try {
            this.updateLayoutParams {
                (this as LinearLayout.LayoutParams).weight = amount
            }
        } catch (ex: Throwable) {
            throw RuntimeException("Weight is only available within a column or row.")
        }
    }
    return ViewWrapper
}

private fun alignmentToGravity(alignment: Align, isVertical: Boolean): Int {
    return when (alignment) {
        Align.Start -> Gravity.START
        Align.Center -> if (isVertical) Gravity.CENTER_VERTICAL else Gravity.CENTER_HORIZONTAL
        Align.End -> Gravity.END
        else -> Gravity.START
    }
}

@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        val params = this.lparams
        val horizontalGravity = alignmentToGravity(horizontal, isVertical = false)
        val verticalGravity = alignmentToGravity(vertical, isVertical = true)
        if (params is LinearLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else if (params is FrameLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        if (horizontal == Align.Stretch) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else if(params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (vertical == Align.Stretch) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else if(params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        wrapNext(ScrollView(this.context)) {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        wrapNext(HorizontalScrollView(this.context)) {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    wrapNext(FrameLayout(this.context)) {
        layoutParams = ViewGroup.LayoutParams(
            /* width = */ constraints.width?.value?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT,
            /* height = */ constraints.height?.value?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit,
): ViewWrapper {
//    TODO("Not yet implemented")
    return ViewWrapper
}