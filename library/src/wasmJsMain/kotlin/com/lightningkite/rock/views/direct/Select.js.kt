package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.WidgetOption
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.NView2
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.launch
import com.lightningkite.rock.views.reactiveScope
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
            list.indexOf(edits.awaitRaw()).toString()
        )
    }
    native.js.onchange = {
        launch {
            native.js.value.toIntOrNull()?.let { edits set list[it] }
        }
    }
}