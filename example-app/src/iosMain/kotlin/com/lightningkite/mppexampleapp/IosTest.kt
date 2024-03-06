package com.lightningkite.mppexampleapp

import com.lightningkite.rock.clockMillis
import com.lightningkite.rock.delay
import com.lightningkite.rock.gc
import com.lightningkite.rock.models.px
import com.lightningkite.rock.reactive.CalculationContextStack
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView
import platform.darwin.NSObject
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.identityHashCode
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

@OptIn(ExperimentalForeignApi::class)
class CustomUIView: UIView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {
}

@OptIn(ExperimentalForeignApi::class)
class KObj: NSObject() {
    val r = CGRectMake(0.0, 0.0, 0.0, 0.0)
    val dataBlock = ByteArray(1024) { 0 }
}

@OptIn(ExperimentalStdlibApi::class, NativeRuntimeApi::class)
fun getUsage(): Long {
    GC.collect()
    return GC.lastGCInfo!!.memoryUsageAfter["heap"]!!.totalObjectsSizeBytes
}

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
fun ViewWriter.iosTest() {
    fun leakTest(label: String, doAction: ()->Unit) {
        println("--- LEAK TEST $label ----")
        var total = 0L
        doAction()
        repeat(10000) { iter ->
            val start = getUsage()
            doAction()
            val after = getUsage()
            (after - start).let {
                total += it
                if(it == 0L) {
                    if(iter % 1000 == 0) print('.')
                } else println("Used ${it.toString().padStart(8, ' ')} bytes")
            }
        }
        println("---")
        println("Total usage: ${total.toString().padStart(8, ' ')}")
    }

//    leakTest {
//        val v = UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))
//        currentView.addChild(v)
//        v.removeFromSuperview()
//    }
    repeat(1000) {
        leakTest("Custom No Add") {
            val v = CustomUIView()
            val sub = CustomUIView()
            v.addSubview(sub)
            v.shutdown()
        }
    }
    leakTest("Manual Custom") {
        val v = CustomUIView()
        currentView.addNView(v)
        v.removeFromSuperview()
    }
    leakTest("Manual") {
        val v = UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))
        currentView.addNView(v)
        CalculationContextStack.useIn(v.calculationContext) {

        }
        v.removeFromSuperview()
        v.shutdown()
    }

    col {
        col {
            leakTest("Vanilla") {
                element(UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))) {
                }
                currentView.clearNViews()
            }
            leakTest("Text") {
                text { content = "test" }
                currentView.clearNViews()
            }
            leakTest("Vanilla with child") {
                element(UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))) {
                    element(UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))) {
                    }
                }
                currentView.clearNViews()
            }
            leakTest("Extended") {
                element(CustomUIView()) {
                }
                currentView.clearNViews()
            }
            leakTest("Extended with child") {
                element(CustomUIView()) {
                    element(UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))) {
                    }
                }
                currentView.clearNViews()
            }
            leakTest("Stack") {
                stack { element(UIView(CGRectMake(0.0, 0.0, 0.0, 0.0))) {
                } }
                currentView.clearNViews()
            }
        }
        button {
            val counter = Property(1)
            text {
                launch {
                    while (true) {
                        content = counter.value.toString()
                        delay(1000L)
                        counter.value++
                    }
                }
            }
            onClick {
                println(clockMillis())
                gc()
            }
        }
    }
}

fun ViewWriter.iosTest2() {
    col {
        spacing = 0.px
        text("A")
        text("B")
        text("C")
    }
}