package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.objc.UICollectionViewFlowLayout2Protocol
import com.lightningkite.rock.reactive.*
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
actual inline fun ViewWriter.viewPagerActual(crossinline setup: ViewPager.() -> Unit) = element(
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
    get() = ((native.delegate as? GeneralCollectionDelegate<*>)?.centerIndex ?: Constant(0))
        .withWrite { value ->
            if(value in 0..<native.numberOfItemsInSection(0L)) {
                native.scrollToItemAtIndexPath(
                    NSIndexPath.indexPathForRow(value.toLong(), 0L),
                    UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally,
                    animationsEnabled
                )
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