package com.lightningkite.rock.views.direct

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.lightningkite.rock.locale.renderToString
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.datetime.*
import java.util.WeakHashMap

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = android.view.View
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = android.view.View



private class LocalDateFieldData(
    val property: Property<LocalDate?> = Property(null),
    var min: LocalDate? = null,
    var max: LocalDate? = null
) {
    var range: ClosedRange<LocalDate>?
        get() = min?.let { m -> max?.let { m..it } }
        set(value) {
            if (value == null) {
                min = null
                max = null
            } else {
                min = value.start
                max = value.endInclusive
            }
        }
}

private val LocalDateFieldMoreData = WeakHashMap<LocalDateField, LocalDateFieldData>()


private class LocalTimeFieldData(
    val property: Property<LocalTime?> = Property(null),
    var min: LocalTime? = null,
    var max: LocalTime? = null
) {
    var range: ClosedRange<LocalTime>?
        get() = min?.let { m -> max?.let { m..it } }
        set(value) {
            if (value == null) {
                min = null
                max = null
            } else {
                min = value.start
                max = value.endInclusive
            }
        }
}

private val LocalTimeFieldMoreData = WeakHashMap<LocalTimeField, LocalTimeFieldData>()


private class LocalDateTimeFieldData(
    val property: Property<LocalDateTime?> = Property(null),
    var min: LocalDateTime? = null,
    var max: LocalDateTime? = null
) {
    var range: ClosedRange<LocalDateTime>?
        get() = min?.let { m -> max?.let { m..it } }
        set(value) {
            if (value == null) {
                min = null
                max = null
            } else {
                min = value.start
                max = value.endInclusive
            }
        }
}

private val LocalDateTimeFieldMoreData = WeakHashMap<LocalDateTimeField, LocalDateTimeFieldData>()

actual var LocalDateField.range: ClosedRange<LocalDate>?
    get() = LocalDateFieldMoreData[this]?.range ?: throw IllegalStateException()
    set(value) { LocalDateFieldMoreData[this]?.range = value }
actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() = LocalTimeFieldMoreData[this]?.range ?: throw IllegalStateException()
    set(value) { LocalTimeFieldMoreData[this]?.range = value }
actual var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() = LocalDateTimeFieldMoreData[this]?.range ?: throw IllegalStateException()
    set(value) { LocalDateTimeFieldMoreData[this]?.range = value }

actual val LocalDateField.content: Writable<LocalDate?>
    get() = LocalDateFieldMoreData[this]?.property ?: throw IllegalStateException()
actual val LocalTimeField.content: Writable<LocalTime?>
    get() = LocalTimeFieldMoreData[this]?.property ?: throw IllegalStateException()
actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() = LocalDateTimeFieldMoreData[this]?.property ?: throw IllegalStateException()

actual var LocalDateField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var LocalTimeField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var LocalDateTimeField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit) = button {
    val me = LocalDateField(this.native)
    val d = LocalDateFieldData()
    LocalDateFieldMoreData[me] = d
    text { ::content { d.property.await()?.renderToString() ?: "Select" } }
    onClick {
        native.showDatePicker(
            d.property.value ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date, d.min, d.max
        ) {
            d.property.value = it
        }
    }
    setup(me)
}

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit) = button {
    val me = LocalTimeField(this.native)
    val d = LocalTimeFieldData()
    LocalTimeFieldMoreData[me] = d
    text { ::content { d.property.await()?.renderToString() ?: "Select" } }
    onClick {
        native.showTimePicker(
            d.property.value ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time, d.min, d.max
        ) {
            d.property.value = it
        }
    }
    setup(me)
}

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit) = button {
    val me = LocalDateTimeField(this.native)
    val d = LocalDateTimeFieldData()
    LocalDateTimeFieldMoreData[me] = d
    text { ::content { d.property.await()?.renderToString() ?: "Select" } }
    onClick {
        native.showDatePicker(
            d.property.value?.date ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            d.min?.date,
            d.max?.date
        ) { date ->
            native.showTimePicker(
                d.property.value?.time ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
                d.min?.time,
                d.max?.time
            ) { time ->
                d.property.value = date.atTime(time)
            }
        }
    }
    setup(me)
}


fun View.showDatePicker(
    start: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    min: LocalDate? = null,
    max: LocalDate? = null,
    onResult: (LocalDate) -> Unit
) {
    DatePickerDialog(context).apply {
        this.updateDate(start.year, start.monthNumber - 1, start.dayOfMonth)
        setOnDateSetListener { _, year, month, dayOfMonth ->
            onResult(LocalDate(year, month + 1, dayOfMonth))
        }
    }.apply {
//        this.datePicker.minDate =
//            min?.atTime(LocalTime(12, 0))?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
//                ?: Long.MIN_VALUE
//        this.datePicker.maxDate =
//            max?.atTime(LocalTime(12, 0))?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
//                ?: Long.MAX_VALUE
        show()
    }
}

fun View.showTimePicker(
    start: LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
    min: LocalTime? = null,
    max: LocalTime? = null,
    onResult: (LocalTime) -> Unit
) {
    TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        onResult(LocalTime(hourOfDay, minute))
    }, start.hour, start.minute, false).apply {
        this.updateTime(start.hour, start.minute)
        show()
    }
}
