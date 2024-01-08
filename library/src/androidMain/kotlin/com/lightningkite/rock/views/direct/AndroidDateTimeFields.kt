package com.lightningkite.rock.views.direct

import android.app.ActionBar.LayoutParams
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.updateLayoutParams
import kotlinx.datetime.*
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
                        localDate = LocalDate.of(year, month + 1, dayOfMonth)
                    }
                }.apply {
                    this.datePicker.minDate = minDate.toKotlinLocalDate().atTime(kotlinx.datetime.LocalTime(12, 0)).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                    this.datePicker.maxDate = maxDate.toKotlinLocalDate().atTime(kotlinx.datetime.LocalTime(12, 0)).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
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
                        dateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0, 0)
                    }
                }.apply {
                    this.datePicker.minDate = minDate.toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                    this.datePicker.maxDate = maxDate.toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                    show()
                }
            }
        }
    }
}