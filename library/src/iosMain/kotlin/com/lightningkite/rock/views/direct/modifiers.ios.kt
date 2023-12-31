@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.*
import kotlinx.cinterop.*


//@Suppress("ACTUAL_WITHOUT_EXPECT")
//actual typealias NLocalTimeField = UIDatePicker
//
//@ViewDsl
//actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit): Unit = stack {
//    element(UIDatePicker()){
//        setPreferredDatePickerStyle(UIDatePickerStyle.UIDatePickerStyleCompact)
////        handleTheme(this) { this. = it.foreground.closestColor().toUiColor() }
//        datePickerMode = UIDatePickerMode.UIDatePickerModeTime
//    }
//}
//
//actual var LocalTimeField.action: Action?
//    get() = TODO()
//    set(value) {}
//actual val LocalTimeField.content: Writable<LocalTime?> get() = object: Writable<LocalTime?> {
//    override suspend fun set(value: LocalTime?) {
//        native.date = value?.atDate(1970, 1, 1)?.toNSDateComponents()?.date() ?: NSDate()
//    }
//
//    override suspend fun awaitRaw(): LocalTime? = native.date.toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).time
//
//    override fun addListener(listener: () -> Unit): () -> Unit {
//        return native.onEvent(UIControlEventValueChanged, listener)
//    }
//
//}
//actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
//    get() = TODO()
//    set(value) {
//    }

//@Suppress("ACTUAL_WITHOUT_EXPECT")
//actual typealias NAutoCompleteTextField = UIView
//
//@ViewDsl
//actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit): Unit =
//    todo("autoCompleteTextField")
//
//actual val AutoCompleteTextField.content: Writable<String> get() = Property("")
//actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
//    get() = TODO()
//    set(value) {}
//actual var AutoCompleteTextField.action: Action?
//    get() = TODO()
//    set(value) {}
//actual inline var AutoCompleteTextField.suggestions: List<String>
//    get() = TODO()
//    set(value) {}

@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit
): ViewWrapper {
    // TODO
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    val parent = this.currentView
    this.beforeNextElementSetup {
        this.extensionWeight = amount
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        extensionHorizontalAlign = horizontal
        extensionVerticalAlign = vertical
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        wrapNext(ScrollLayout()) {
            handleTheme(this, viewDraws = false)
            horizontal = false
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        wrapNext(ScrollLayout()) {
            handleTheme(this, viewDraws = false)
            horizontal = true
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {
        extensionSizeConstraints = constraints
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.marginless: ViewWrapper
    get() {
        beforeNextElementSetup {
            extensionMarginless = true
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper
    get() {
        beforeNextElementSetup {
//        extensionPadding
        }
        return ViewWrapper
    }
// End