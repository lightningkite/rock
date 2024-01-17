package com.lightningkite.rock

import com.lightningkite.rock.views.ExtensionProperty
import com.lightningkite.rock.views.NViewCalculationContext
import platform.Foundation.NSThread
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.reflect.KClass

actual fun debugger() {
}

@OptIn(ExperimentalNativeApi::class)
object ObjCountTrackers {
    private val allWeak = HashMap<KClass<*>, ArrayList<WeakReference<Any>>>()
    val alive: Map<KClass<*>, Int> get() {
        allWeak.values.forEach { it.removeAll { it.get() == null } }
        return allWeak.mapValues { it.value.size }
    }
    fun track(instance: Any) {
        allWeak.getOrPut(instance::class) { ArrayList() }.add(WeakReference(instance))
    }
}

@OptIn(NativeRuntimeApi::class, ExperimentalStdlibApi::class)
actual fun gc(): GCInfo {
    println("ExtensionProperty.storage.size = ${ExtensionProperty.storage.size}")
    ObjCountTrackers.alive.entries.forEach {
        println("${it.key.qualifiedName}.alive = ${it.value}")
    }
    GC.collect()
    println("ExtensionProperty.storage.size = ${ExtensionProperty.storage.size}")
    ObjCountTrackers.alive.entries.forEach {
        println("${it.key.qualifiedName}.alive = ${it.value}")
    }
    ExtensionProperty.debug()
    return GCInfo(GC.lastGCInfo!!.memoryUsageAfter["heap"]?.totalObjectsSizeBytes ?: -1L)
}

actual fun assertMainThread() {
    if(!NSThread.isMainThread) throw Error("NOT MAIN THREAD!!!")
}