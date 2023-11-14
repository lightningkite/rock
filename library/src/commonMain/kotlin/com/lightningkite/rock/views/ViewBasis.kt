package com.lightningkite.rock.views

import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.*

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

expect class ViewContext {
    val addons: MutableMap<String, Any?>
    val calculationContext: CalculationContext
    fun beforeNextElementSetup(action: NView.()->Unit)
    fun afterNextElementSetup(action: NView.()->Unit)
    fun split(): ViewContext
    fun clearChildren()
    fun <T> forEachUpdating(items: Readable<List<T>>, render: ViewContext.(Readable<T>)->Unit)
}

expect open class NView
interface RView<Wraps: NView> {
    val native: Wraps
}

expect val NView.onRemove: CalculationContext
expect var NView.rotation: Angle
expect var NView.alpha: Double
expect var NView.exists: Boolean
expect var NView.visible: Boolean

val RView<*>.onRemove: CalculationContext get() = native.onRemove
var RView<*>.rotation: Angle
    get() = native.rotation
    set(value) { native.rotation = value }
var RView<*>.opacity: Double
    get() = native.alpha
    set(value) { native.alpha = value }
var RView<*>.exists: Boolean
    get() = native.exists
    set(value) { native.exists = value }
var RView<*>.visible: Boolean
    get() = native.visible
    set(value) { native.visible = value }

fun <T> ViewContext.forEach(items: Readable<List<T>>, render: ViewContext.(T)->Unit) = with(split()) {
    calculationContext.reactiveScope {
        clearChildren()
        items.await().forEach {
            render(it)
        }
    }
}

fun RView<*>.reactiveScope(action: suspend ()->Unit) {
    onRemove.reactiveScope(action)
}
fun RView<*>.launch(action: suspend () -> Unit) = onRemove.launch(action)