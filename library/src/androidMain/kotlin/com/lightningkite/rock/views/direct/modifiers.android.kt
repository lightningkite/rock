package com.lightningkite.rock.views.direct

import android.graphics.Color
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


val viewIsMarginless: WeakHashMap<View, Boolean> = WeakHashMap()

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
            val lp = (lparams as LinearLayout.LayoutParams)
            lp.weight = amount
            if((this.parent as LinearLayout).orientation == LinearLayout.HORIZONTAL) {
                lp.width = 0
            } else {
                lp.height = 0
            }
        } catch (ex: Throwable) {
            throw RuntimeException("Weight is only available within a column or row.")
        }
    }
    return ViewWrapper
}


@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    afterNextElementSetup {
        val params = this.lparams
        val horizontalGravity = when (horizontal) {
            Align.Start -> Gravity.START
            Align.Center -> Gravity.CENTER_HORIZONTAL
            Align.End -> Gravity.END
            else -> Gravity.CENTER_HORIZONTAL
        }
        val verticalGravity = when (vertical) {
            Align.Start -> Gravity.TOP
            Align.Center -> Gravity.CENTER_VERTICAL
            Align.End -> Gravity.BOTTOM
            else -> Gravity.CENTER_VERTICAL
        }
        if (params is LinearLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else if (params is FrameLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else
            println("Unknown layout params kind ${params::class.qualifiedName}; I am ${this::class.qualifiedName}")
        if (horizontal == Align.Stretch && (this.parent as? LinearLayout)?.orientation != LinearLayout.HORIZONTAL) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else if(params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (vertical == Align.Stretch && (this.parent as? LinearLayout)?.orientation != LinearLayout.VERTICAL) {
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
            isFillViewport = true
        }
        beforeNextElementSetup {
//            lparams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            lparams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        wrapNext(HorizontalScrollView(this.context)) {
            isFillViewport = true
        }
        beforeNextElementSetup {
//            lparams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            lparams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
//    if(constraints.maxHeight != null || constraints.maxWidth != null) {
//        wrapNext(FrameLayout(this.context)) {
//            layoutParams = ViewGroup.LayoutParams(
//                /* width = */ constraints.width?.value?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT,
//                /* height = */ constraints.height?.value?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }
//    } else {
        beforeNextElementSetup {
            constraints.minHeight?.let { minimumHeight = it.value.toInt() }
            constraints.minWidth?.let { minimumWidth = it.value.toInt() }
            constraints.width?.let { lparams.width = it.value.toInt() }
            constraints.height?.let { lparams.height = it.value.toInt() }
        }
//    }
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