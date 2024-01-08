package com.lightningkite.rock.views.direct

import android.widget.EditText
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = EditText

actual val TextArea.content: Writable<String>
    get() {
        return this@content.native.content
    }
actual var TextArea.keyboardHints: KeyboardHints
    get() {
        return native.keyboardHints
    }
    set(value) {
        native.keyboardHints = value
    }
actual var TextArea.hint: String
    get() {
        return this@hint.native.hint.toString()
    }
    set(value) {
        this@hint.native.hint = value
    }

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit) {
    return viewElement(factory = ::EditText, wrapper = ::TextArea, setup = setup)
}