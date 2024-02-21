package com.lightningkite.rock.views.direct

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

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
actual fun ViewWriter.labelActual(setup: Label.() -> Unit) {
    col {
        subtext {  }
        setup(Label(this.native))
    }
}