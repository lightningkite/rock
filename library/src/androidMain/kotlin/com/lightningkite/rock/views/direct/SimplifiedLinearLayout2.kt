//package com.lightningkite.rock.views.direct
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.Gravity
//import android.view.View
//import android.view.ViewDebug
//import android.view.ViewGroup
//
//
///**
// * A drastically simplified variant of LinearLayout.
// */
//open class SimplifiedLinearLayout(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0,
//    defStyleRes: Int = 0
//) :
//    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
//
//    var vertical: Boolean = true
//        set(value) {
//            field = value
//            requestLayout()
//        }
//
//    override fun shouldDelayChildPressedState(): Boolean {
//        return false
//    }
//
//    var gap: Int = 0
//        set(value) {
//            field = value
//            requestLayout()
//        }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val primarySpec = if (vertical) heightMeasureSpec else widthMeasureSpec
//        val secondarySpec = if (vertical) widthMeasureSpec else heightMeasureSpec
//        var totalWeight = 0f
//        var primarySpace = if (vertical) paddingTop + paddingBottom else paddingLeft + paddingRight
//        var first = true
//        val primarySubSpec = MeasureSpec.makeMeasureSpec(
//            MeasureSpec.getSize(secondarySpec) - if (vertical) paddingTop + paddingBottom else paddingLeft + paddingRight,
//            when(val m = MeasureSpec.getMode(secondarySpec)) {
//                MeasureSpec.EXACTLY -> MeasureSpec.AT_MOST
//                else -> m
//            }
//        )
//        val secondarySubSpec = MeasureSpec.makeMeasureSpec(
//            MeasureSpec.getSize(secondarySpec) - if (vertical) paddingLeft + paddingRight else paddingTop + paddingBottom,
//            MeasureSpec.getMode(secondarySpec)
//        )
//        for (index in 0..<childCount) {
//            val child = getChildAt(index)
//            if (child.visibility == View.GONE) continue
//            if (first) {
//                first = false
//            } else {
//                primarySpace += gap
//            }
//            val params = child.layoutParams as LayoutParams
//            if (params.weight > 0f) {
//                totalWeight += params.weight
//                continue
//            }
//            child.measure(
//                if (vertical) secondarySubSpec else primarySubSpec,
//                if (vertical) primarySubSpec else secondarySubSpec
//            )
//            primarySpace += if (vertical) child.measuredHeight else child.measuredWidth
//        }
//        val primarySizeAndState = resolveSizeAndState(
//            primarySpace.coerceAtLeast(if (vertical) suggestedMinimumHeight else suggestedMinimumWidth),
//            primarySpec,
//            0
//        )
//        val remaining = MeasureSpec.getSize(primarySizeAndState) - primarySpace
//        for (index in 0..<childCount) {
//            val child = getChildAt(index)
//            if (child.visibility == View.GONE) continue
//            val params = child.layoutParams as LayoutParams
//            if (params.weight <= 0f) continue
//            val wr = params.weight / totalWeight
//            val primarySubSpec = MeasureSpec.makeMeasureSpec((remaining * wr).toInt(), MeasureSpec.EXACTLY)
//            child.measure(
//                if (vertical) secondarySubSpec else primarySubSpec,
//                if (vertical) primarySubSpec else secondarySubSpec
//            )
//        }
//        var secondarySpace = 0
//        for (index in 0..<childCount) {
//            val child = getChildAt(index)
//            if (child.visibility == View.GONE) continue
//            secondarySpace = secondarySpace.coerceAtLeast(if (vertical) child.measuredWidth else child.measuredHeight)
//        }
//        val secondarySizeAndState = resolveSizeAndState(
//            secondarySpace.coerceAtLeast(if (vertical) suggestedMinimumWidth else suggestedMinimumHeight),
//            secondarySpec,
//            0
//        )
//        setMeasuredDimension(
//            if (vertical) secondarySizeAndState else primarySizeAndState,
//            if (vertical) primarySizeAndState else secondarySizeAndState,
//        )
//    }
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        var primary = if (vertical) paddingTop else paddingStart
//        var first = true
//        for (index in 0..<childCount) {
//            val child = getChildAt(index)
//            if (child.visibility == View.GONE) continue
//            if (first) {
//                first = false
//            } else {
//                primary += gap
//            }
//            val params = child.layoutParams as LayoutParams
//            val childWidth = child.measuredWidth
//            val childHeight = child.measuredHeight
//            val secondaryAlign = if (vertical)
//                (params.gravity) and 0xF
//            else
//                (params.gravity shr Gravity.AXIS_Y_SHIFT) and 0xF
//            val secondaryPullBefore = secondaryAlign and Gravity.AXIS_PULL_BEFORE != 0
//            val secondaryPullAfter = secondaryAlign and Gravity.AXIS_PULL_AFTER != 0
//            val secondaryOffset = if (secondaryPullBefore) {
//                if (secondaryPullAfter) {
//                    // Stretch
//                    if (vertical) (b - t - childHeight) / 2
//                    else (r - l - childWidth) / 2
//                } else {
//                    // Start
//                    0
//                }
//            } else {
//                if (secondaryPullAfter) {
//                    // End
//                    if (vertical) (b - t - childHeight)
//                    else (r - l - childWidth)
//                } else {
//                    // Center
//                    if (vertical) (b - t - childHeight) / 2
//                    else (r - l - childWidth) / 2
//                }
//            }
//            if (vertical)
//                child.layout(
//                    l + secondaryOffset,
//                    t + primary,
//                    l + secondaryOffset + childWidth,
//                    t + primary + childHeight
//                )
//            else
//                child.layout(
//                    l + primary,
//                    t + secondaryOffset,
//                    r + primary + childWidth,
//                    t + secondaryOffset + childHeight
//                )
//            primary += (if (vertical) childHeight else childWidth) + gap
//        }
//    }
//
//    override fun generateLayoutParams(attrs: AttributeSet): SimplifiedLinearLayout.LayoutParams {
//        return LayoutParams(0, 0)
//    }
//
//    override fun generateDefaultLayoutParams(): SimplifiedLinearLayout.LayoutParams? {
//        if (vertical) {
//            return LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        } else {
//            return LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }
//    }
//
//    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): SimplifiedLinearLayout.LayoutParams {
//        if (lp is SimplifiedLinearLayout.LayoutParams) {
//            return LayoutParams(lp)
//        } else if (lp is MarginLayoutParams) {
//            return LayoutParams(lp)
//        } else {
//            return LayoutParams(lp)
//        }
//    }
//
//    open class LayoutParams : UseMarginsLayoutParams {
//        /**
//         * Indicates how much of the extra space in the LinearLayout will be
//         * allocated to the view associated with these LayoutParams. Specify
//         * 0 if the view should not be stretched. Otherwise the extra pixels
//         * will be pro-rated among all views whose weight is greater than 0.
//         */
//        @ViewDebug.ExportedProperty(category = "layout")
//        var weight = 0f
//
//        var maxWidth: Int = 0x00FFFFFF
//        var maxHeight: Int = 0x00FFFFFF
//
//        /**
//         * Gravity for the view associated with these LayoutParams.
//         *
//         * @see android.view.Gravity
//         */
//        @ViewDebug.ExportedProperty(
//            category = "layout",
//            mapping = [ViewDebug.IntToString(from = -1, to = "NONE"), ViewDebug.IntToString(
//                from = Gravity.NO_GRAVITY,
//                to = "NONE"
//            ), ViewDebug.IntToString(from = Gravity.TOP, to = "TOP"), ViewDebug.IntToString(
//                from = Gravity.BOTTOM,
//                to = "BOTTOM"
//            ), ViewDebug.IntToString(from = Gravity.LEFT, to = "LEFT"), ViewDebug.IntToString(
//                from = Gravity.RIGHT,
//                to = "RIGHT"
//            ), ViewDebug.IntToString(from = Gravity.START, to = "START"), ViewDebug.IntToString(
//                from = Gravity.END,
//                to = "END"
//            ), ViewDebug.IntToString(
//                from = Gravity.CENTER_VERTICAL,
//                to = "CENTER_VERTICAL"
//            ), ViewDebug.IntToString(
//                from = Gravity.FILL_VERTICAL,
//                to = "FILL_VERTICAL"
//            ), ViewDebug.IntToString(from = Gravity.CENTER_HORIZONTAL, to = "CENTER_HORIZONTAL"), ViewDebug.IntToString(
//                from = Gravity.FILL_HORIZONTAL,
//                to = "FILL_HORIZONTAL"
//            ), ViewDebug.IntToString(from = Gravity.CENTER, to = "CENTER"), ViewDebug.IntToString(
//                from = Gravity.FILL,
//                to = "FILL"
//            )]
//        )
//        var gravity = -1
//
//        /**
//         * {@inheritDoc}
//         */
//        constructor(width: Int, height: Int) : super(width, height) {
//            weight = 0f
//        }
//
//        /**
//         * Creates a new set of layout parameters with the specified width, height
//         * and weight.
//         *
//         * @param width the width, either [.MATCH_PARENT],
//         * [.WRAP_CONTENT] or a fixed size in pixels
//         * @param height the height, either [.MATCH_PARENT],
//         * [.WRAP_CONTENT] or a fixed size in pixels
//         * @param weight the weight
//         */
//        constructor(width: Int, height: Int, weight: Float) : super(width, height) {
//            this.weight = weight
//        }
//
//        /**
//         * {@inheritDoc}
//         */
//        constructor(p: ViewGroup.LayoutParams) : super(p)
//
//        /**
//         * Copy constructor. Clones the width, height, margin values, weight,
//         * and gravity of the source.
//         *
//         * @param source The layout params to copy from.
//         */
//        constructor(source: SimplifiedLinearLayout.LayoutParams) : super(source) {
//            weight = source.weight
//            gravity = source.gravity
//        }
//    }
//}
