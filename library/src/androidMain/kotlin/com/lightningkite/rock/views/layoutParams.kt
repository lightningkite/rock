package com.lightningkite.rock.views

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.lightningkite.rock.views.direct.DesiredSizeView

val View.lparams: ViewGroup.LayoutParams get() {
    val parent = parent
    if(parent is DesiredSizeView) return parent.lparams
    if(layoutParams != null) return layoutParams
    val newParams = when(parent) {
        is LinearLayout -> LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        is FrameLayout -> FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        is NestedScrollView -> FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        is ScrollView -> FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        is HorizontalScrollView -> FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        null -> {
            println("No parent to identify LayoutParams type for a ${this::class.qualifiedName}")
            ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        else -> throw Exception("Unknown parent type ${parent::class.qualifiedName}")
    }
    layoutParams = newParams
    return newParams
}