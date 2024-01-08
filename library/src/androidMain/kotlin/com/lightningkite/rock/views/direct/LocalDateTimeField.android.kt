package com.lightningkite.rock.views.direct

import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.datetime.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateField = AndroidDateField
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalTimeField = AndroidTimeField
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLocalDateTimeField = AndroidDateTimeField

actual var LocalDateField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var LocalDateField.range: ClosedRange<LocalDate>?
    get() {
        return native.minDate.toKotlinLocalDate().rangeTo(native.maxDate.toKotlinLocalDate())
    }
    set(value) {
        if (value == null) {
            native.defaultRange()
        } else {
            native.minDate = value.start.toJavaLocalDate()
            native.maxDate = value.endInclusive.toJavaLocalDate()
        }
    }
actual val LocalDateField.content: Writable<LocalDate?>
    get() {
        return object : Writable<LocalDate?> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@content.native, listener)
                this@content.native.addTextChangedListener {
                    NativeListeners.listeners.get(this@content.native)?.forEach { action -> action() }
                }
                return this@content.native.removeListener(listener)
            }

            override suspend fun awaitRaw(): LocalDate? {
                return this@content.native.localDate.toKotlinLocalDate()
            }

            override suspend fun set(value: LocalDate?) {
                if (value == null) {
                    this@content.native.setText("")
                } else {
                    this@content.native.localDate = value?.toJavaLocalDate() ?: java.time.LocalDate.now()
                }
            }
        }
    }
actual val LocalTimeField.content: Writable<LocalTime?>
    get() {
        return object : Writable<LocalTime?> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@content.native, listener)
                this@content.native.addTextChangedListener {
                    NativeListeners.listeners.get(this@content.native)?.forEach { action -> action() }
                }
                return this@content.native.removeListener(listener)
            }

            override suspend fun awaitRaw(): LocalTime? {
                return LocalTime(this@content.native.time.hour, this@content.native.time.minute)
            }

            override suspend fun set(value: LocalTime?) {
                if (value == null) {
                    this@content.native.setText("")
                } else {
                    this@content.native.time = java.time.LocalTime.of(value.hour, value.minute)
                }
            }
        }
    }
actual var LocalTimeField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var LocalTimeField.range: ClosedRange<LocalTime>?
    get() {
        return LocalTime(0, 0, 0, 0)
            .rangeTo(LocalTime(23, 59, 59, 0))
    }
    set(value) {
//        TODO("Not Implemented")
    }
actual val LocalDateTimeField.content: Writable<LocalDateTime?>
    get() {
        return object : Writable<LocalDateTime?> {
            override fun addListener(listener: () -> Unit): () -> Unit {
                NativeListeners.listeners.addListener(this@content.native, listener)
                this@content.native.addTextChangedListener {
                    NativeListeners.listeners.get(this@content.native)?.forEach { action -> action() }
                }
                return this@content.native.removeListener(listener)
            }

            override suspend fun awaitRaw(): LocalDateTime? {
                return LocalDateTime(
                    this@content.native.dateTime.year,
                    this@content.native.dateTime.month,
                    this@content.native.dateTime.dayOfMonth,
                    this@content.native.dateTime.hour,
                    this@content.native.dateTime.minute,
                    this@content.native.dateTime.second
                )
            }

            override suspend fun set(value: LocalDateTime?) {
                if (value != null) {
                    this@content.native.dateTime = java.time.LocalDateTime.of(
                        value.year, value.month, value.dayOfMonth, value.hour, value.minute, value.second
                    )
                } else {
                    this@content.native.setText("")
                }
            }
        }
    }
actual var LocalDateTimeField.action: Action?
    get() {
        return native.getAction
    }
    set(value) {
        native.setAction(value)
    }
actual var LocalDateTimeField.range: ClosedRange<LocalDateTime>?
    get() {
        val minDate: java.time.LocalDateTime = this@range.native.minDate
        val maxDate: java.time.LocalDateTime = this@range.native.maxDate
        val min = minDate.toKotlinLocalDateTime()
        val max = maxDate.toKotlinLocalDateTime()
        return min.rangeTo(max)
    }
    set(value) {
        if (value != null) {
            this@range.native.minDate = value.start.toJavaLocalDateTime()
            this@range.native.maxDate = value.endInclusive.toJavaLocalDateTime()
        }
    }

@ViewDsl
actual fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit) {
    return viewElement(factory = ::AndroidDateField, wrapper = ::LocalDateField) {
        handleTheme<TextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit) {
    return viewElement(factory = ::AndroidTimeField, wrapper = ::LocalTimeField) {
        handleTheme<TextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}

@ViewDsl
actual fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit) {
    return viewElement(factory = ::AndroidDateTimeField, wrapper = ::LocalDateTimeField) {
        handleTheme<TextView>(native, foreground = applyTextColorFromTheme)
        setup()
    }
}