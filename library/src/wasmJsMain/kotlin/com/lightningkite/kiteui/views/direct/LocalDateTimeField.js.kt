package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Action
import com.lightningkite.kiteui.reactive.Writable
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.NView2
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.launch
import kotlinx.datetime.*
import org.w3c.dom.HTMLInputElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NLocalDateField(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.localDateFieldActual(crossinline setup: LocalDateField.() -> Unit): Unit =
    themedElementEditable("input", ::NLocalDateField) {
        js.type = "date"
        setup(LocalDateField(this))
    }

actual val LocalDateField.content: Writable<LocalDate?>
    get() = native.js.vprop(
        eventName = "input",
        get = {
            TODO()
//            (native.js.valueAsDate as? Date)
//                ?.toKotlinInstant()
//                ?.toLocalDateTime(TimeZone.UTC)
//                ?.date
        },
        set = {
//            native.js.valueAsDate = it?.let { LocalDateTime(it, LocalTime(12, 0, 0)).toInstant(TimeZone.UTC).toJSDate() }
        }
    )
actual var LocalDateField.action: Action?
    get() = TODO()
    set(value) {
        native.js.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalDateField.range: ClosedRange<LocalDate>?
    get() = TODO()
    set(value) {
        value?.let {
            native.js.min = it.start.toString()
            native.js.max = it.endInclusive.toString()
        } ?: run {
            native.js.removeAttribute("min")
            native.js.removeAttribute("max")
        }
    }
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NLocalTimeField(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.localTimeFieldActual(crossinline setup: LocalTimeField.() -> Unit): Unit =
    themedElementEditable("input", ::NLocalTimeField) {
        js.type = "time"
        setup(LocalTimeField(this))
    }

actual val LocalTimeField.content: Writable<LocalTime?>
    get() = native.js.vprop(
        "input",
        {
        TODO()
//            (native.js.valueAsDate as? Date)?.toKotlinInstant()?.toLocalDateTime(
//                TimeZone.UTC
//            )?.time
        },
        {
//            valueAsDate =
//                it?.let { LocalDateTime(LocalDate(1970, 1, 1), it).toInstant(TimeZone.UTC).toJSDate() }
        }
    )
actual var LocalTimeField.action: Action?
    get() = TODO()
    set(value) {
        native.js.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalTimeField.range: ClosedRange<LocalTime>?
    get() = TODO()
    set(value) {
        value?.let {
            native.js.min = it.start.toString().take(5)
            native.js.max = it.endInclusive.toString().take(5)
        } ?: run {
            native.js.removeAttribute("min")
            native.js.removeAttribute("max")
        }
    }
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NLocalDateTimeField(override val js: HTMLInputElement): NView2<HTMLInputElement>()

@ViewDsl
actual inline fun ViewWriter.localDateTimeFieldActual(crossinline setup: LocalDateTimeField.() -> Unit): Unit =
    themedElementEditable("input", ::NLocalDateTimeField) {
        js.type = "datetime-local"
        setup(LocalDateTimeField(this))
    }

actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() = native.js.vprop(
        "input",
        {
        TODO()
//            (native.js.valueAsDate as? Date)?.toKotlinInstant()?.toLocalDateTime(
//                TimeZone.UTC
//            )
        },
        {
//            valueAsDate = it?.let { it.toInstant(TimeZone.UTC).toJSDate() }
        }
    )
actual var LocalDateTimeField.action: Action?
    get() = TODO()
    set(value) {
        native.js.onkeyup = if (value == null) null else { ev ->
            if (ev.keyCode == 13) {
                launch {
                    value.onSelect()
                }
            }
        }
    }
actual inline var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() = TODO()
    set(value) {
        value?.let {
            native.js.min = it.start.toString()
            native.js.max = it.endInclusive.toString()
        } ?: run {
            native.js.removeAttribute("min")
            native.js.removeAttribute("max")
        }
    }