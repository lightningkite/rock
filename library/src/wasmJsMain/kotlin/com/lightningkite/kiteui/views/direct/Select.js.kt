package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.WidgetOption
import com.lightningkite.kiteui.reactive.Readable
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.reactive.awaitOnce
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.launch
import com.lightningkite.kiteui.views.reactiveScope
import org.w3c.dom.HTMLSelectElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSelect(override val js: HTMLSelectElement): NView2<HTMLSelectElement>()

@ViewDsl
actual inline fun ViewWriter.selectActual(crossinline setup: Select.() -> Unit): Unit =
    themedElementEditable("select", ::NSelect) { setup(Select(this)) }

actual fun <T> Select.bind(
    edits: Writable<T>,
    data: Readable<List<T>>,
    render: (T) -> String
) {
    var list: List<T> = listOf()
    reactiveScope {
        list = data.await()
        native.js.__resetContentToOptionList(
            list.mapIndexed { index, t ->
                WidgetOption(index.toString(), render(t))
            },
            list.indexOf(edits.awaitOnce()).toString()
        )
    }
    native.js.onchange = {
        launch {
            native.js.value.toIntOrNull()?.let { edits set list[it] }
        }
    }
}