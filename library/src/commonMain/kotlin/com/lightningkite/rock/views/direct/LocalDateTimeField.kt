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
import kotlin.contracts.*

expect class NLocalDateField : NView

@JvmInline
value class LocalDateField(override val native: NLocalDateField) : RView<NLocalDateField>

@ViewDsl
expect fun ViewWriter.localDateFieldActual(setup: LocalDateField.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.localDateField(noinline setup: LocalDateField.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; localDateFieldActual(setup) }
expect val LocalDateField.content: Writable<LocalDate?>
expect var LocalDateField.action: Action?
expect var LocalDateField.range: ClosedRange<LocalDate>?

expect class NLocalTimeField : NView

@JvmInline
value class LocalTimeField(override val native: NLocalTimeField) : RView<NLocalTimeField>

@ViewDsl
expect fun ViewWriter.localTimeFieldActual(setup: LocalTimeField.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.localTimeField(noinline setup: LocalTimeField.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; localTimeFieldActual(setup) }
expect val LocalTimeField.content: Writable<LocalTime?>
expect var LocalTimeField.action: Action?
expect var LocalTimeField.range: ClosedRange<LocalTime>?

expect class NLocalDateTimeField : NView

@JvmInline
value class LocalDateTimeField(override val native: NLocalDateTimeField) : RView<NLocalDateTimeField>

@ViewDsl
expect fun ViewWriter.localDateTimeFieldActual(setup: LocalDateTimeField.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.localDateTimeField(noinline setup: LocalDateTimeField.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; localDateTimeFieldActual(setup) }
expect val LocalDateTimeField.content: Writable<LocalDateTime?>
expect var LocalDateTimeField.action: Action?
expect var LocalDateTimeField.range: ClosedRange<LocalDateTime>?