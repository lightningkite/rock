package com.lightningkite.rock

import com.lightningkite.rock.navigation.camelToHuman
import kotlin.reflect.KClass

actual fun KClass<*>.approximateSimpleName() = this::class.toString().removePrefix("class ")