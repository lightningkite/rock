package com.lightningkite.kiteui.locale

import kotlinx.datetime.*

actual fun LocalDate.renderToString(
    size: RenderSize,
    includeWeekday: Boolean,
    includeYear: Boolean,
    includeEra: Boolean
): String = this.toString()

actual fun LocalTime.renderToString(size: RenderSize): String = this.toString()
actual fun LocalDateTime.renderToString(
    size: RenderSize,
    includeWeekday: Boolean,
    includeYear: Boolean,
    includeEra: Boolean
): String = this.toString()

actual fun TimeZone.renderToString(size: RenderSize): String = this.toString()
actual fun DayOfWeek.renderToString(size: RenderSize): String = this.toString()