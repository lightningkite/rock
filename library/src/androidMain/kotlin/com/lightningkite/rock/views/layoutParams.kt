package com.lightningkite.rock.views

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

val View.lparams: ViewGroup.LayoutParams get() {
    if(layoutParams != null) return layoutParams
    val parent = parent
    val newParams = when(parent) {
        is LinearLayout -> LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        is FrameLayout -> FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        null -> {
            println("No parent to identify LayoutParams type")
            ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        else -> throw Exception("Unknown parent type ${parent::class.qualifiedName}")
    }
    layoutParams = newParams
    return newParams
}