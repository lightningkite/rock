package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.lazyExpanding

@Routable("recycler-view-infinite-images")
object InfiniteImagesScreen : RockScreen {
    override val title: Readable<String>
        get() = super.title

    object ReturnIndexList: List<Int>{
        override val size: Int
            get() = Int.MAX_VALUE
        override fun get(index: Int): Int = index
        override fun isEmpty(): Boolean = false
        override fun iterator(): Iterator<Int> = (0..<Int.MAX_VALUE).iterator()
        override fun listIterator(): ListIterator<Int> = object: ListIterator<Int> {
            var n = -1
            override fun hasNext(): Boolean = n < Int.MAX_VALUE
            override fun hasPrevious(): Boolean = n > 0
            override fun next(): Int = ++n
            override fun nextIndex(): Int = ++n
            override fun previous(): Int = --n
            override fun previousIndex(): Int = --n
        }
        override fun listIterator(index: Int): ListIterator<Int> = object: ListIterator<Int> {
            var n = index - 1
            override fun hasNext(): Boolean = n < Int.MAX_VALUE
            override fun hasPrevious(): Boolean = n > 0
            override fun next(): Int = ++n
            override fun nextIndex(): Int = ++n
            override fun previous(): Int = --n
            override fun previousIndex(): Int = --n
        }
        override fun subList(fromIndex: Int, toIndex: Int): List<Int> = (fromIndex..<toIndex).toList()
        override fun lastIndexOf(element: Int): Int = element
        override fun indexOf(element: Int): Int = element
        override fun containsAll(elements: Collection<Int>): Boolean = true
        override fun contains(element: Int): Boolean = true
    }

    override fun ViewWriter.render() {
        recyclerView {
            columns = 4
            children(Constant(ReturnIndexList)) {
                stack {
                    sizeConstraints(height = 16.rem) - image {
                        scaleType = ImageScaleType.Crop
                        ::source { ImageRemote("https://picsum.photos/seed/${it.await()}/200/200") }
//                            source = Resources.imagesSolera
                    }
                }
            }
        }
    }
}