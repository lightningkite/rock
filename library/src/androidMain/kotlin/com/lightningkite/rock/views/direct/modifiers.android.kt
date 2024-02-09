package com.lightningkite.rock.views.direct

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.PopoverPreferredDirection
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.views.*
import java.util.*


val viewIsMarginless: WeakHashMap<View, Boolean> = WeakHashMap()
val viewHasPadding: WeakHashMap<View, Boolean> = WeakHashMap()

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
            viewHasPadding[this] = true
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    beforeNextElementSetup {
        try {
            val lp = (lparams as LinearLayoutCompat.LayoutParams)
            lp.weight = amount
            if((this.parent as LinearLayoutCompat).orientation == LinearLayoutCompat.HORIZONTAL) {
                lp.width = 0
            } else {
                lp.height = 0
            }
        } catch (ex: Throwable) {
            throw RuntimeException("Weight is only available within a column or row, but the parent is a ${parent?.let {it::class.simpleName}}")
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
        if (params is LinearLayoutCompat.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else if (params is FrameLayout.LayoutParams)
            params.gravity = horizontalGravity or verticalGravity
        else
            println("Unknown layout params kind ${params::class.qualifiedName}; I am ${this::class.qualifiedName}")
        if (horizontal == Align.Stretch && (this.parent as? LinearLayoutCompat)?.orientation != LinearLayoutCompat.HORIZONTAL) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else if(params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        if (vertical == Align.Stretch && (this.parent as? LinearLayoutCompat)?.orientation != LinearLayoutCompat.VERTICAL) {
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
        wrapNext(NestedScrollView(this.context)) {
            isFillViewport = true
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        wrapNext(HorizontalScrollView(this.context)) {
            isFillViewport = true
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