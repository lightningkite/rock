package com.lightningkite.rock

import com.lightningkite.rock.reactive.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReactivityTests {
    @Test fun testAsync() {

        var cont: Continuation<String>? = null
        val item = asyncGlobal<String> {
            println("Calculating...")
            suspendCoroutineCancellable {
                cont = it
                return@suspendCoroutineCancellable {}
            }
        }
        launchGlobal {
            println("A: ${item.await()}")
        }
        launchGlobal {
            println("B: ${item.await()}")
        }
        cont?.resume("Success")

    }
    @Test
    fun waitingTest() {
        val property = Property<Int?>(null)
        val emissions = ArrayList<Int>()
        with(CalculationContext.Standard()) {
            reactiveScope {
                emissions.add(property.waitForNotNull.await())
            }
            repeat(10) {
                property.value = null
                property.value = it
            }
            cancel()
        }
        assertEquals((0..9).toList(), emissions)
    }


    @Test fun doubleAwait(){

        val base = LateInitProperty<Int>()
        val sub = shared{
            base
        }

        var usableEmits = 0
        val usable = shared{
            println("A")
            val t = sub.await().also { println("B") }.await()
//            val t = base.await()
            println("C")
            usableEmits++
            t
        }

        testContext {
            reactiveScope {
                println(usable.await())
            }

            base.value = 1
            assertEquals(1, usableEmits)
        }

    }

    @Test fun sharedShutdownTest() {
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

    @Test fun basics() {
        val a = Property(1)
        var bInvocations = 0
        val b = shared { a.await().also { bInvocations++ } }
        testContext {
            reactiveScope {
                b.await()
                println("bInvocations: $bInvocations")
            }
        }
    }

    @Test fun sharedTest() {
        val a = Property(1)
        val b = Property(2)
        var cInvocations = 0
        val c = shared { cInvocations++; println("cInvocations: $cInvocations"); a.await() + b.await() }
        println("$c: c")
        var dInvocations = 0
        val d = shared { dInvocations++; println("dInvocations: $dInvocations"); c.await() + c.await() }
        println("$d: d")
        var eInvocations = 0
        val e = shared { eInvocations++; println("eInvocations: $eInvocations"); d.await() / 2 }
        println("$e: e")

        with(CalculationContext.Standard()) {
            reactiveScope {
                e.await()
            }
            assertEquals(1, cInvocations)
            assertEquals(1, dInvocations)
            assertEquals(1, eInvocations)
            println("a.value = 3")
            a.value = 3
            assertEquals(2, cInvocations)
            assertEquals(2, dInvocations)
            assertEquals(2, eInvocations)
            println("b.value = 4")
            b.value = 4
            assertEquals(3, cInvocations)
            assertEquals(3, dInvocations)
            assertEquals(3, eInvocations)
        }
    }

    @Test fun sharedTest2() {
        val a = Property(1)
        val b = Property(2)
        var cInvocations = 0
        val c = shared { cInvocations++; println("cInvocations: $cInvocations"); a.await() + b.await() }
        println("$c: c")
        var dInvocations = 0
        val d = shared { dInvocations++; println("dInvocations: $dInvocations"); c.await() + b.await() }
        println("$d: d")
        var eInvocations = 0
        val e = shared { eInvocations++; println("eInvocations: $eInvocations"); d.await() / 2 }
        println("$e: e")

        with(CalculationContext.Standard()) {
            reactiveScope {
                e.await()
            }
            assertEquals(1, cInvocations)
            assertEquals(1, dInvocations)
            assertEquals(1, eInvocations)
            println("a.value = 3")
            a.value = 3
            assertEquals(2, cInvocations)
            assertEquals(2, dInvocations)
            assertEquals(2, eInvocations)
            println("b.value = 4")
            b.value = 4
            assertEquals(3, cInvocations)
            assertTrue(4 >= dInvocations)
            assertTrue(4 >= eInvocations)
        }
    }

    @Test fun sharedTest3() {
        val a = VirtualDelay { 1 }
        val c = shared { a.await() }
        val d = shared { c.await() }
        testContext {
            launch { println("launch got " + d.await()) }
            reactiveScope { println("reactiveScope got " + d.await()) }
            println("Ready... GO!")
            a.go()
        }
    }
}

class VirtualDelay<T>(val action: () -> T) {
    val continuations = ArrayList<Continuation<T>>()
    var value: T? = null
    var ready: Boolean = false
    suspend fun await(): T {
        if(ready) return value as T
        return suspendCoroutineCancellable {
            continuations.add(it)
            return@suspendCoroutineCancellable {}
        }
    }
    fun go() {
        val value = action()
        this.value = value
        ready = true
        for(continuation in continuations) {
            continuation.resume(value)
        }
        continuations.clear()
    }
}

fun testContext(action: CalculationContext.()->Unit): Cancellable {
    var error: Throwable? = null
    val onRemoveSet = HashSet<()->Unit>()
    with(object: CalculationContext {
        override fun onRemove(action: () -> Unit) {
            onRemoveSet.add(action)
        }

        override fun notifyComplete(result: Result<Unit>) {
            result.onFailure { t ->
                if(t !is CancelledException) {
                    t.printStackTrace()
                    error = t
                }
            }
        }
    }) {
        action()
        if(error != null) throw error!!
    }
    return object: Cancellable {
        override fun cancel() {
            onRemoveSet.forEach { it() }
            onRemoveSet.clear()
        }
    }
}