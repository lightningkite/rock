package com.lightningkite.rock.views.direct

import android.widget.TextView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

actual typealias NLabel = TextView

actual var Label.content: String
    get() {
        return native.text.toString()
    }
    set(value) {
        native.text = value
    }

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit) {
    viewElement(factory = ::TextView, wrapper = ::Label, setup = setup)
}