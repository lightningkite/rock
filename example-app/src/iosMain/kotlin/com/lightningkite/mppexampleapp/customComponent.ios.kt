package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ExtensionData
import com.lightningkite.rock.views.ViewWriter
import kotlin.native.runtime.NativeRuntimeApi

actual class CustomComponent {
    actual var src: String
        get() = TODO("Not yet implemented")
        set(value) {}
}

actual fun ViewWriter.customComponent(setup: CustomComponent.() -> Unit) {
}

@OptIn(NativeRuntimeApi::class, ExperimentalStdlibApi::class)
actual fun gcCheck() {
    kotlin.native.runtime.GC.collect()
    ExtensionData.clean()
    kotlin.native.runtime.GC.collect()
    kotlin.native.runtime.GC.lastGCInfo?.let {
        println("memoryUsageAfter " + it.memoryUsageAfter.mapValues { it.value.totalObjectsSizeBytes })
    }
}