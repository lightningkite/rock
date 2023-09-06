package com.lightningkite.mppexample

fun convertToVector(input: String): ImageVector {
    val viewBox = Regex("viewBox=\"([^\"]*)\"").find(input)?.groupValues?.get(1)?.split(" ")?.map { it.toInt() }
    val width = Regex("width=\"([^\"]*)\"").find(input)?.groupValues?.get(1)?.toInt()
    val height = Regex("height=\"([^\"]*)\"").find(input)?.groupValues?.get(1)?.toInt()
    val fill = Regex("fill=\"([^\"]*)\"").find(input)?.groupValues?.get(1)
    val fillColor = if (fill == null || fill == "none") null else Color.fromHexString(fill)
    val stroke = Regex("stroke=\"([^\"]*)\"").find(input)?.groupValues?.get(1)
    val strokeColor = if (stroke == null || stroke == "none") null else Color.fromHexString(stroke)
    val strokeWidth = Regex("stroke-width=\"([^\"]*)\"").find(input)?.groupValues?.get(1)?.toDouble()
    val paths = Regex("<path[^>]*d=\"([^\"]*)\"[^>]*>").findAll(input).map { it.groupValues[1] }.toList()
    return ImageVector(
        width = width?.px ?: 24.px,
        height = height?.px ?: 24.px,
        viewBoxMinX = viewBox?.get(0) ?: 0,
        viewBoxMinY = viewBox?.get(1) ?: 0,
        viewBoxWidth = viewBox?.get(2) ?: 24,
        viewBoxHeight = viewBox?.get(3) ?: 24,
        paths = paths.map { path ->
            ImageVector.Path(
                path = path,
                fillColor = fillColor,
                strokeColor = strokeColor,
                strokeWidth = strokeWidth,
            )
        }
    )
}

fun ImageVector.toKotlin(): String {
    return ""
//    return """
//        ImageVector(
//            width = ${width.value},
//            height = ${height.value},
//            viewBoxMinX = ${viewBoxMinX},
//            viewBoxMinY = ${viewBoxMinY},
//            viewBoxWidth = ${viewBoxWidth},
//            viewBoxHeight = ${viewBoxHeight},
//            paths = listOf(
//                ${paths.joinToString(",\n") { path ->
//        """
//                    ImageVector.Path(
//                        path = "${path.path}",
//                        fillColor = ${path.fillColor?.toKotlin()},
//                        strokeColor = ${path.strokeColor?.toKotlin()},
//                        strokeWidth = ${path.strokeWidth},
//                    )
//        """.trimIndent()
//    }}
//            )
//        )
//    """.trimIndent()
}
