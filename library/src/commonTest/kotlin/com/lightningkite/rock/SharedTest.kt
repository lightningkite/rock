package com.lightningkite.rock

import com.lightningkite.rock.reactive.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedTest {
    @Test
    fun sharedPassesNulls() {
        val a = LateInitProperty<Int?>()
        val b = shared { a.await() }
        var starts = 0
        var hits = 0
        with(CalculationContext.Standard()) {
            reactiveScope {
                starts++
                b.await()
                hits++
            }
            assertEquals(0, hits)
            assertEquals(1, starts)
            a.value = null
            assertEquals(1, hits)
            assertEquals(1, starts)
            a.value = 1
            assertEquals(2, hits)
            assertEquals(2, starts)
        }
    }

    @Test fun sharedTerminatesWhenNoOneIsListening() {
        var onRemoveCalled = 0
        var scopeCalled = 0
        val shared = shared {
            scopeCalled++
            onRemove { onRemoveCalled++ }
            42
        }
        assertEquals(0, scopeCalled)
        assertEquals(0, onRemoveCalled)
        val removeListener = shared.addListener {  }
        assertEquals(1, scopeCalled)
        assertEquals(0, onRemoveCalled)
        removeListener()
        assertEquals(1, scopeCalled)
        assertEquals(1, onRemoveCalled)
    }

    @Test fun sharedSharesCalculations() {
        var hits = 0
        val property = Property(1)
        val a = shared {
            hits++
            property.await()
        }
        testContext {
            reactiveScope {
                a.await()
            }
            launch {
                a.await()
            }
            reactiveScope {
                a.await()
            }
            assertEquals(1, hits)

            property.value = 2
            assertEquals(2, hits)
        }.cancel()

        // Shouldn't be listening anymore, so it does not trigger a hit
        property.value = 3
        assertEquals(2, hits)

        testContext {
            reactiveScope {
                a.await()
            }
            launch {
                a.await()
            }
            reactiveScope {
                a.await()
            }
        }.cancel()
        assertEquals(3, hits)
    }
}