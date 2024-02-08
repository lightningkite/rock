package com.lightningkite.rock.views.direct

import com.lightningkite.rock.debugger
import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.dom.CSSStyleDeclaration
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div", viewDraws = false) {
        classList.add("recyclerView")
        var scrollingContainer: HTMLDivElement = this
        var sizingContainer: HTMLDivElement = this
        var contentCol: ContainingView = ContainingView(this)
        var indexcontentscroll: HTMLDivElement = this
        var indexcontent: HTMLDivElement = this
        val newViews: ViewWriter = this@recyclerView.newViews()
        style.position = "relative"
        element<HTMLDivElement>("div") {
            classList.add("contentScroll")
            scrollingContainer = this
            style.position = "absolute"
            style.left = "0"
            style.right = "0"
            style.top = "0"
            style.bottom = "0"
            style.overflowY = "scroll"
            element<HTMLDivElement>("div") {
                style.position = "relative"
                classList.add("content")
                style.maxHeight = "unset"
                style.height = "${reservedScrollingSpace}px"
                sizingContainer = this

                col {
                    contentCol = this
                    native.style.position = "absolute"
                    native.style.width = "100%"
                    native.style.top = "${reservedScrollingSpace / 2}px"
                }
            }
            scrollTop = reservedScrollingSpace / 2
        }
        element<HTMLDivElement>("div") {
            classList.add("barScroll")
            indexcontentscroll = this
            onscroll = { ev ->
                ev.preventDefault()
            }
            style.position = "absolute"
            style.right = "0px"
            style.top = "0"
            style.bottom = "0"
            style.width = "20px"
            style.overflowY = "scroll"
            element<HTMLDivElement>("div") {
                classList.add("barContent")
                indexcontent = this
                style.maxHeight = "unset"
                style.height = "${reservedScrollingSpace}px"
                style.width = "10px"
            }
        }

        this.asDynamic().__ROCK__controller = RecyclerController(
            root = this,
            scrollingContainer = scrollingContainer,
            sizingContainer = sizingContainer,
            contentCol = contentCol,
            indexcontentscroll = indexcontentscroll,
            indexcontent = indexcontent,
            newViews = newViews,
        )
        setup(RecyclerView(this))
    }
}

@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div") {
        classList.add("recyclerView")
        var scrollingContainer: HTMLDivElement = this
        var sizingContainer: HTMLDivElement = this
        var contentCol: ContainingView = ContainingView(this)
        var indexcontentscroll: HTMLDivElement = this
        var indexcontent: HTMLDivElement = this
        val newViews: ViewWriter = this@horizontalRecyclerView.newViews()
        style.position = "relative"
        element<HTMLDivElement>("div") {
            classList.add("contentScroll")
            scrollingContainer = this
            style.position = "absolute"
            style.left = "0"
            style.right = "0"
            style.top = "0"
            style.bottom = "0"
            style.overflowX = "scroll"
            element<HTMLDivElement>("div") {
                style.position = "relative"
                classList.add("content")
                style.maxWidth = "unset"
                style.width = "${reservedScrollingSpace}px"
                style.height = "100%"
                sizingContainer = this

                row {
                    contentCol = this
                    native.style.position = "absolute"
                    native.style.height = "100%"
                    native.style.left = "${reservedScrollingSpace / 2}px"
                }
            }
            scrollLeft = reservedScrollingSpace / 2
        }
        element<HTMLDivElement>("div") {
            classList.add("barScroll")
            indexcontentscroll = this
            onscroll = { ev ->
                ev.preventDefault()
            }
            style.position = "absolute"
            style.bottom = "0px"
            style.left = "0"
            style.right = "0"
            style.height = "20px"
            style.overflowX = "scroll"
            element<HTMLDivElement>("div") {
                classList.add("barContent")
                indexcontent = this
                style.maxWidth = "unset"
                style.width = "${reservedScrollingSpace}px"
                style.height = "10px"
            }
        }

        this.asDynamic().__ROCK__controller = RecyclerController(
            root = this,
            scrollingContainer = scrollingContainer,
            sizingContainer = sizingContainer,
            contentCol = contentCol,
            indexcontentscroll = indexcontentscroll,
            indexcontent = indexcontent,
            newViews = newViews,
            vertical = false
        )
        setup(RecyclerView(this))
    }
}
@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit = TODO()

actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
        native.style.setProperty("grid-template-rows", "repeat($value, auto)")
    }

actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
): Unit {
    (native.asDynamic().__ROCK__controller as RecyclerController).dataAndRenderer = DataAndRenderer(
        data = items,
        renderer = render
    )
}

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
    (native.asDynamic().__ROCK__controller as RecyclerController).jump(index, align ?: Align.Center)
}





val reservedScrollingSpace = 10_000.0

class DataAndRenderer<T>(val data: Readable<List<T>>, val renderer: ViewWriter.(Readable<T>)->Unit) {
    @Suppress("UNCHECKED_CAST")
    fun renderAny(writer: ViewWriter, readable: Readable<*>) {
        (renderer as ViewWriter.(Readable<Any?>)->Unit).invoke(writer, readable)
    }
}
class RecyclerController(
    val root: HTMLDivElement,
    var sizingContainer: HTMLDivElement,
    var scrollingContainer: HTMLDivElement,
    var indexcontentscroll: HTMLDivElement,
    var indexcontent: HTMLDivElement,
    var contentCol: ContainingView,
    val newViews: ViewWriter,
    val vertical: Boolean = true
) {
//    var beyondEdge: Double = 0.0
    var beyondEdge: Double = 200.0

    private var NView.scrollStart
        get() = if(vertical) scrollTop else scrollLeft
        set(value) { if(vertical) scrollTop = value else scrollLeft = value }
    private val NView.clientSize
        get() = if(vertical) clientHeight else clientWidth
    private val NView.scrollSize
        get() = if(vertical) scrollHeight else scrollWidth
    private val DOMRect.size
        get() = if(vertical) height else width
    private val DOMRect.start
        get() = if(vertical) top else left
    private val DOMRect.end
        get() = if(vertical) bottom else right
    private val CSSStyleDeclaration.marginStart
        get() = if(vertical) marginTop else marginLeft
    private val CSSStyleDeclaration.marginEnd
        get() = if(vertical) marginBottom else marginRight
    private var CSSStyleDeclaration.size
        get() = if(vertical) height else width
        set(value) { if (vertical) height = value else width = value }
    private var CSSStyleDeclaration.start
        get() = if(vertical) top else left
        set(value) { if (vertical) top = value else left = value }


    var dataCopy = Property<List<*>>(listOf<Any?>())
    var firstIndex = 0
    var lastIndex = -1
    var minIndex = -40
    var maxIndex = 40
    var suppress = false
    var lastDefaultPos = reservedScrollingSpace / 2
    val forSingleRenderer = CalculationContext.Standard()
    var dataAndRenderer: DataAndRenderer<*> = DataAndRenderer<Int>(Constant(listOf())) { space(10.0) }
        set(value) {
            suppress = true
            field = value
            // Gotta dump the whole thing now, reset ALL the state
            contentCol.native.clearNViews()
            lastDefaultPos = reservedScrollingSpace / 2
            firstIndex = 0
            lastIndex = -1
            minIndex = 0
            maxIndex = 0
            dataCopy.value = emptyList<Unit>()
            forSingleRenderer.cancel()
            suppress = false
            forSingleRenderer.reactiveScope {
                scrollingContainer.scrollStart = lastDefaultPos
                val list = value.data.await()
                minIndex = 0
                maxIndex = list.size - 1
                dataCopy.value = list
                correctScrollBoundaries()
            }
        }

    init {
        root.calculationContext.onRemove {
            reserved.forEach { it.shutdown() }
        }

        ResizeObserver { entries, obs ->
            scrollHandler()
        }.observe(contentCol.native)
        scrollingContainer.onscroll = { ev ->
            scrollHandler()
            Unit
        }
        indexcontentscroll.onscroll = onscroll@{ ev ->
            if(fakeScrollSuppress) {
                fakeScrollSuppress = false
                return@onscroll Unit
            }
            val index = ((indexcontentscroll.scrollStart / (reservedScrollingSpace - indexcontentscroll.clientSize)) * (maxIndex - minIndex)).roundToInt() + minIndex
            jump(
                index = index
            )
        }
    }

    var fakeScrollSuppress = false
    fun updateFakeScroll() {
        fakeScrollSuppress = true
        val index = (firstIndex + lastIndex) / 2.0
        val scrollStart = ((index - minIndex) / (maxIndex - minIndex) * (reservedScrollingSpace - indexcontentscroll.clientSize))
        indexcontentscroll.scrollStart = scrollStart
    }

    fun jump(index: Int, align: Align = Align.Center) {
        val shift = when(align) {
            Align.Start -> {
                scrollingContainer.scrollStart = lastDefaultPos
                index - firstIndex
            }
            Align.End -> {
                val visibleViews = (lastIndex - firstIndex + 1).coerceAtLeast(1)
                scrollingContainer.scrollStart = lastDefaultPos + (root.getBoundingClientRect().size) / visibleViews + beyondEdge * 2
                index - lastIndex
            }
            else -> {
                val visibleViews = (lastIndex - firstIndex + 1).coerceAtLeast(1)
                scrollingContainer.scrollStart = lastDefaultPos + (root.getBoundingClientRect().size) / visibleViews / 2 + beyondEdge
                index - (firstIndex + lastIndex) / 2
            }
        }.coerceIn(minIndex - firstIndex, maxIndex - lastIndex)

        if(shift != 0) {
            root.withoutAnimation {
                for (view in contentCol.native.listNViews()) {
                    val p = view.asDynamic().__ROCK__property as? Property<Int> ?: continue
                    val newValue = p.value + shift
                    if(newValue < minIndex || newValue > maxIndex) contentCol.native.removeChild(view)
                    else p.value = newValue
                }
            }
            firstIndex = (firstIndex + shift).coerceAtLeast(minIndex)
            lastIndex = (lastIndex + shift).coerceAtMost(maxIndex)
            updateFakeScroll()
        }
        scrollHandler()
    }

    fun correctScrollBoundaries() {
        // If this runs twice at the beginning then the scroll offset gets messed up
        // Correct for being out-of-bounds
        val shift = if(lastIndex > maxIndex) {
            maxIndex - lastIndex
        } else if(firstIndex < minIndex) {
            minIndex - firstIndex
        } else {
            0
        }
        if(shift != 0) {
            root.withoutAnimation {
                for (view in contentCol.native.listNViews()) {
                    val p = view.asDynamic().__ROCK__property as? Property<Int> ?: continue
                    val newValue = p.value + shift
                    if(newValue < minIndex || newValue > maxIndex) contentCol.native.removeChild(view)
                    else p.value = newValue
                }
            }
            firstIndex = (firstIndex + shift).coerceAtLeast(minIndex)
            lastIndex = (lastIndex + shift).coerceAtMost(maxIndex)
            updateFakeScroll()
        }
        scrollHandler()
    }

    var block = false
    val reserved = ArrayList<HTMLElement>()
    fun scrollHandler() = with(scrollingContainer) {
//        if(block) {
//            println("Blocked for $scrollStart")
//            return
//        }
        if (suppress) {
            return
        }
        suppress = true
        val outerBounds = root.getBoundingClientRect()
        val children = contentCol.native.children

//        println("Processing $scrollStart")

        // Handle huge scroll
//        if (abs(scrollStart - lastDefaultPos) > (outerBounds.size + beyondEdge * 2) * 3 / 4) {
//            scrollStart = scrollStart.coerceIn(
//                lastDefaultPos - (outerBounds.size + beyondEdge * 2) * 3 / 4,
//                lastDefaultPos + (outerBounds.size + beyondEdge * 2) * 3 / 4,
//            )
//        }
        val beforeScroll = scrollStart

        var scrollElements = 0
        var scrollAmount = 0.0
        val canRecycle = ArrayList<NView>()

        // Handle top falloff first
        if (lastIndex != maxIndex) {
            for (index in 0..<children.length) {
                val child = children.get(index) as? org.w3c.dom.HTMLElement ?: continue
                val bounds = child.getBoundingClientRect()
                val style = window.getComputedStyle(child)
                val margin = style.marginStart.removeSuffix("px").toDouble()
                if (bounds.end + margin < (outerBounds.start - beyondEdge)) {
                    // We're scrolling down and this fell out the top
                    scrollElements += 1
                    scrollAmount += bounds.size
                    scrollAmount += style.marginStart.removeSuffix("px").toInt()
                    scrollAmount += style.marginEnd.removeSuffix("px").toInt()
                    canRecycle.add(child)
//                    println("Removing view from top because ${bounds.end} + ${margin} < ${outerBounds.start}")
//                    println("Scrolling ${scrollAmount}")
                    firstIndex += 1
                    block = firstIndex == 2
                } else {
                    break
                }
            }
        }

        // Handle bottom falloff
        for (index in children.length - 1 downTo 0) {
            val child = children.get(index) as? org.w3c.dom.HTMLElement ?: continue
            val bounds = child.getBoundingClientRect()
            val style = window.getComputedStyle(child)
            val margin = style.marginStart.removeSuffix("px").toDouble()
            if (bounds.start - margin > (outerBounds.end + beyondEdge) ) {
                // We're scrolling up and this fell out the bottom
                canRecycle.add(child)
//                println("Removing view from bottom because ${bounds.start} - ${margin} > ${outerBounds.end}")
                lastIndex -= 1
            } else {
                break
            }
        }
        // Handle top needs content
        var neededOnTop = (children.get(0) as? org.w3c.dom.HTMLElement)?.let { child ->
            val bounds = child.getBoundingClientRect()
            val style = window.getComputedStyle(child)
            val margin = style.marginStart.removeSuffix("px").toDouble()
//            println("bounds.start - margin - outerBounds.start: ${bounds.start} - $margin - ${outerBounds.start}")
            (bounds.start - margin - (outerBounds.start - beyondEdge)).coerceAtLeast(0.0)
        } ?: 0.0
        // Handle bottom needs content
        var neededOnBottom = (contentCol.native.lastElementChild as? org.w3c.dom.HTMLElement)?.let { child ->
            val bounds = child.getBoundingClientRect()
            val style = window.getComputedStyle(child)
            val margin = style.marginEnd.removeSuffix("px").toDouble()
//            println("outerBounds.end - bounds.end - margin: ${outerBounds.end} - ${bounds.end} - $margin")
            ((outerBounds.end + beyondEdge) - bounds.end - margin).coerceAtLeast(0.0)
        } ?: (outerBounds.size + beyondEdge * 2)

        // Add new elements
        for (r in canRecycle) contentCol.native.removeChild(r)
        reserved.addAll(canRecycle)

        // Append views at the bottom until the needed space is filled
        while (neededOnBottom > 1.0 && lastIndex < maxIndex) {
//            println("Appending view on bottom $neededOnBottom")
            lastIndex += 1
            val newElement = if (reserved.isNotEmpty()) {
                reserved.removeAt(reserved.lastIndex).also {
                    root.withoutAnimation {
                        (it.asDynamic().__ROCK__property as Property<Int>).value = lastIndex
                    }
                }
            } else {
                with(newViews) {
                    val p = Property(lastIndex)
                    root.withoutAnimation {
                        dataAndRenderer.renderAny(newViews, shared { dataCopy.await()[p.await()] })
                    }
                    rootCreated!!.also {
                        it.asDynamic().__ROCK__property = p
                    }
                }
            }
//            println("Appending view")
            contentCol.native.appendChild(newElement)
            neededOnBottom -= newElement.scrollSize
        }

        // Insert views on top until the needed space is filled
        while (neededOnTop > 1.0 && firstIndex > minIndex) {
//            println("Inserting view on top $neededOnTop")
            firstIndex -= 1
            val newElement = if (reserved.isNotEmpty()) {
                reserved.removeAt(reserved.lastIndex).also {
                    root.withoutAnimation {
                        (it.asDynamic().__ROCK__property as Property<Int>).value = firstIndex
                    }
                }
            } else {
                with(newViews) {
                    val p = Property(firstIndex)
                    root.withoutAnimation {
                        dataAndRenderer.renderAny(newViews, shared { dataCopy.await()[p.await()] })
                    }
                    rootCreated!!.also {
                        it.asDynamic().__ROCK__property = p
                    }
                }
            }
            contentCol.native.insertBefore(newElement, contentCol.native.firstChild)
            val style = window.getComputedStyle(newElement)
            scrollAmount -= newElement.scrollSize
            scrollAmount -= style.marginStart.removeSuffix("px").toInt()
            scrollAmount -= style.marginEnd.removeSuffix("px").toInt()
            neededOnTop -= newElement.scrollSize
        }

        // Handle scroll edges; we do this by altering the container size.
        // You can't just set scrollTop; it will have odd effects when pushing against the scroll edge
//        println("Before edge processing: $scrollStart")
        lastDefaultPos = if (firstIndex == minIndex && lastIndex == maxIndex) {
            // cap both; go straight to native style
//            println("cap both; go straight to native style")
            sizingContainer.style.size = "max-content"
            sizingContainer.style.position = "unset"
            contentCol.native.style.start = "-${0}px"
            contentCol.native.style.position = "unset"
            val newDefaultPos = 0.0
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        } else if (firstIndex == minIndex) {
            // cap top
//            println("cap top")
            sizingContainer.style.size = "${reservedScrollingSpace / 2}px"
            sizingContainer.style.position = "relative"
            contentCol.native.style.start = "-${0}px"
            contentCol.native.style.position = "absolute"
            val newDefaultPos = 0.0
//            if(newDefaultPos != lastDefaultPos) scrollAmount += beyondEdge * 2
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        } else if (lastIndex == maxIndex) {
            // cap bottom
//            println("cap bottom")
            val h = contentCol.native.scrollSize
            sizingContainer.style.size = "${reservedScrollingSpace / 2 + h}px"
            sizingContainer.style.position = "relative"
            contentCol.native.style.start = "${reservedScrollingSpace / 2 - 0}px"
            contentCol.native.style.position = "absolute"
            val newDefaultPos = reservedScrollingSpace / 2
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        } else {
            // uncap
//            println("uncap")
            sizingContainer.style.size = "${reservedScrollingSpace}px"
            sizingContainer.style.position = "relative"
            contentCol.native.style.start = "${reservedScrollingSpace / 2 - 0}px"
            contentCol.native.style.position = "absolute"
            val newDefaultPos = reservedScrollingSpace / 2
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        }
//        println("After edge processing: $scrollStart")

        // Adjust offset
        if (scrollAmount != 0.0) {
//            println("Scrolling ${scrollAmount} total from $beforeScroll to position ${beforeScroll - scrollAmount}")
            scrollStart = beforeScroll - scrollAmount
            updateFakeScroll()
        }
        suppress = false
//                suppressNext = true
    }
}