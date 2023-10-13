package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.reactive.Readable

class ListRenderer<T>(
    val render: ViewContext.(property: Readable<T>)->Unit
) {
    var data: List<T> = listOf()
}