package com.lightningkite.kiteui.views.direct

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = ViewGroup

actual var Label.content: String
    get() {
        return (native.children.first() as TextView).text.toString()
    }
    set(value) {
        (native.children.first() as TextView).text = value
    }

@ViewDsl
actual inline fun ViewWriter.labelActual(crossinline setup: Label.() -> Unit) {
    col {
        subtext {  }
        setup(Label(this.native))
    }
}