package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.dom.CSSStyleDeclaration
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.compareToImpl
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = HTMLDivElement

@ViewDsl
actual inline fun ViewWriter.recyclerViewActual(crossinline setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div", viewDraws = false) {
        classList.add("recyclerView")
        val newViews: ViewWriter = newViews()
        this.asDynamic().__ROCK__controller = RecyclerController2(
            root = this,
            newViews = newViews,
            vertical = true
        )
        setup(RecyclerView(this))
    }
}

@ViewDsl
actual inline fun ViewWriter.horizontalRecyclerViewActual(crossinline setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div", viewDraws = false) {
        classList.add("recyclerView")
        val newViews: ViewWriter = newViews()
        this.asDynamic().__ROCK__controller = RecyclerController2(
            root = this,
            newViews = newViews,
            vertical = false
        )
        setup(RecyclerView(this))
    }
}

actual var RecyclerView.columns: Int
    get() = (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).columns
    set(value) {
        (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).columns = value
    }

actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
): Unit {
    (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).dataAndRenderer = DataAndRenderer(
        data = items,
        renderer = render
    )
}

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
    (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).jump(index, align ?: Align.Center)
}

actual val RecyclerView.firstVisibleIndex: Readable<Int>
    get() = (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).firstVisible

actual val RecyclerView.lastVisibleIndex: Readable<Int>
    get() = (native.asDynamic().__ROCK__controller as RecyclerControllerInterface).lastVisible

interface Indexed<T> {
    val min: Int
    val max: Int
    operator fun get(index: Int): T
}

fun <T> List<T>.asIndexed() = object : Indexed<T> {
    override val min: Int
        get() = 0
    override val max: Int
        get() = this@asIndexed.size - 1

    override fun get(index: Int): T {
        return this@asIndexed.get(index)
    }
}

interface RecyclerControllerInterface {
    val firstVisible: Readable<Int>
    val lastVisible: Readable<Int>
    fun jump(index: Int, align: Align)
    var dataAndRenderer: DataAndRenderer<*>
    var columns: Int
}

class RecyclerController2(
    val root: HTMLDivElement,
    val newViews: ViewWriter,
    val vertical: Boolean = true
) : RecyclerControllerInterface {
    override val firstVisible = Property(0)
    override val lastVisible = Property(0)
    override fun jump(index: Int, align: Align) {
    }

    val contentHolder = (document.createElement("div") as HTMLDivElement).apply {
        classList.add("contentScroll-${if (vertical) "V" else "H"}")
    }
    val fakeScroll = (document.createElement("div") as HTMLDivElement).apply {
        classList.add("barScroll")
    }
    val capView = (document.createElement("div") as HTMLDivElement).apply {
        style.size = "1px"
        style.start = "${reservedScrollingSpace}px"
        style.backgroundColor = "red"
    }

    init {
        root.addNView(contentHolder)
        root.addNView(fakeScroll)
        contentHolder.addNView(capView)
        ResizeObserver { entries, obs ->
            val newSize = root.clientSize
            if (viewportSize != newSize) {
                viewportSize = newSize
            }
        }.observe(root)
    }

    private var NView.scrollStart
        get() = if (vertical) scrollTop else scrollLeft
        set(value) {
            if (vertical) scrollTop = value else scrollLeft = value
        }
    private val NView.offsetStart
        get() = if (vertical) offsetTop else offsetLeft
    private val NView.clientSize
        get() = if (vertical) clientHeight else clientWidth
    private val NView.scrollSize
        get() = if (vertical) scrollHeight else scrollWidth
    private val DOMRect.size
        get() = if (vertical) height else width
    private val DOMRect.start
        get() = if (vertical) top else left
    private val DOMRect.end
        get() = if (vertical) bottom else right
    private val CSSStyleDeclaration.marginStart
        get() = if (vertical) marginTop else marginLeft
    private val CSSStyleDeclaration.marginEnd
        get() = if (vertical) marginBottom else marginRight
    private var CSSStyleDeclaration.size
        get() = if (vertical) height else width
        set(value) {
            if (vertical) height = value else width = value
        }
    private var CSSStyleDeclaration.start
        get() = if (vertical) top else left
        set(value) {
            if (vertical) top = value else left = value
        }

    val forSingleRenderer = CalculationContext.Standard()

    override var columns: Int = 1
        set(value) {
            field = value
            allSubviews.forEach {
                it.element.shutdown()
            }
            allSubviews.clear()
            ready()
        }
    override var dataAndRenderer: DataAndRenderer<*> = DataAndRenderer<Int>(Constant(listOf())) { space(10.0) }
        set(value) {
            field = value
            allSubviews.forEach {
                it.element.shutdown()
            }
            allSubviews.clear()
            forSingleRenderer.cancel()
            forSingleRenderer.reactiveScope {
                data = value.data.await().toList().asIndexed()
                ready()
            }
        }
    var data: Indexed<*> = ArrayList<Any?>().asIndexed()
        set(value) {
            field = value
            allSubviews.forEach {
                if (it.index in value.min..value.max) {
                    it.visible = true
                    it.property.value = value[it.index]
                } else {
                    it.visible = false
                }
            }
        }
    var spacing: Int = 0
        set(value) {
            if (value != field) {
                field = value
                relayout()
            }
        }

    val allSubviews: ArrayList<Subview> = ArrayList()

    var viewportSize: Int = 0
        set(value) {
            field = value
            relayout()
        }
    var viewportOffset: Int = 0
        set(value) {
            field = value
            contentHolder.scrollStart = value.toDouble()
        }
    val mainStartPosition = reservedScrollingSpace / 2

    init {
        contentHolder.onscroll = { ev ->
            viewportOffset = contentHolder.scrollStart.toInt()
            populate()
            if (allSubviews.isNotEmpty()) {
                var defaultShift = true
                if (allSubviews.first().index == data.min) {
                    // shift and attach to top
                    if(allSubviews.first().startPosition > 100) {
                        shift(-allSubviews.first().startPosition)
                    }
                    defaultShift = false
                }
                if (allSubviews.last().index == data.max) {
                    capView.style.start = allSubviews.last().let { it.startPosition + it.size }.let { "${it}px" }
                    contentHolder.style.size =
                        allSubviews.last().let { it.startPosition + it.size }.let { "${it}px" }
                    defaultShift = false
                } else {
                    capView.style.start = reservedScrollingSpace.let { "${it}px" }
                    contentHolder.style.size = reservedScrollingSpace.let { "${it}px" }
                }
                if(defaultShift) {
                    if (viewportOffset > reservedScrollingSpace * 3 / 4) {
                        shift(reservedScrollingSpace / -4)
                    } else if (viewportOffset < reservedScrollingSpace / 4) {
                        shift(reservedScrollingSpace / 4)
                    }
                }
            }
            Unit
        }
        window.setTimeout({ ready() }, 1)
    }

    fun ready() {
        spacing = window.getComputedStyle(root).columnGap.removeSuffix("px").toDouble().toInt()
        populate()
    }

    inner class Subview(
        val property: Property<Any?>,
        val element: HTMLElement,
        var index: Int,
    ) {
        var startPosition: Int = 0
            set(value) {
                field = value
                element.style.start = "${value}px"
            }
        var size: Int = -1

        init {
            ResizeObserver { entries, obs ->
                val newSize = element.scrollSize
                if (size != newSize) {
                    measure()
                    relayout()
                }
            }.observe(element)
        }

        fun measure() {
            size = element.scrollSize
        }

        var visible: Boolean
            get() = element.exists
            set(value) {
                element.exists = value
            }
    }

    fun shift(by: Int) {
        viewportOffset += by
        for (view in allSubviews) {
            view.startPosition += by
        }
    }

    fun makeFirst(): Subview? {
        if(data.max < data.min) return null
        viewportOffset = reservedScrollingSpace / 2
        val nextIndex = 0
        val property = Property<Any?>(data[nextIndex])
        dataAndRenderer.renderAny(newViews, property)
        val elementInner = newViews.rootCreated!!
        val element = Subview(
            property = property,
            element = elementInner,
            index = nextIndex,
        ).also { allSubviews.add(it); contentHolder.addNView(it.element) }
        element.measure()
        element.startPosition = reservedScrollingSpace / 2
        return element
    }

    fun populate() {
        populateDown()
        populateUp()
        firstVisible.let {
            val v = allSubviews.firstOrNull()?.index ?: -1
            if(v != it.value) it.value = v
        }
        lastVisible.let {
            val v = allSubviews.lastOrNull()?.index ?: -1
            if(v != it.value) it.value = v
        }
    }

    fun populateDown() {
        var anchor = allSubviews.lastOrNull() ?: makeFirst() ?: return
        var bottom = anchor.startPosition + anchor.size
        while ((bottom < viewportSize + viewportOffset)) {
            val nextIndex = anchor.index + 1
            if (nextIndex > data.max) break
            // Get the element to place
            val element: Subview = allSubviews.first().takeIf {
                (it.startPosition + it.size < viewportOffset)
            }?.also {
                it.index = nextIndex
                it.element.withoutAnimation {
                    it.property.value = data[nextIndex]
                }
                allSubviews.removeFirst()
                allSubviews.add(it)
            } ?: run {
                val property = Property<Any?>(data[nextIndex])
                dataAndRenderer.renderAny(newViews, property)
                val element = newViews.rootCreated!!
                Subview(
                    property = property,
                    element = element,
                    index = nextIndex,
                ).also { allSubviews.add(it); contentHolder.addNView(it.element) }
            }
            element.measure()
            element.startPosition = bottom + spacing
            bottom += element.size + spacing
            anchor = element
        }
    }

    fun populateUp() {
        var anchor = allSubviews.firstOrNull() ?: makeFirst() ?: return
        var top = anchor.startPosition
        while ((top > viewportOffset)) {
            val nextIndex = anchor.index - 1
            if (nextIndex < data.min) break
            // Get the element to place
            val element: Subview = allSubviews.last().takeIf {
                it.startPosition > viewportOffset + viewportSize
            }?.also {
                it.index = nextIndex
                it.element.withoutAnimation {
                    it.property.value = data[nextIndex]
                }
                allSubviews.removeLast()
                allSubviews.add(0, it)
            } ?: run {
                val property = Property<Any?>(data[nextIndex])
                dataAndRenderer.renderAny(newViews, property)
                val element = newViews.rootCreated!!
                Subview(
                    property = property,
                    element = element,
                    index = nextIndex,
                ).also { allSubviews.add(0, it); contentHolder.addNView(it.element) }
            }
            element.measure()
            element.startPosition = top - element.size - spacing
            top -= element.size + spacing
            anchor = element
        }
    }

    val anchorPosition: Align = Align.Start
    fun relayout() {
        if (allSubviews.isEmpty()) return
        when (anchorPosition) {
            Align.Start -> relayoutDown(0)
            Align.End -> relayoutUp(allSubviews.lastIndex)
            else -> {
                val centeredIndex = allSubviews.size / 2
                relayoutUp(centeredIndex)
                relayoutDown(centeredIndex)
            }
        }
        populate()
    }

    fun relayoutDown(startingIndex: Int) {
        var bottom = allSubviews[startingIndex].let { anchor -> anchor.startPosition + anchor.size }
        for (index in (startingIndex + 1)..allSubviews.lastIndex) {
            val element = allSubviews[index]
            element.startPosition = bottom + spacing
            bottom += element.size + spacing
        }
    }

    fun relayoutUp(startingIndex: Int) {
        var top = allSubviews[startingIndex].let { anchor -> anchor.startPosition }
        for (index in (startingIndex - 1)downTo 0) {
            val element = allSubviews[index]
            element.startPosition = top - element.size - spacing
            top -= element.size + spacing
        }
    }
}


val reservedScrollingSpace = 10_000

class DataAndRenderer<T>(val data: Readable<List<T>>, val renderer: ViewWriter.(Readable<T>) -> Unit) {
    @Suppress("UNCHECKED_CAST")
    fun renderAny(writer: ViewWriter, readable: Readable<*>) {
        (renderer as ViewWriter.(Readable<Any?>) -> Unit).invoke(writer, readable)
    }
}
