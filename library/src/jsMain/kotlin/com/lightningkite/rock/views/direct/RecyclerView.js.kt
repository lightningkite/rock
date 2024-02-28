package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.dom.CSSStyleDeclaration
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = HTMLDivElement

@ViewDsl
actual fun ViewWriter.recyclerViewActual(setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div", viewDraws = false) {
        classList.add("recyclerView")
        var scrollingContainer: HTMLDivElement = this
        var sizingContainer: HTMLDivElement = this
        var contentCol: ContainingView = ContainingView(this)
        var indexcontentscroll: HTMLDivElement = this
        var indexcontent: HTMLDivElement = this
        val newViews: ViewWriter = this@recyclerViewActual.newViews()
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
actual fun ViewWriter.horizontalRecyclerViewActual(setup: RecyclerView.() -> Unit): Unit {
    themedElement<HTMLDivElement>("div") {
        classList.add("recyclerView")
        var scrollingContainer: HTMLDivElement = this
        var sizingContainer: HTMLDivElement = this
        var contentCol: ContainingView = ContainingView(this)
        var indexcontentscroll: HTMLDivElement = this
        var indexcontent: HTMLDivElement = this
        val newViews: ViewWriter = this@horizontalRecyclerViewActual.newViews()
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

actual var RecyclerView.columns: Int
    get() = (native.asDynamic().__ROCK__controller as RecyclerController).columns
    set(value) {
        (native.asDynamic().__ROCK__controller as RecyclerController).columns = value
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

actual val RecyclerView.firstVisibleIndex: Readable<Int>
    get() = (native.asDynamic().__ROCK__controller as RecyclerController).firstVisible

actual val RecyclerView.lastVisibleIndex: Readable<Int>
    get() = (native.asDynamic().__ROCK__controller as RecyclerController).lastVisible




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
    var columns = 1
        set(value) {
//            println("Changing to $value columns")
            suppress = true
            field = value
//            println("Clearing native views")
            contentCol.native.clearNViews()
//            println("Cleared native views")
            scrollingContainer.scrollStart = lastDefaultPos
            firstIndex = 0
            lastIndex = -value
            reserved.clear()
            suppress = false
//            println("Existing views should be empty, but is ${contentCol.native.listNViews().size}")
//            println("Rerunning scrolling")
            correctScrollBoundaries()
        }
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
    val firstVisible = Property(0)
    val lastVisible = Property(0)
    var firstIndex = 0
    var lastIndex = -1
    val minIndexProperty = Property(-40)
    var minIndex by minIndexProperty
    val maxIndexProperty = Property(40)
    var maxIndex by maxIndexProperty
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
            lastIndex = -columns
            minIndex = 0
            maxIndex = 0
            dataCopy.value = emptyList<Unit>()
            reserved.clear()
            forSingleRenderer.cancel()
            suppress = false
            forSingleRenderer.reactiveScope {
                suppress = true
                scrollingContainer.scrollStart = lastDefaultPos
                val list = value.data.await().toList()
                minIndex = 0
                maxIndex = list.size - 1
                dataCopy.value = list
                suppress = false
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
                val visibleRows = (lastIndex - firstIndex + 1).div(columns).coerceAtLeast(1)
                scrollingContainer.scrollStart = lastDefaultPos + (root.getBoundingClientRect().size) / visibleRows + beyondEdge * 2
                index - lastIndex
            }
            else -> {
                val visibleRows = (lastIndex - firstIndex + 1).div(columns).coerceAtLeast(1)
                scrollingContainer.scrollStart = lastDefaultPos + (root.getBoundingClientRect().size) / visibleRows / 2 + beyondEdge
                index - (firstIndex + lastIndex) / 2
            }
        }

        if(shift != 0) {
            root.withoutAnimation {
                for (view in contentCol.native.listNViews()) {
                    if(columns == 1) {
                        val p = view.asDynamic().__ROCK__property as? Property<Int> ?: continue
                        val newValue = p.value + shift
                        if (newValue < minIndex || newValue > maxIndex) contentCol.native.removeChild(view)
                        else p.value = newValue
                    } else {
                        val ps = view.asDynamic().__ROCK__properties as? Array<Property<Int>> ?: continue
                        for(p in ps) {
                            p.value = p.value + shift
                        }
                    }
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
//            println("SHIFT FIX ($maxIndex - $lastIndex).div($columns).times($columns)")
            (maxIndex - lastIndex).div(columns).times(columns)
        } else if(firstIndex < minIndex) {
//            println("SHIFT FIX ($minIndex - $firstIndex).div($columns).times($columns)")
            (minIndex - firstIndex).div(columns).times(columns)
        } else {
//            println("SHIFT FIX zero")
            0
        }
        if(shift != 0) {
            root.withoutAnimation {
                for (view in contentCol.native.listNViews()) {
                    if(columns == 1) {
                        val p = view.asDynamic().__ROCK__property as? Property<Int> ?: continue
                        val newValue = p.value + shift
//                        println("shift ${p.value} -> $newValue")
                        if(newValue < minIndex || newValue > maxIndex) {
//                            println("Removed a view in shift")
                            contentCol.native.removeChild(view)
                        }
                        else p.value = newValue
                    } else {
                        val ps = view.asDynamic().__ROCK__properties as? Array<Property<Int>> ?: continue
                        var min = 100000000
                        var max = -100000000
                        for(p in ps) {
                            p.value = p.value + shift
                            min = min(min, p.value)
                            max = max(max, p.value)
                        }
                        if(max < minIndex || min > maxIndex) {
//                            println("Removed a view in shift")
                            contentCol.native.removeChild(view)
                        }
                    }
                }
            }
            firstIndex = (firstIndex + shift).coerceAtLeast(minIndex.div(columns).times(columns))
            lastIndex = (lastIndex + shift).coerceAtMost(maxIndex.plus(columns - 1).div(columns).times(columns))
            updateFakeScroll()
        }
        scrollHandler()
    }

    val reserved = ArrayList<HTMLElement>()
    fun scrollHandler() = with(scrollingContainer) {
        if (suppress) {
            return Unit
        }
        suppress = true
        val outerBounds = root.getBoundingClientRect()
        val children = contentCol.native.children

        // Handle huge scroll
        if (abs(scrollStart - lastDefaultPos) > (outerBounds.size + beyondEdge * 2) * 3 / 4) {
            scrollStart = scrollStart.coerceIn(
                lastDefaultPos - (outerBounds.size + beyondEdge * 2) * 3 / 4,
                lastDefaultPos + (outerBounds.size + beyondEdge * 2) * 3 / 4,
            )
//            println("Capping scroll")
        }
        val beforeScroll = scrollStart

        var scrollAmount = 0.0
        val canRecycle = ArrayList<NView>()

        // Handle top falloff first
        val extra = 10
        if (lastIndex != maxIndex) {
            for (index in 0..<children.length) {
                val child = children.get(index) as? org.w3c.dom.HTMLElement ?: continue
                val bounds = child.getBoundingClientRect()
                val style = window.getComputedStyle(child)
                val margin = style.marginStart.removeSuffix("px").toDoubleOrNull() ?: 0.0
                if (bounds.end + margin < (outerBounds.start - beyondEdge - extra)) {
                    // We're scrolling down and this fell out the top
                    scrollAmount += bounds.size
                    scrollAmount += style.marginStart.removeSuffix("px").toInt()
                    scrollAmount += style.marginEnd.removeSuffix("px").toInt()
                    canRecycle.add(child)
//                    println("Removing view from top because ${bounds.end} + ${margin} < ${outerBounds.start}")
////                    println("Scrolling ${scrollAmount}")
                    firstIndex += columns
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
            val margin = style.marginStart.removeSuffix("px").toDoubleOrNull() ?: 0.0
            if (bounds.start - margin > (outerBounds.end + beyondEdge + extra) ) {
                // We're scrolling up and this fell out the bottom
                canRecycle.add(child)
//                println("Removing view from bottom because ${bounds.start} - ${margin} > ${outerBounds.end}")
                lastIndex -= columns
            } else {
                break
            }
        }

//        println("Processing $scrollStart - $minIndex..$maxIndex cap, viewing $firstIndex..$lastIndex")

        fun makeElement(index: Int): HTMLElement {
            return if (reserved.isNotEmpty()) {
                reserved.removeAt(reserved.lastIndex).also {
                    if(columns == 1) {
                        root.withoutAnimation {
                            (it.asDynamic().__ROCK__property as Property<Int>).value = index
                        }
                    } else {
                        root.withoutAnimation {
                            (it.asDynamic().__ROCK__properties as Array<Property<Int>>).forEachIndexed { offset, p ->
                                p.value = index + offset
                            }
                        }
                    }
                }
            } else {
                if(columns == 1) {
                    with(newViews) {
                        val p = Property(index)
                        root.withoutAnimation {
                            dataAndRenderer.renderAny(newViews, shared { dataCopy.await()[p.await().coerceIn(minIndex, maxIndex)] })
                        }
                        rootCreated!!.also {
                            it.asDynamic().__ROCK__property = p
                        }
                    }
                } else {
                    with(newViews) {
                        val properties = Array(columns) { offset -> Property(index + offset) }
                        root.withoutAnimation {
                            if(vertical) {
                                row {
                                    repeat(columns) { offset ->
                                        val p = properties[offset]
                                        beforeNextElementSetup {
                                            style.flexGrow = "1"
                                            style.flexShrink = "1"
                                            style.flexBasis = "0"
                                            this.calculationContext.reactiveScope {
                                                style.visibility =
                                                    if (p.await() in minIndexProperty.await()..maxIndexProperty.await()) "visible" else "hidden"
                                            }
                                        }
                                        dataAndRenderer.renderAny(
                                            newViews,
                                            shared { dataCopy.await()[p.await().coerceIn(minIndex, maxIndex)] })
                                    }
                                }
                            } else {
                                col {
                                    repeat(columns) { offset ->
                                        val p = properties[offset]
                                        beforeNextElementSetup {
                                            style.flexGrow = "1"
                                            style.flexShrink = "1"
                                            style.flexBasis = "0"
                                            this.calculationContext.reactiveScope {
                                                style.visibility =
                                                    if (p.await() in minIndexProperty.await()..maxIndexProperty.await()) "visible" else "hidden"
                                            }
                                        }
                                        dataAndRenderer.renderAny(
                                            newViews,
                                            shared { dataCopy.await()[p.await().coerceIn(minIndex, maxIndex)] })
                                    }
                                }
                            }
                        }
                        rootCreated!!.also {
                            it.asDynamic().__ROCK__properties = properties
                        }
                    }
                }
            }
        }


        // Handle top needs content
        val neededOnTop = (children.get(0) as? org.w3c.dom.HTMLElement)?.let { child ->
            val bounds = child.getBoundingClientRect()
            val style = window.getComputedStyle(child)
            val margin = style.marginStart.removeSuffix("px").toDoubleOrNull() ?: 0.0
////            println("bounds.start - margin - outerBounds.start: ${bounds.start} - $margin - ${outerBounds.start}")
            (bounds.start - margin - (outerBounds.start - beyondEdge)).coerceAtLeast(0.0)
        } ?: 0.0
        // Handle bottom needs content
        val neededOnBottom = (contentCol.native.lastElementChild as? org.w3c.dom.HTMLElement)?.let { child ->
            val bounds = child.getBoundingClientRect()
            val style = window.getComputedStyle(child)
            val margin = style.marginEnd.removeSuffix("px").toDoubleOrNull() ?: 0.0
////            println("outerBounds.end - bounds.end - margin: ${outerBounds.end} - ${bounds.end} - $margin")
            ((outerBounds.end + beyondEdge) - bounds.end - margin).coerceAtLeast(0.0)
        } ?: (outerBounds.size + beyondEdge * 2)

        // Add new elements
        for (r in canRecycle) contentCol.native.removeChild(r)
        reserved.addAll(canRecycle)

        // Append views at the bottom until the needed space is filled
        val startHeight1 = contentCol.native.getBoundingClientRect().size
        while (neededOnBottom > contentCol.native.getBoundingClientRect().size - startHeight1 && lastIndex + columns <= maxIndex) {
            lastIndex += columns
            val newElement = makeElement(lastIndex)
            contentCol.native.appendChild(newElement)
        }

        // Insert views on top until the needed space is filled
        val startHeight2 = contentCol.native.getBoundingClientRect().size
        while (neededOnTop > contentCol.native.getBoundingClientRect().size - startHeight2 && firstIndex - columns >= minIndex) {
            firstIndex -= columns
            val newElement = makeElement(firstIndex)
            contentCol.native.insertBefore(newElement, contentCol.native.firstChild)
        }
        scrollAmount -= contentCol.native.getBoundingClientRect().size - startHeight2

        // Handle scroll edges; we do this by altering the container size.
        // You can't just set scrollTop; it will have odd effects when pushing against the scroll edge
////        println("Before edge processing: $scrollStart")
        lastDefaultPos = if (firstIndex <= minIndex && lastIndex >= maxIndex) {
            // cap both; go straight to native style
////            println("cap both; go straight to native style")
            sizingContainer.style.size = "max-content"
            sizingContainer.style.position = "unset"
            contentCol.native.style.start = "-${0}px"
            contentCol.native.style.position = "unset"
            val newDefaultPos = 0.0
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        } else if (firstIndex <= minIndex) {
            // cap top
////            println("cap top")
            sizingContainer.style.size = "${reservedScrollingSpace / 2}px"
            sizingContainer.style.position = "relative"
            contentCol.native.style.start = "-${0}px"
            contentCol.native.style.position = "absolute"
            val newDefaultPos = 0.0
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        } else if (lastIndex >= maxIndex) {
            // cap bottom
////            println("cap bottom")
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
////            println("uncap")
            sizingContainer.style.size = "${reservedScrollingSpace}px"
            sizingContainer.style.position = "relative"
            contentCol.native.style.start = "${reservedScrollingSpace / 2 - 0}px"
            contentCol.native.style.position = "absolute"
            val newDefaultPos = reservedScrollingSpace / 2
            scrollAmount -= newDefaultPos - lastDefaultPos
            newDefaultPos
        }
////        println("After edge processing: $scrollStart")


        // Adjust offset
        if (scrollAmount != 0.0) {
//            println("Scrolling ${scrollAmount} total from $beforeScroll to position ${beforeScroll - scrollAmount}")
            scrollStart = beforeScroll - scrollAmount
            updateFakeScroll()
        }
        suppress = false
//                suppressNext = true

        (0..<children.length).firstOrNull { index ->
            val child = children.get(index) as? org.w3c.dom.HTMLElement ?: return@firstOrNull false
            val bounds = child.getBoundingClientRect()
            bounds.start >= outerBounds.start
        }?.times(columns)?.plus(firstIndex)?.let {
            if(it != firstVisible.value) firstVisible.value = it
        }
        (children.length-1 downTo 0).firstOrNull { index ->
            val child = children.get(index) as? org.w3c.dom.HTMLElement ?: return@firstOrNull false
            val bounds = child.getBoundingClientRect()
            bounds.end <= outerBounds.end
        }?.times(columns)?.plus(columns-1)?.plus(firstIndex)?.let {
            if(it != lastVisible.value) lastVisible.value = it
        }
    }
}
