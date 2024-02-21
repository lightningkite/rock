package com.lightningkite.rock.views.direct

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView as AndroidAutocompleteTextView
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.AndroidAppContext
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

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

private class RockStringAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    val items: List<String> = objects
}

actual var AutoCompleteTextField.suggestions: List<String>
    get() {
        return (native.adapter as RockStringAdapter).items
    }
    set(value) {
        native.setAdapter(RockStringAdapter(native.context, AndroidAppContext.autoCompleteLayoutResource, value))
    }

@ViewDsl
actual fun ViewWriter.autoCompleteTextFieldActual(setup: AutoCompleteTextField.() -> Unit) {
    return viewElement(factory = ::AndroidAutocompleteTextView, wrapper = ::AutoCompleteTextField, setup = setup)
}