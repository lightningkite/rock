package com.lightningkite.rock

import com.lightningkite.rock.reactive.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ReactivityTests {
    @Test
    fun wait() {
        val property = Property<Int?>(null)
        val emissions = ArrayList<Int>()
        with(CalculationContext.Test()) {
            reactiveScope {
                emissions.add(property.waitForNotNull.await())
            }
            repeat(10) {
                property.value = it
                property.value = null
            }
            cancel()
        }
        assertEquals((0..9).toList(), emissions)
    }
}