package com.lightningkite.rock.views.direct

import android.app.ActionBar.LayoutParams
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.updateLayoutParams
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.max

private const val defaultEms = 6

class AndroidDateField : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var minDate: LocalDate = LocalDate.EPOCH
    var maxDate: LocalDate = LocalDate.now().plusYears(100)

    var localDate: LocalDate = LocalDate.now()
        set(value) {
            field = value
            updateText(field)
        }

    private fun updateText(date: LocalDate) {
        setText(date.toString())
    }

    fun defaultRange() {
        minDate = LocalDate.EPOCH
        maxDate = LocalDate.now().plusYears(100)
    }

    init {
        post {
            setEms(defaultEms)
            updateLayoutParams { width = LayoutParams.WRAP_CONTENT }
            updateText(this.localDate)
        }
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(context).apply {
                    setOnDateSetListener { _, year, month, dayOfMonth ->
                        localDate = LocalDate.of(year, month, dayOfMonth)
                    }
                }.apply {
                    this.datePicker.minDate = minDate.milliseconds
                    this.datePicker.maxDate = maxDate.milliseconds
                    show()
                }
            }
        }
    }
}

class AndroidTimeField : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    var time: LocalTime = LocalTime.now()
        set(value) {
            field = value
            updateText(value)
        }

    private fun updateText(value: LocalTime) {
        setText(dateTimeFormatter.format(value))
    }

    init {
        post {
            setEms(defaultEms)
            updateLayoutParams { width = LayoutParams.WRAP_CONTENT }
            updateText(this.time)
        }
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    this.time = LocalTime.of(hourOfDay, minute)
                }, time.hour, time.minute, false)
            }
        }
    }
}

fun LocalDateTime.toKotlinDateTime(): kotlinx.datetime.LocalDateTime {
    return kotlinx.datetime.LocalDateTime(this.year, this.month, this.dayOfMonth, this.hour, this.minute, this.second)
}

fun LocalTime.toKotlinTime(): kotlinx.datetime.LocalTime {
    return kotlinx.datetime.LocalTime(this.hour, this.minute, this.second, this.nano)
}

private val LocalDateTime.milliseconds: Long
    get() {
        val seconds = this.toEpochSecond(ZoneOffset.of(ZoneId.systemDefault().toString()))
        return seconds * 1000
    }

private val LocalDate.milliseconds: Long
    get() {
        return LocalDateTime.of(this.year, this.month, this.dayOfMonth, 0, 0, 0).milliseconds
    }

fun LocalDate.toKotlinDate(): kotlinx.datetime.LocalDate {
    return kotlinx.datetime.LocalDate(this.year, this.month, this.dayOfMonth)
}
class AndroidDateTimeField : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var minDate: LocalDateTime = LocalDate.EPOCH.atTime(0, 0)
    var maxDate: LocalDateTime = LocalDateTime.now().plusYears(100)

    var dateTime: LocalDateTime = LocalDateTime.now()
        set(value) {
            field = value
            updateText(field)
        }

    private fun updateText(field: LocalDateTime) {
        setText(field.toString())
    }

    init {
        post {
            setEms(defaultEms)
            updateLayoutParams { width = LayoutParams.WRAP_CONTENT }
            updateText(this.dateTime)
        }
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(context).apply {
                    setOnDateSetListener { _, year, month, dayOfMonth ->
                        dateTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
                    }
                }.apply {
                    this.datePicker.minDate = minDate.milliseconds
                    this.datePicker.maxDate = maxDate.milliseconds
                    show()
                }
            }
        }
    }
}