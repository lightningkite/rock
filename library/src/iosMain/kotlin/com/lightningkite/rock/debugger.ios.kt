package com.lightningkite.rock

import com.lightningkite.rock.views.ExtensionProperty
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

actual fun debugger() {
}

@OptIn(NativeRuntimeApi::class, ExperimentalStdlibApi::class)
actual fun gc(): GCInfo {
    GC.collect()
    println("ExtensionProperty.storage.size = ${ExtensionProperty.storage.size}")
    ExtensionProperty.debug()
    return GCInfo(GC.lastGCInfo!!.memoryUsageAfter["heap"]?.totalObjectsSizeBytes ?: -1L)
}