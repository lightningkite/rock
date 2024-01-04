package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.MaterialLikeTheme
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*

/**
 * An object that writes view trees, similar to the way a Writer in Java sequentially writes text data.
 * Views rendered through here will be inserted into the given parent in the constructor.
 */
class ViewWriter(
    parent: NView?,
    private val startDepth: Int = 0,
) {
    val depth: Int get() = stack.size - 1 + startDepth
    var rootCreated: NView? = null

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
    fun split(): ViewWriter = ViewWriter(stack.last(), startDepth = depth).also {
        it.addons.putAll(this.addons)
        it.currentTheme = currentTheme
        it.isRoot = isRoot
        it.transitionNextView = transitionNextView
    }

    /**
     * Creates a copy of the [ViewWriter] with no root view.
     * Used for view containers that need their contents removed and replaced later.
     */
    fun newViews(): ViewWriter = ViewWriter(null, startDepth = depth).also {
        it.addons.putAll(this.addons)
        it.currentTheme = currentTheme
        it.isRoot = isRoot
        it.transitionNextView = transitionNextView
    }

    /**
     * Creates a copy of the [ViewWriter] with no root view.
     * Used for view containers that need their contents removed and replaced later.
     */
    fun targeting(view: NView): ViewWriter = ViewWriter(view, startDepth = depth).also {
        it.addons.putAll(this.addons)
        it.currentTheme = currentTheme
        it.isRoot = isRoot
        it.transitionNextView = transitionNextView
    }

    private val stack = if(parent == null) arrayListOf() else arrayListOf(parent)
    val currentView: NView get() = stack.last()
    private inline fun <T : NView> stackUse(item: T, action: T.() -> Unit) =
        CalculationContextStack.useIn(item.calculationContext) {
            stack.add(item)
            try {
                action(item)
            } finally {
                stack.removeLast()
            }
        }

    var currentTheme: suspend () -> Theme = { MaterialLikeTheme() }
    inline fun <T> withThemeGetter(crossinline calculate: suspend (suspend ()->Theme)->Theme, action: ()->T): T {
        val old = currentTheme
        currentTheme = { calculate(old) }
        try {
            return action()
        } finally {
            currentTheme = old
        }
    }
    @ViewModifierDsl3 inline fun ViewWriter.themeModifier(crossinline calculate: suspend (suspend ()->Theme)->Theme): ViewWrapper {
        val old = currentTheme
        currentTheme = { calculate(old) }
        afterNextElementSetup {
            currentTheme = old
        }
        return ViewWrapper
    }

    /**
     * Adds a card / border / padding to the next view.
     */
    sealed interface TransitionNextView {
        object No: TransitionNextView
        object Yes: TransitionNextView
        class Maybe(val logic: suspend () -> Boolean): TransitionNextView
    }
    var transitionNextView: TransitionNextView = TransitionNextView.No
    var isRoot: Boolean = true

    val calculationContext: CalculationContext get() = stack.last().calculationContext

    /**
     * Runs the given [action] on the next created element before its setup block is run.
     */
    fun beforeNextElementSetup(action: NView.() -> Unit) {
        beforeNextElementSetupList.add(action)
    }

    /**
     * Runs the given [action] on the next created element after its setup block is run.
     */
    fun afterNextElementSetup(action: NView.() -> Unit) {
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
        val beforeCopy = if (beforeNextElementSetupList.isNotEmpty()) beforeNextElementSetupList.toList() else listOf()
        beforeNextElementSetupList = ArrayList()
        val afterCopy = if (afterNextElementSetupList.isNotEmpty()) afterNextElementSetupList.toList() else listOf()
        afterNextElementSetupList = ArrayList()
        CalculationContextStack.useIn(element.calculationContext) {
            beforeCopy.forEach { it(element) }
            setup(element)
            afterCopy.forEach { it(element) }
        }
        popCount++
        return ViewWrapper
    }

    /**
     * Writes an element to the current parent.
     */
    fun <T : NView> element(initialElement: T, setup: T.() -> Unit) {
        initialElement.apply {
            stack.lastOrNull()?.addChild(this) ?: run { rootCreated = this }
            val beforeCopy =
                if (beforeNextElementSetupList.isNotEmpty()) beforeNextElementSetupList.toList() else listOf()
            beforeNextElementSetupList = ArrayList()
            val afterCopy = if (afterNextElementSetupList.isNotEmpty()) afterNextElementSetupList.toList() else listOf()
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
                stack.lastOrNull()?.addChild(item) ?: run { rootCreated = item }
                toPop--
            }
//            wrapperToDoList.clear()
        }
    }

    fun <T> forEachUpdating(items: Readable<List<T>>, render: ViewWriter.(Readable<T>) -> Unit) {
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
