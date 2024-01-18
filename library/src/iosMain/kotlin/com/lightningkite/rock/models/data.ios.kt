package com.lightningkite.rock.models

actual fun Dimension.compareToImpl(other: Dimension): Int = value.compareTo(other.value)