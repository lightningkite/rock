package com.lightningkite.kiteui.reactive

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Property<T>(startValue: T) : ImmediateWritable<T>, BaseImmediateReadable<T>(startValue),
    ReadWriteProperty<Any?, T> {
    override suspend infix fun set(value: T) {
        this.value = value
    }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}