package com.lightningkite.rock.views

import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.reactive.CalculationContext
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import kotlin.jvm.JvmInline


/**
 * A wrapper in pure common code for a native view.
 * Used to remove naming conflicts with properties.
 */
interface RView<Wraps: NView> {
    val native: Wraps
}

val RView<*>.calculationContext: CalculationContext get() = native.calculationContext
var RView<*>.rotation: Angle
    get() = native.nativeRotation
    set(value) { native.nativeRotation = value }
var RView<*>.opacity: Double
    get() = native.opacity
    set(value) { native.opacity = value }
var RView<*>.exists: Boolean
    get() = native.exists
    set(value) { native.exists = value }
var RView<*>.visible: Boolean
    get() = native.visible
    set(value) { native.visible = value }
var RView<*>.spacing: Dimension
    get() = native.spacing
    set(value) { native.spacing = value }
var RView<*>.ignoreInteraction: Boolean
    get() = native.ignoreInteraction
    set(value) { native.ignoreInteraction = value }

fun <T> ViewWriter.forEach(items: Readable<List<T>>, render: ViewWriter.(T)->Unit) = with(split()) {
    calculationContext.reactiveScope {
        currentView.clearNViews()
        items.await().forEach {
            render(it)
        }
    }
}

fun RView<*>.reactiveScope(action: suspend ()->Unit) {
    calculationContext.reactiveScope(null, action)
}
fun RView<*>.launch(action: suspend () -> Unit) = calculationContext.launch(action)