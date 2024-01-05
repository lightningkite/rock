@file:OptIn(ExperimentalForeignApi::class)

package com.lightningkite.rock.views.direct

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.canvas.DrawingContext2D
import com.lightningkite.rock.views.l2.icon
import kotlinx.cinterop.*
import kotlinx.datetime.*
import platform.Foundation.NSDate
import platform.Foundation.NSIndexPath
import platform.UIKit.*
import platform.darwin.NSInteger
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import platform.CoreGraphics.*
import platform.CoreImage.provideImageData
import platform.Foundation.NSBundle
import platform.Foundation.NSCoder
import platform.darwin.NSObject
import platform.objc.object_getClass
import platform.objc.sel_registerName
import kotlin.experimental.ExperimentalObjCName


@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSeparator = UIView

@ViewDsl
actual fun ViewWriter.separator(setup: Separator.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        backgroundColor = it.foreground.closestColor().toUiColor()
        alpha = 0.25
    }
    extensionSizeConstraints = SizeConstraints(minWidth = 1.px, minHeight = 1.px)
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContainingView = UIView

@ViewDsl
actual fun ViewWriter.stack(setup: ContainingView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.col(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = false
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@ViewDsl
actual fun ViewWriter.row(setup: ContainingView.() -> Unit): Unit = element(LinearLayout()) {
    horizontal = true
    handleTheme(this, viewDraws = false)
    setup(ContainingView(this))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLink = NativeLink

@ViewDsl
actual fun ViewWriter.link(setup: Link.() -> Unit): Unit = element(NativeLink()) {
    handleThemeControl(this) {
        setup(Link(this))
        onNavigator = navigator
    }
}

actual inline var Link.to: RockScreen
    get() = native.toScreen ?: RockScreen.Empty
    set(value) {
        native.toScreen = value
    }
actual inline var Link.newTab: Boolean
    get() = native.newTab
    set(value) {
        native.newTab = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = NativeLink

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit): Unit = element(NativeLink()) {
    handleThemeControl(this) {
        setup(ExternalLink(this))
    }
}

actual inline var ExternalLink.to: String
    get() = native.toUrl ?: ""
    set(value) {
        native.toUrl = value
    }
actual inline var ExternalLink.newTab: Boolean
    get() = native.newTab
    set(value) {
        native.newTab = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NImage = UIImageView

@ViewDsl
actual fun ViewWriter.image(setup: Image.() -> Unit): Unit = element(NImage()) {
    handleTheme(this, viewDraws = true)
    this.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
    setup(Image(this))
}

actual inline var Image.source: ImageSource
    get() = TODO()
    set(value) {
        when (value) {
            is ImageRaw -> {
                native.image = UIImage(data = value.data.toNSData())
                native.informParentOfSizeChange()
            }

            is ImageRemote -> {
                launch {
                    native.image = UIImage(data = fetch(value.url).blob().data)
                    native.informParentOfSizeChange()
                }
            }

            is ImageResource -> {
                native.image = UIImage.imageNamed(value.name)
                native.informParentOfSizeChange()
            }

            is ImageVector -> {
                native.image = value.render()
                native.informParentOfSizeChange()
            }

            else -> {}
        }
    }
actual inline var Image.scaleType: ImageScaleType
    get() = TODO()
    set(value) {
        when (value) {
            ImageScaleType.Fit -> native.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            ImageScaleType.Crop -> native.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
            ImageScaleType.Stretch -> native.contentMode = UIViewContentMode.UIViewContentModeScaleToFill
            ImageScaleType.NoScale -> native.contentMode = UIViewContentMode.UIViewContentModeCenter
        }
    }
actual inline var Image.description: String?
    get() = TODO()
    set(value) {
        native.accessibilityLabel = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextView = UILabel

@ViewDsl
actual fun ViewWriter.h1(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 2)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h2(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.6)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h3(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.4)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h4(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.3)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h5(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.2)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.h6(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.1)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.title
        it.title.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.text(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 1.0)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.body
        it.body.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

@ViewDsl
actual fun ViewWriter.subtext(setup: TextView.() -> Unit): Unit = element(UILabel()) {
    font = UIFont.systemFontOfSize(UIFont.systemFontSize * 0.8)
    handleTheme(this) {
        this.textColor = it.foreground.closestColor().toUiColor()
        this.extensionFontAndStyle = it.body
        it.body.let { this.font = it.font.get(font.pointSize, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic) }
    }
    setup(TextView(this))
}

actual inline var TextView.content: String
    get() = native.text ?: ""
    set(value) {
        native.text = value
        native.informParentOfSizeChange()
    }
actual inline var TextView.align: Align
    get() = when (native.textAlignment) {
        NSTextAlignmentLeft -> Align.Start
        NSTextAlignmentCenter -> Align.Center
        NSTextAlignmentRight -> Align.End
        NSTextAlignmentJustified -> Align.Stretch
        else -> Align.Start
    }
    set(value) {
        native.contentMode = when (value) {
            Align.Start -> UIViewContentMode.UIViewContentModeLeft
            Align.Center -> UIViewContentMode.UIViewContentModeCenter
            Align.End -> UIViewContentMode.UIViewContentModeRight
            Align.Stretch -> UIViewContentMode.UIViewContentModeScaleAspectFit
        }
        native.textAlignment = when (value) {
            Align.Start -> NSTextAlignmentLeft
            Align.Center -> NSTextAlignmentCenter
            Align.End -> NSTextAlignmentRight
            Align.Stretch -> NSTextAlignmentJustified
        }
    }
actual inline var TextView.textSize: Dimension
    get() = Dimension(native.font.pointSize)
    set(value) {
        native.extensionFontAndStyle?.let {
            native.font = it.font.get(value.value, if (it.bold) UIFontWeightBold else UIFontWeightRegular, it.italic)
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = UIView

@ViewDsl
actual fun ViewWriter.label(setup: Label.() -> Unit): Unit = col {
    subtext {  }
    setup(Label(native))
}
actual inline var Label.content: String
    get() = (native.subviews[0] as UILabel).text ?: ""
    set(value) { (native.subviews[0] as UILabel).text = value }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NActivityIndicator = UIActivityIndicatorView

@ViewDsl
actual fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit): Unit =
    element(UIActivityIndicatorView()) {
        hidden = false
        startAnimating()
        handleTheme(this) {
            this.color = it.foreground.closestColor().toUiColor()
        }
        extensionSizeConstraints = SizeConstraints(minWidth = 2.rem, minHeight = 2.rem)
        setup(ActivityIndicator(this))
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = UIView

@ViewDsl
actual fun ViewWriter.space(setup: Space.() -> Unit): Unit = element(UIView()) {
    setup(Space(this))
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        extensionSizeConstraints = SizeConstraints(minHeight = it.spacing * multiplier)
    }
    setup(Space(this))
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NDismissBackground = UIView

@ViewDsl
actual fun ViewWriter.dismissBackground(setup: DismissBackground.() -> Unit): Unit = element(UIView()) {
    handleTheme(this) {
        backgroundColor = it.background.closestColor().copy(alpha = 0.5f).toUiColor()
    }
    setup(DismissBackground(this))
}

@OptIn(ExperimentalForeignApi::class)
actual fun DismissBackground.onClick(action: suspend () -> Unit): Unit {
    val actionHolder = object : NSObject() {
        @ObjCAction
        fun eventHandler() = launch(action)
    }
    val rec = UITapGestureRecognizer(actionHolder, sel_registerName("eventHandler"))
    native.addGestureRecognizer(rec)
    calculationContext.onRemove {
        // Retain the sleeve until disposed
        rec.enabled
        actionHolder.description
    }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NButton = FrameLayoutButton

@ViewDsl
actual fun ViewWriter.button(setup: Button.() -> Unit): Unit = element(FrameLayoutButton()) {
    handleThemeControl(this) {
        setup(Button(this))
    }
}

actual fun Button.onClick(action: suspend () -> Unit): Unit {
    native.onEvent(UIControlEventTouchUpInside) { launch(action) }
}

actual inline var Button.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCheckbox = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.checkbox(setup: Checkbox.() -> Unit): Unit {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    toggleButton {
        icon(Icon.done, "") {
            ::visible { checked.await() }
        } in marginless
        setup(Checkbox(this.native))
    }
}
actual inline var Checkbox.enabled: Boolean
    get() = native.enabled
    set(value) { native.enabled = value }
actual val Checkbox.checked: Writable<Boolean> get() =  native.checkedWritable

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit): Unit {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    radioToggleButton {
        icon(Icon.done, "") {
            ::visible { checked.await() }
        } in marginless
        setup(RadioButton(this.native))
    }
}
actual inline var RadioButton.enabled: Boolean
    get() = native.enabled
    set(value) { native.enabled = value }
actual val RadioButton.checked: Writable<Boolean> get() =  native.checkedWritable

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwitch = UISwitch

@ViewDsl
actual fun ViewWriter.switch(setup: Switch.() -> Unit): Unit = element(UISwitch()) {
    handleTheme(this) {

    }
    setup(Switch(this))
}

actual inline var Switch.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }
actual val Switch.checked: Writable<Boolean>
    get() {
        return object : Writable<Boolean> {
            override suspend fun awaitRaw(): Boolean = native.on
            override fun addListener(listener: () -> Unit): () -> Unit {
                return native.onEvent(UIControlEventValueChanged) { listener() }
            }

            override suspend fun set(value: Boolean) {
                native.on = value
            }
        }
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NToggleButton = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.toggleButton(setup: ToggleButton.() -> Unit): Unit = element(FrameLayoutToggleButton()) {
    handleThemeControl(this, { checkedWritable.await() }) {
        setup(ToggleButton(this))
    }
}

actual inline var ToggleButton.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }
actual val ToggleButton.checked: Writable<Boolean> get() = native.checkedWritable

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioToggleButton = FrameLayoutToggleButton

@ViewDsl
actual fun ViewWriter.radioToggleButton(setup: RadioToggleButton.() -> Unit): Unit =
    element(FrameLayoutToggleButton()) {
        handleThemeControl(this, { checkedWritable.await() }) {
            allowUnselect = false
            setup(RadioToggleButton(this))
        }
    }

actual inline var RadioToggleButton.enabled: Boolean
    get() = native.enabled
    set(value) {
        native.enabled = value
    }
actual val RadioToggleButton.checked: Writable<Boolean> get() = native.checkedWritable

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = TextFieldInput

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit): Unit = stack {
    element(TextFieldInput()) {
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        val p = Property<LocalDate?>(null)
        currentValue = p
        inputView = UIDatePicker().apply {
            setPreferredDatePickerStyle(UIDatePickerStyle.UIDatePickerStyleInline)
            datePickerMode = UIDatePickerMode.UIDatePickerModeDate
            date = p.value?.toNSDateComponents()?.date() ?: NSDate()
            this@element.calculationContext.onRemove(onEventNoRemove(UIControlEventValueChanged) {
                p.value = this.date.toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).date
            })
        }
        setup(LocalDateField(this))
        reactiveScope { text = p.await()?.toString() ?: "Pick one" }
    }
}

actual var LocalDateField.action: Action?
    get() = native.action
    set(value) {
        native.action = value
    }
actual val LocalDateField.content: Writable<LocalDate?>
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.currentValue as Property<LocalDate?>
    }
actual inline var LocalDateField.range: ClosedRange<LocalDate>?
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.valueRange as ClosedRange<LocalDate>
    }
    set(value) {
        native.valueRange = value
    }

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

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = TextFieldInput

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit): Unit = stack {
    element(TextFieldInput()) {
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        val p = Property<LocalTime?>(null)
        currentValue = p
        inputView = UIDatePicker().apply {
            setPreferredDatePickerStyle(UIDatePickerStyle.UIDatePickerStyleWheels)
            datePickerMode = UIDatePickerMode.UIDatePickerModeTime
            date = p.value?.atDate(1970, 1, 1)?.toNSDateComponents()?.date() ?: NSDate()
            this@element.calculationContext.onRemove(onEventNoRemove(UIControlEventValueChanged) {
                p.value = this.date.toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault()).time
            })
        }
        setup(LocalTimeField(this))
        reactiveScope { text = p.await()?.toString() ?: "Pick one" }
    }
}

actual var LocalTimeField.action: Action?
    get() = native.action
    set(value) {
        native.action = value
    }
actual val LocalTimeField.content: Writable<LocalTime?>
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.currentValue as Property<LocalTime?>
    }
actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.valueRange as ClosedRange<LocalTime>
    }
    set(value) {
        native.valueRange = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = TextFieldInput

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit): Unit = stack {
    element(TextFieldInput()) {
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        val p = Property<LocalDateTime?>(null)
        currentValue = p
        inputView = UIDatePicker().apply {
            setPreferredDatePickerStyle(UIDatePickerStyle.UIDatePickerStyleWheels)
            datePickerMode = UIDatePickerMode.UIDatePickerModeDateAndTime
            date = p.value?.toNSDateComponents()?.date() ?: NSDate()
            this@element.calculationContext.onRemove(onEventNoRemove(UIControlEventValueChanged) {
                p.value = this.date.toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault())
            })
        }
        setup(LocalDateTimeField(this))
        reactiveScope { text = p.await()?.toString() ?: "Pick one" }
    }
}

actual var LocalDateTimeField.action: Action?
    get() = native.action
    set(value) {
        native.action = value
    }
actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.currentValue as Property<LocalDateTime?>
    }
actual inline var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() {
        @Suppress("UNCHECKED_CAST")
        return native.valueRange as ClosedRange<LocalDateTime>
    }
    set(value) {
        native.valueRange = value
    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextField = UITextField

@ViewDsl
actual fun ViewWriter.textField(setup: TextField.() -> Unit): Unit = stack {
    element(UITextField()) {
        smartDashesType = UITextSmartDashesType.UITextSmartDashesTypeNo
        smartQuotesType = UITextSmartQuotesType.UITextSmartQuotesTypeNo
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        calculationContext.onRemove {
            extensionStrongRef = null
        }
        setup(TextField(this))
    }
}

actual val TextField.content: Writable<String>
    get() = object : Writable<String> {
        override suspend fun awaitRaw(): String = native.text ?: ""
        override fun addListener(listener: () -> Unit): () -> Unit {
            return native.onEvent(UIControlEventEditingChanged) {
                listener()
            }
        }

        override suspend fun set(value: String) {
            native.text = value
        }
    }
actual inline var TextField.keyboardHints: KeyboardHints
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
actual var TextField.action: Action?
    get() = TODO()
    set(value) {
        native.delegate = value?.let {
            val d = object : NSObject(), UITextFieldDelegateProtocol {
                override fun textFieldShouldReturn(textField: UITextField): Boolean {
                    launch { it.onSelect() }
                    return true
                }
            }
            native.extensionStrongRef = d
            d
        } ?: NextFocusDelegateShared
        native.returnKeyType = when (value?.title) {
            "Emergency Call" -> UIReturnKeyType.UIReturnKeyEmergencyCall
            "Go" -> UIReturnKeyType.UIReturnKeyGo
            "Next" -> UIReturnKeyType.UIReturnKeyNext
            "Continue" -> UIReturnKeyType.UIReturnKeyContinue
            "Default" -> UIReturnKeyType.UIReturnKeyDefault
            "Join" -> UIReturnKeyType.UIReturnKeyJoin
            "Done" -> UIReturnKeyType.UIReturnKeyDone
            "Yahoo" -> UIReturnKeyType.UIReturnKeyYahoo
            "Send" -> UIReturnKeyType.UIReturnKeySend
            "Google" -> UIReturnKeyType.UIReturnKeyGoogle
            "Route" -> UIReturnKeyType.UIReturnKeyRoute
            "Search" -> UIReturnKeyType.UIReturnKeySearch
            else -> UIReturnKeyType.UIReturnKeyDone
        }
    }
actual inline var TextField.hint: String
    get() = native.placeholder ?: ""
    set(value) {
        native.placeholder = value
    }
actual inline var TextField.range: ClosedRange<Double>?
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTextArea = UITextView

@ViewDsl
actual fun ViewWriter.textArea(setup: TextArea.() -> Unit): Unit = stack {
    element(UITextView()) {
        smartDashesType = UITextSmartDashesType.UITextSmartDashesTypeNo
        smartQuotesType = UITextSmartQuotesType.UITextSmartQuotesTypeNo
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
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

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSelect = TextFieldInput

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit): Unit = stack {
    element(TextFieldInput()) {
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        inputView = UIPickerView().apply {
        }
        setup(Select(this))
    }
}
actual fun <T> Select.bind(
    edits: Writable<T>,
    data: Readable<List<T>>,
    render: (T) -> String
) {
    val picker = (native.inputView as UIPickerView)
    val source = object: NSObject(), UIPickerViewDataSourceProtocol, UIPickerViewDelegateProtocol {
        var list: List<T> = listOf()

        init {
            reactiveScope {
                list = data.await()
                picker.reloadAllComponents()
            }
            reactiveScope { native.text = render(edits.await()) }
        }

        override fun numberOfComponentsInPickerView(pickerView: UIPickerView): NSInteger = 1L
        @Suppress("CONFLICTING_OVERLOADS", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun pickerView(pickerView: UIPickerView, numberOfRowsInComponent: NSInteger): NSInteger = list.size.toLong()
        @Suppress("CONFLICTING_OVERLOADS", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun pickerView(pickerView: UIPickerView, titleForRow: NSInteger, forComponent: NSInteger): String? {
            return render(list[titleForRow.toInt()])
        }
        @Suppress("CONFLICTING_OVERLOADS", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun pickerView(pickerView: UIPickerView, didSelectRow: NSInteger, inComponent: NSInteger) {
            launch {
                val item = list[didSelectRow.toInt()]
                edits set item
            }
        }
    }
    picker.setDataSource(source)
    picker.setDelegate(source)
    picker.extensionStrongRef = source
}

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

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NAutoCompleteTextField = UITextField

@ViewDsl
actual fun ViewWriter.autoCompleteTextField(setup: AutoCompleteTextField.() -> Unit): Unit = stack {
    element(UITextField()) {
        smartDashesType = UITextSmartDashesType.UITextSmartDashesTypeNo
        smartQuotesType = UITextSmartQuotesType.UITextSmartQuotesTypeNo
        handleTheme(this) { textColor = it.foreground.closestColor().toUiColor() }
        calculationContext.onRemove {
            extensionStrongRef = null
        }
        setup(AutoCompleteTextField(this))
    }
}

actual val AutoCompleteTextField.content: Writable<String>
    get() = object : Writable<String> {
        override suspend fun awaitRaw(): String = native.text ?: ""
        override fun addListener(listener: () -> Unit): () -> Unit {
            return native.onEvent(UIControlEventEditingChanged) {
                listener()
            }
        }

        override suspend fun set(value: String) {
            native.text = value
        }
    }
actual inline var AutoCompleteTextField.keyboardHints: KeyboardHints
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
actual var AutoCompleteTextField.action: Action?
    get() = TODO()
    set(value) {
        native.delegate = action?.let {
            val d = object : NSObject(), UITextFieldDelegateProtocol {
                override fun textFieldShouldReturn(textField: UITextField): Boolean {
                    launch { it.onSelect() }
                    return true
                }
            }
            native.extensionStrongRef = d
            d
        } ?: NextFocusDelegateShared
        native.returnKeyType = when (action?.title) {
            "Emergency Call" -> UIReturnKeyType.UIReturnKeyEmergencyCall
            "Go" -> UIReturnKeyType.UIReturnKeyGo
            "Next" -> UIReturnKeyType.UIReturnKeyNext
            "Continue" -> UIReturnKeyType.UIReturnKeyContinue
            "Default" -> UIReturnKeyType.UIReturnKeyDefault
            "Join" -> UIReturnKeyType.UIReturnKeyJoin
            "Done" -> UIReturnKeyType.UIReturnKeyDone
            "Yahoo" -> UIReturnKeyType.UIReturnKeyYahoo
            "Send" -> UIReturnKeyType.UIReturnKeySend
            "Google" -> UIReturnKeyType.UIReturnKeyGoogle
            "Route" -> UIReturnKeyType.UIReturnKeyRoute
            "Search" -> UIReturnKeyType.UIReturnKeySearch
            else -> UIReturnKeyType.UIReturnKeyDone
        }
    }
//actual inline var AutoCompleteTextField.hint: String
//    get() = native.placeholder ?: ""
//    set(value) {
//        native.placeholder = value
//    }
actual inline var AutoCompleteTextField.suggestions: List<String>
    get() = TODO()
    set(value) {

    }

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSwapView = FrameLayout

@ViewDsl
actual fun ViewWriter.swapView(setup: SwapView.() -> Unit) = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    setup(SwapView(this))
}

@ViewDsl
actual fun ViewWriter.swapViewDialog(setup: SwapView.() -> Unit): Unit = element(FrameLayout()) {
    handleTheme(this, viewDraws = false)
    hidden = true
    setup(SwapView(this))
}

actual fun SwapView.swap(transition: ScreenTransition, createNewView: () -> Unit): Unit {
    native.clearChildren()
    createNewView()
    native.hidden = native.subviews.all { (it as UIView).hidden }
}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NWebView = UIView

@ViewDsl
actual fun ViewWriter.webView(setup: WebView.() -> Unit): Unit = todo("webView")
actual inline var WebView.url: String
    get() = TODO()
    set(value) {}
actual inline var WebView.permitJs: Boolean
    get() = TODO()
    set(value) {}
actual inline var WebView.content: String
    get() = TODO()
    set(value) {}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NCanvas = CanvasView

@ViewDsl
actual fun ViewWriter.canvas(setup: Canvas.() -> Unit): Unit = element(CanvasView()) {
    setup(Canvas(this))
}
actual fun Canvas.redraw(action: DrawingContext2D.() -> Unit): Unit {
    this.native.draw = action
    this.native.setNeedsDisplay()
}
actual val Canvas.width: Readable<Double> get() = Property(this.native.bounds.useContents { size.width })
actual val Canvas.height: Readable<Double> get() = Property(this.native.bounds.useContents { size.height })
actual fun Canvas.onPointerDown(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerDown.add(action)
}
actual fun Canvas.onPointerMove(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerMove.add(action)
}
actual fun Canvas.onPointerCancel(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerCancel.add(action)
}
actual fun Canvas.onPointerUp(action: (id: Int, x: Double, y: Double, width: Double, height: Double) -> Unit): Unit {
    native.onPointerUp.add(action)
}
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = UICollectionView

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit = element(UICollectionView(CGRectMake(0.0, 0.0, 0.0, 0.0), run {
    val size = NSCollectionLayoutSize.sizeWithWidthDimension(
        width = NSCollectionLayoutDimension.fractionalWidthDimension(1.0),
        heightDimension = NSCollectionLayoutDimension.estimatedDimension(50.0),
    )
    UICollectionViewCompositionalLayout(
        NSCollectionLayoutSection.sectionWithGroup(
            NSCollectionLayoutGroup.horizontalGroupWithLayoutSize(
                layoutSize = size,
                subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                    layoutSize = size
                ),
                count = 1,
            )
        )
    )
})) {
    calculationContext.onRemove {
        extensionStrongRef = null
    }
    backgroundColor = UIColor.clearColor
    extensionViewWriter = newViews()
    handleTheme(this, viewDraws = false)
    setup(RecyclerView(this))
}

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit = element(UICollectionView(CGRectMake(0.0, 0.0, 0.0, 0.0), run {
    val size = NSCollectionLayoutSize.sizeWithWidthDimension(
        width = NSCollectionLayoutDimension.estimatedDimension(50.0),
        heightDimension = NSCollectionLayoutDimension.fractionalHeightDimension(1.0),
    )
    UICollectionViewCompositionalLayout(
        NSCollectionLayoutSection.sectionWithGroup(
            NSCollectionLayoutGroup.verticalGroupWithLayoutSize(
                layoutSize = size,
                subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                    layoutSize = size
                ),
                count = 1,
            )
        )
    ).apply {
        configuration.scrollDirection = UICollectionViewScrollDirection.UICollectionViewScrollDirectionHorizontal
    }
})) {
    backgroundColor = UIColor.clearColor
    extensionViewWriter = newViews()
    handleTheme(this, viewDraws = false)
    setup(RecyclerView(this))
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit = recyclerView(setup)
actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
    }

@OptIn(ExperimentalObjCName::class, BetaInteropApi::class)
@ExportObjCClass
class ObsUICollectionViewCell<T>: UICollectionViewCell, UIViewWithSizeOverridesProtocol {
    constructor():this(CGRectMake(0.0, 0.0, 0.0, 0.0))
    @OverrideInit constructor(frame: CValue<CGRect>):super(frame = frame)
    @OverrideInit constructor(coder: NSCoder):super(coder = coder)
    val data = LateInitProperty<T>()
    var ready = false
    override fun subviewDidChangeSizing(view: UIView?) {
        frameLayoutSubviewDidChangeSizing(view)
        generateSequence(this as UIView) { it.superview }.filterIsInstance<UICollectionView>().firstOrNull()?.collectionViewLayout?.invalidateLayout()
    }
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }
    @OptIn(ExperimentalForeignApi::class)
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size)
    override fun layoutSubviews() = frameLayoutLayoutSubviews()
    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
    val altCellRef = HashSet<UICollectionViewCell>()
    calculationContext.onRemove {
        altCellRef.forEach { it.shutdown() }
        altCellRef.clear()
    }
    @Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES", "RETURN_TYPE_MISMATCH_ON_INHERITANCE", "MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED")
    val source = object: NSObject(), UICollectionViewDelegateProtocol, UICollectionViewDataSourceProtocol {
        var list: List<T> = listOf()

        init {
            reactiveScope {
                list = items.await()
                native.reloadData()
            }
        }
        val registered = HashSet<String>()

        @Suppress("CONFLICTING_OVERLOADS", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            if (registered.add("main")) {
                collectionView.registerClass(object_getClass(ObsUICollectionViewCell<T>())!!, "main")
            }
            @Suppress("UNCHECKED_CAST") val cell = collectionView.dequeueReusableCellWithReuseIdentifier("main", cellForItemAtIndexPath) as ObsUICollectionViewCell<T>
            if(altCellRef.add(cell)) collectionView.calculationContext.onRemove { cell.shutdown() }
//                ?: run {
//                val vw = native.extensionViewWriter ?: throw IllegalStateException("No view writer attached")
//                vw!!.element(ObsUICollectionViewCell<T>()) {
//                    render(vw, data)
//                }
//                vw.rootCreated as? ObsUICollectionViewCell<T> ?: throw IllegalStateException("No view created")
//            }
            list.getOrNull(cellForItemAtIndexPath.row.toInt())?.let {
                cell.data.value = it
            }
            if(!cell.ready) {
                val vw = native.extensionViewWriter ?: throw IllegalStateException("No view writer attached")
                render(vw.targeting(cell), cell.data)
                cell.ready = true
            }
            return cell
        }

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = list.size.toLong()
    }
    native.setDataSource(source)
    native.setDelegate(source)
    native.extensionStrongRef = source
}

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