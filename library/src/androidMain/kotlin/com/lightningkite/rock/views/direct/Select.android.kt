package com.lightningkite.rock.views.direct

import androidx.appcompat.widget.AppCompatSpinner
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = AppCompatSpinner

actual fun <T> Select.bind(
    edits: Writable<T>,
    data: Readable<List<T>>,
    render: (T) -> String
) {
}

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit) {
    return viewElement(factory = ::AppCompatSpinner, wrapper = ::Select, setup = setup)
}