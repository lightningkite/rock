package com.lightningkite.mppexample

data class Background(
    val fill: Paint? = null,
    val stroke: Color? = null,
    val strokeWidth: Dimension? = null,
    val corners: CornerRadii? = null,
) {
    companion object {
        fun capsule(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
        ) = Background(fill, stroke, strokeWidth, corners = null)
        fun rectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
        ) = Background(fill, stroke, strokeWidth, corners = null)
        fun roundedRectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
            corners: CornerRadii
        ) = Background(fill, stroke, strokeWidth, corners = corners)
        fun roundedRectangle(
            fill: Paint,
            stroke: Color,
            strokeWidth: Dimension,
            cornerRadius: Dimension
        ) = Background(fill, stroke, strokeWidth, corners = CornerRadii(cornerRadius))
    }
}

data class CornerRadii(
    val topLeft: Dimension = 0.px,
    val topRight: Dimension = 0.px,
    val bottomLeft: Dimension = 0.px,
    val bottomRight: Dimension = 0.px
) {
    constructor(all: Dimension) : this(all, all, all, all)
}
