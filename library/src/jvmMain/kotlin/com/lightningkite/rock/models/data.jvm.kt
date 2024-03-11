package com.lightningkite.rock.models

actual val Dimension.px: Double get() = value.filter { it.isDigit() }.toDouble()