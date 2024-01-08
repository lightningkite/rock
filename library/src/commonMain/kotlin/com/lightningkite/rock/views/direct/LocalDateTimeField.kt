package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Action
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.jvm.JvmInline

expect class NLocalDateField : NView

@JvmInline
value class LocalDateField(override val native: NLocalDateField) : RView<NLocalDateField>

@ViewDsl
expect fun ViewWriter.localDateField(setup: LocalDateField.() -> Unit = {}): Unit
expect val LocalDateField.content: Writable<LocalDate?>
expect var LocalDateField.action: Action?
expect var LocalDateField.range: ClosedRange<LocalDate>?

expect class NLocalTimeField : NView

@JvmInline
value class LocalTimeField(override val native: NLocalTimeField) : RView<NLocalTimeField>

@ViewDsl
expect fun ViewWriter.localTimeField(setup: LocalTimeField.() -> Unit = {}): Unit
expect val LocalTimeField.content: Writable<LocalTime?>
expect var LocalTimeField.action: Action?
expect var LocalTimeField.range: ClosedRange<LocalTime>?

expect class NLocalDateTimeField : NView

@JvmInline
value class LocalDateTimeField(override val native: NLocalDateTimeField) : RView<NLocalDateTimeField>

@ViewDsl
expect fun ViewWriter.localDateTimeField(setup: LocalDateTimeField.() -> Unit = {}): Unit
expect val LocalDateTimeField.content: Writable<LocalDateTime?>
expect var LocalDateTimeField.action: Action?
expect var LocalDateTimeField.range: ClosedRange<LocalDateTime>?