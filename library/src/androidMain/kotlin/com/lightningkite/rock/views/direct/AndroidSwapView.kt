package com.lightningkite.rock.views.direct

import android.content.Context
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import com.lightningkite.rock.models.ScreenTransition

class AndroidSwapView: FrameLayout {

    private var windowInsetsListenerCopy: OnApplyWindowInsetsListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setOnApplyWindowInsetsListener(listener: OnApplyWindowInsetsListener?) {
        this.windowInsetsListenerCopy = listener
        super.setOnApplyWindowInsetsListener(listener)
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val newInsets = this.windowInsetsListenerCopy?.onApplyWindowInsets(this, insets) ?: insets
        val count = childCount
        for (i in 0 until count) {
            getChildAt(i).dispatchApplyWindowInsets(newInsets)
        }
        return newInsets
    }

    private var currentView: View? = null
    private val hasCurrentView get() = currentView != null

    /**
     * Swaps from the current view to another one with the given [screenTransition].
     */
    fun swap(to: View?, screenTransition: ScreenTransition) {
        val oldView = currentView
        val newView = to ?: View(context)
        TransitionManager.beginDelayedTransition(this, TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            screenTransition.exit?.addTarget(oldView ?: View(context))
            screenTransition.enter?.addTarget(newView)
            screenTransition.enter?.let { addTransition(it) }
            screenTransition.exit?.let { addTransition(it) }
        })
        this.removeView(oldView)
        this.addView(newView)

        currentView = newView
//        post {
//            runKeyboardUpdate(newView, oldView)
//        }
    }
}