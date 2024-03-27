package com.lightningkite.kiteui.views.direct

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView as AndroidAutocompleteTextView
import com.lightningkite.kiteui.models.Action
import com.lightningkite.kiteui.models.KeyboardHints
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.AndroidAppContext
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = AndroidAutocompleteTextView

actual val AutoCompleteTextField.content: Writable<String>
    get() {
        return native.content
    }
actual var AutoCompleteTextField.keyboardHints: KeyboardHints
    get() {
        return native.keyboardHints
    }
    set(value) {
        native.keyboardHints = value
    }
actual var AutoCompleteTextField.action: Action?
    get() {
        return native.tag as? Action
    }
    set(value) {
        native.tag = value
    }

private class KiteUiStringAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    val items: List<String> = objects
}

actual var AutoCompleteTextField.suggestions: List<String>
    get() {
        return (native.adapter as KiteUiStringAdapter).items
    }
    set(value) {
        native.setAdapter(KiteUiStringAdapter(native.context, AndroidAppContext.autoCompleteLayoutResource, value))
    }

@ViewDsl
actual inline fun ViewWriter.autoCompleteTextFieldActual(crossinline setup: AutoCompleteTextField.() -> Unit) {
    return viewElement(factory = ::AndroidAutocompleteTextView, wrapper = ::AutoCompleteTextField, setup = setup)
}