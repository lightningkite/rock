package com.lightningkite.rock.locale

import kotlinx.datetime.*

enum class RenderSize { Numerical, Abbreviation, Full }
expect fun LocalDate.renderToString(size: RenderSize = RenderSize.Full, includeWeekday: Boolean = false, includeYear: Boolean = true, includeEra: Boolean = false): String
expect fun LocalTime.renderToString(size: RenderSize = RenderSize.Full): String
expect fun LocalDateTime.renderToString(size: RenderSize = RenderSize.Full, includeWeekday: Boolean = false, includeYear: Boolean = true, includeEra: Boolean = false): String
expect fun Instant.renderToString(size: RenderSize = RenderSize.Full, zone: TimeZone = TimeZone.currentSystemDefault(), includeWeekday: Boolean = false, includeYear: Boolean = true, includeEra: Boolean = false): String
expect fun TimeZone.renderToString(size: RenderSize = RenderSize.Full): String
expect fun DayOfWeek.renderToString(size: RenderSize = RenderSize.Full): String