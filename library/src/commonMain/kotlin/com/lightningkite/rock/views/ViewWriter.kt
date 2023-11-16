package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.*

@DslMarker
annotation class ViewDsl

@DslMarker
annotation class ViewModifierDsl3

/**
 * An object that writes view trees, similar to the way a Writer in Java sequentially writes text data.
 * Views rendered through here will be inserted into the given parent in the constructor.
 */
class ViewWriter(
    parent: NView
) {
    /**
     * Additional data keyed by string attached to the context.
     * Copied to writers that are split off.
     * Easiest use comes from [viewWriterAddon] and [viewWriterAddonLateInit].
     */
    val addons: MutableMap<String, Any?> = mutableMapOf()

    /**
     * Creates a copy of the [ViewWriter] with the current view as its root.
     * Used for view containers that need their contents removed and replaced later.
     */
    fun split(): ViewWriter = ViewWriter(stack.last()).also {
        it.addons.putAll(this.addons)
    }

    internal var themeJustChanged: Boolean = false

    private val stack = arrayListOf(parent)
    val currentView: NView get() = stack.last()
    private fun <T : NView> stackUse(item: T, action: T.() -> Unit) =
        CalculationContextStack.useIn(item.calculationContext) {
            stack.add(item)
            try {
                action(item)
            } finally {
                stack.removeLast()
            }
        }

    val calculationContext: CalculationContext = stack.last().calculationContext

    /**
     * Runs the given [action] on the next created element before its setup block is run.
     */
    fun beforeNextElementSetup(action: NView.()->Unit) {
        beforeNextElementSetupList.add(action)
    }
    /**
     * Runs the given [action] on the next created element after its setup block is run.
     */
    fun afterNextElementSetup(action: NView.()->Unit) {
        afterNextElementSetupList.add(action)
    }
    private var beforeNextElementSetupList = ArrayList<NView.() -> Unit>()
    private var afterNextElementSetupList = ArrayList<NView.() -> Unit>()
    //    private val wrapperToDoList = ArrayList<NView.() -> Unit>()
    private var popCount = 0

    /**
     * Wraps the next created element within this element.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : NView> wrapNext(element: T, setup: T.() -> Unit): ViewWrapper {
        stack.add(element)
        CalculationContextStack.useIn(element.calculationContext) {
            setup(element)
        }
        popCount++
        return ViewWrapper
    }

    /**
     * Writes an element to the current parent.
     */
    fun <T : NView> element(initialElement: T, setup: T.() -> Unit) {
        initialElement.apply {
            stack.last().addChild(this)
            val beforeCopy = if(beforeNextElementSetupList.isNotEmpty()) beforeNextElementSetupList.toList() else listOf()
            beforeNextElementSetupList = ArrayList()
            val afterCopy = if(afterNextElementSetupList.isNotEmpty()) afterNextElementSetupList.toList() else listOf()
            afterNextElementSetupList = ArrayList()
            var toPop = popCount
            popCount = 0
            stackUse(this) {
                beforeCopy.forEach { it(this) }
                setup()
                afterCopy.forEach { it(this) }
            }
            while (toPop > 0) {
                val item = stack.removeLast()
                stack.last().addChild(item)
                toPop--
            }
//            wrapperToDoList.clear()
        }
    }

    fun <T> forEachUpdating(items: Readable<List<T>>, render: ViewWriter.(Readable<T>)->Unit) {
        // TODO: Faster version
        return with(split()) {
            calculationContext.reactiveScope {
                stack.last().clearChildren()
                repeat(5) {
                    render(Never)
                }
                val data = items.await()
                stack.last().clearChildren()
                data.forEach {
                    render(Constant(it))
                }
            }
        }
    }
}

/**
 * A native view in the underlying view system.
 */
expect open class NView

/**
 * A wrapper in pure common code for a native view.
 * Used to remove naming conflicts with properties.
 */
interface RView<Wraps: NView> {
    val native: Wraps
}

expect val NView.calculationContext: CalculationContext
expect var NView.nativeRotation: Angle
expect var NView.opacity: Double
expect var NView.exists: Boolean
expect var NView.visible: Boolean
expect fun NView.clearChildren()
expect fun NView.addChild(child: NView)

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

fun <T> ViewWriter.forEach(items: Readable<List<T>>, render: ViewWriter.(T)->Unit) = with(split()) {
    calculationContext.reactiveScope {
        currentView.clearChildren()
        items.await().forEach {
            render(it)
        }
    }
}

fun RView<*>.reactiveScope(action: suspend ()->Unit) {
    calculationContext.reactiveScope(action)
}
fun RView<*>.launch(action: suspend () -> Unit) = calculationContext.launch(action)