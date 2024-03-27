package com.lightningkite.kiteui.models

sealed class CornerRadii {
    data class Constant(val value: Dimension): CornerRadii()
    data class RatioOfSpacing(val value: Float): CornerRadii()
}