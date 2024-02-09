package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.objc.UICollectionViewFlowLayout2Protocol
import com.lightningkite.rock.reactive.BasicListenable
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.*
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSIndexPath
import platform.UIKit.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NViewPager = UICollectionView

actual fun <T> ViewPager.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) = native.children(items, render)

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.viewPager(setup: ViewPager.() -> Unit) = element(
    UICollectionView(
        CGRectMake(
            0.0,
            0.0,
            0.0,
            0.0
        ), ViewPagerLayout())
) {
    calculationContext.onRemove {
        extensionStrongRef = null
    }
    backgroundColor = UIColor.clearColor
    handleTheme(this, viewDraws = false)
    extensionViewWriter = newViews()
    setup(ViewPager(this))
}

@OptIn(ExperimentalForeignApi::class)
actual val ViewPager.index: Writable<Int>
    get() = object: Writable<Int> {
        var last = native.indexPathForItemAtPoint(native.frame.useContents { CGPointMake(size.width / 2, size.height / 2) })?.row?.toInt() ?:  0

        override suspend fun set(value: Int) {
            if(value in 0..<native.numberOfItemsInSection(0L)) {
                native.scrollToItemAtIndexPath(
                    NSIndexPath.indexPathForRow(value.toLong(), 0L),
                    UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally,
                    animationsEnabled
                )
            }
        }

        override suspend fun awaitRaw(): Int {
            val current = native.indexPathForItemAtPoint(native.frame.useContents { CGPointMake(size.width / 2, size.height / 2) })?.row?.toInt() ?:  0
            last = current
            return current
        }

        private val listeners = ArrayList<() -> Unit>()
        private var parentListener: (()->Unit)? = null
        override fun addListener(listener: () -> Unit): () -> Unit {
            if(listeners.isEmpty()) {
                parentListener = (native.delegate as? GeneralCollectionDelegate<*>)?.onScroll?.addListener {
                    val current = native.indexPathForItemAtPoint(native.frame.useContents { CGPointMake(size.width / 2, size.height / 2) })?.row?.toInt() ?:  0
                    if(last != current) {
                        last = current
                        listeners.toList().forEach { it() }
                    }
                } ?: {}
            }
            listeners.add(listener)
            return  {
                val pos = listeners.indexOfFirst { it === listener }
                if(pos != -1) {
                    listeners.removeAt(pos)
                }
                if(listeners.isEmpty()) {
                    parentListener?.invoke()
                    parentListener = null
                }
            }
        }
    }

@OptIn(ExperimentalForeignApi::class)
class ViewPagerLayout(): UICollectionViewFlowLayout(), UICollectionViewFlowLayout2Protocol {

    override fun prepareLayout() {
        scrollDirection = UICollectionViewScrollDirection.UICollectionViewScrollDirectionHorizontal
        sectionInset = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
        sectionInsetReference = UICollectionViewFlowLayoutSectionInsetReference.UICollectionViewFlowLayoutSectionInsetFromSafeArea
        val collectionView = collectionView!!
        collectionView.calculationContext.onRemove(collectionView.layer.observe("bounds") {
          itemSize = collectionView.bounds.useContents { CGSizeMake(size.width, size.height) }
        })
    }

    override fun targetContentOffsetForProposedContentOffset(proposedContentOffset: CValue<CGPoint>, withScrollingVelocity: CValue<CGPoint>): CValue<CGPoint> {
        val collectionView = collectionView!!

        // Page width used for estimating and calculating paging.
        val pageWidth = itemSize.useContents { width } + minimumInteritemSpacing

        // Make an estimation of the current page position.
        val approximatePage = collectionView.contentOffset.useContents { x } / pageWidth
        // Determine the current page based on velocity.
        val currentPage = if(withScrollingVelocity.useContents { x } == 0.0) round(approximatePage) else if (withScrollingVelocity.useContents { x } < 0.0) floor(approximatePage) else ceil(approximatePage)

        // Create custom flickVelocity.
        val flickVelocity = withScrollingVelocity.useContents { x } * 0.3

        // Check how many pages the user flicked, if <= 1 then flickedPages should return 0.
        val flickedPages = if(abs(round(flickVelocity)) <= 1) 0.0 else round(flickVelocity)

        // Calculate newHorizontalOffset.
        val newHorizontalOffset = ((currentPage + flickedPages) * pageWidth) - collectionView.contentInset.useContents { left }

        return CGPointMake(newHorizontalOffset, proposedContentOffset.useContents { y })
    }
}