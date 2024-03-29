package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.KeyboardCase
import com.lightningkite.rock.models.KeyboardHints
import com.lightningkite.rock.models.KeyboardType
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import platform.UIKit.*
import platform.darwin.NSObject

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = UITextView

@ViewDsl
actual inline fun ViewWriter.textAreaActual(crossinline setup: TextArea.() -> Unit): Unit = stack {
    element(UITextView()) {
        smartDashesType = UITextSmartDashesType.UITextSmartDashesTypeNo
        smartQuotesType = UITextSmartQuotesType.UITextSmartQuotesTypeNo
        handleTheme(this, viewLoads = true) { textColor = it.foreground.closestColor().toUiColor() }
        setup(TextArea(this))
        calculationContext.onRemove {
            extensionStrongRef = null
        }
    }
}

actual val TextArea.content: Writable<String>
    get() = object : Writable<String> {
        override suspend fun awaitRaw(): String = native.text ?: ""
        override fun addListener(listener: () -> Unit): () -> Unit {
            // TODO: Multiple listeners
            native.setDelegate(object : NSObject(), UITextViewDelegateProtocol {
                override fun textViewDidChange(textView: UITextView) {
                    listener()
                }
            })
            return {
                native.setDelegate(null)
            }
        }

        override suspend fun set(value: String) {
            native.text = value
        }
    }
actual inline var TextArea.keyboardHints: KeyboardHints
    get() = TODO()
    set(value) {
        native.autocapitalizationType = when (value.case) {
            KeyboardCase.None -> UITextAutocapitalizationType.UITextAutocapitalizationTypeNone
            KeyboardCase.Letters -> UITextAutocapitalizationType.UITextAutocapitalizationTypeAllCharacters
            KeyboardCase.Words -> UITextAutocapitalizationType.UITextAutocapitalizationTypeWords
            KeyboardCase.Sentences -> UITextAutocapitalizationType.UITextAutocapitalizationTypeSentences
        }
        native.keyboardType = when (value.type) {
            KeyboardType.Text -> UIKeyboardTypeDefault
            KeyboardType.Integer -> UIKeyboardTypeNumberPad
            KeyboardType.Phone -> UIKeyboardTypePhonePad
            KeyboardType.Decimal -> UIKeyboardTypeNumbersAndPunctuation
            KeyboardType.Email -> UIKeyboardTypeEmailAddress
        }
    }
actual inline var TextArea.hint: String
    get() = TODO()
    set(value) {}