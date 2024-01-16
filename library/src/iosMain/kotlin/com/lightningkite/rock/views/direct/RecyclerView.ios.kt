package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.LateInitProperty
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSCoder
import platform.Foundation.NSIndexPath
import platform.UIKit.*
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.objc.object_getClass
import kotlin.experimental.ExperimentalObjCName

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = UICollectionView

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.recyclerView(setup: RecyclerView.() -> Unit): Unit = element(
    UICollectionView(
        CGRectMake(
            0.0,
            0.0,
            0.0,
            0.0
        ), run {
            val size = NSCollectionLayoutSize.sizeWithWidthDimension(
                width = NSCollectionLayoutDimension.fractionalWidthDimension(1.0),
                heightDimension = NSCollectionLayoutDimension.estimatedDimension(1000.0),
            )
            UICollectionViewCompositionalLayout(
                NSCollectionLayoutSection.sectionWithGroup(
                    NSCollectionLayoutGroup.horizontalGroupWithLayoutSize(
                        layoutSize = size,
                        subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                            layoutSize = size
                        ),
                        count = 1,
                    )
                )
            )
        })
) {
    calculationContext.onRemove {
        extensionStrongRef = null
    }
    backgroundColor = UIColor.clearColor
    extensionViewWriter = newViews()
    handleTheme(this, viewDraws = false)
    setup(RecyclerView(this))
}

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.horizontalRecyclerView(setup: RecyclerView.() -> Unit): Unit = element(
    UICollectionView(
        CGRectMake(
            0.0,
            0.0,
            0.0,
            0.0
        ), run {
            val size = NSCollectionLayoutSize.sizeWithWidthDimension(
                width = NSCollectionLayoutDimension.estimatedDimension(500.0),
                heightDimension = NSCollectionLayoutDimension.fractionalHeightDimension(1.0),
            )
            UICollectionViewCompositionalLayout(
                NSCollectionLayoutSection.sectionWithGroup(
                    NSCollectionLayoutGroup.verticalGroupWithLayoutSize(
                        layoutSize = size,
                        subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                            layoutSize = size
                        ),
                        count = 1,
                    )
                )
            ).apply {
                configuration = configuration.apply {
                    scrollDirection =
                        UICollectionViewScrollDirection.UICollectionViewScrollDirectionHorizontal
                }
            }
        })
) {
    backgroundColor = UIColor.clearColor
    extensionViewWriter = newViews()
    handleTheme(this, viewDraws = false)
    setup(RecyclerView(this))
}

@ViewDsl
actual fun ViewWriter.gridRecyclerView(setup: RecyclerView.() -> Unit): Unit = recyclerView(setup)
actual var RecyclerView.columns: Int
    get() = 1
    set(value) {
    }

@OptIn(ExperimentalObjCName::class, BetaInteropApi::class, ExperimentalForeignApi::class)
@ExportObjCClass
class ObsUICollectionViewCell<T>: UICollectionViewCell, UIViewWithSizeOverridesProtocol {
    constructor():this(CGRectMake(0.0, 0.0, 0.0, 0.0))

    @OverrideInit
    constructor(frame: CValue<CGRect>):super(frame = frame)

    @OverrideInit
    constructor(coder: NSCoder):super(coder = coder)

    init {

    }

    val data = LateInitProperty<T>()
    var ready = false
    var suppressRemeasure = false
    override fun subviewDidChangeSizing(view: UIView?) {
        if(suppressRemeasure) return
        frameLayoutSubviewDidChangeSizing(view)
        // Remeasure self
        if(lastInputHeight != -1.0) {
            val size = sizeThatFits(CGSizeMake(lastInputWidth, lastInputHeight))
            if(size.useContents { width } != lastWidth || size.useContents { height } != lastHeight) {
                lastWidth = size.useContents { width }
                lastHeight = size.useContents { height }
                generateSequence(this as UIView) { it.superview }.filterIsInstance<UICollectionView>().firstOrNull()?.collectionViewLayout?.invalidateLayout()
            }
        }
    }
    var padding: Double
        get() = extensionPadding ?: 0.0
        set(value) { extensionPadding = value }

    fun setNeedsNewMeasure() {
        lastWidth = -1.0
        lastInputWidth = -1.0
        lastHeight = -1.0
        lastInputHeight = -1.0
    }

    var lastInputWidth = -1.0
    var lastWidth = -1.0
    var lastInputHeight = -1.0
    var lastHeight = -1.0

    override fun preferredLayoutAttributesFittingAttributes(layoutAttributes: UICollectionViewLayoutAttributes): UICollectionViewLayoutAttributes {
        var widthMeasured = lastWidth
        var heightMeasured = lastHeight
        if(lastInputWidth != layoutAttributes.size.useContents { width } || lastInputHeight != layoutAttributes.size.useContents { height }) {
            val size = sizeThatFits(layoutAttributes.size)
            widthMeasured = size.useContents { width }
            heightMeasured = size.useContents { height }
//            println("Remeasured to $widthMeasured x $heightMeasured")
            lastWidth = widthMeasured
            lastHeight = heightMeasured
            lastInputWidth = layoutAttributes.size.useContents { width }
            lastInputHeight = layoutAttributes.size.useContents { height }
        } else {
//            println("Reusing $widthMeasured x $heightMeasured")
        }
        if(layoutAttributes.frame.useContents { size.width } != widthMeasured || layoutAttributes.frame.useContents { size.height } != heightMeasured) {
            layoutAttributes.frame = CGRectMake(
                layoutAttributes.frame.useContents { origin.x },
                layoutAttributes.frame.useContents { origin.y },
                widthMeasured,
                heightMeasured,
            )
        }
        return layoutAttributes
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> {
        return frameLayoutSizeThatFits(size)
    }
    override fun layoutSubviews() = frameLayoutLayoutSubviews()
    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
    val altCellRef = HashSet<UICollectionViewCell>()
    calculationContext.onRemove {
        altCellRef.forEach { it.shutdown() }
        altCellRef.clear()
    }
    val placeholders = 5
    @Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES", "RETURN_TYPE_MISMATCH_ON_INHERITANCE", "MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED")
    val source = object: NSObject(), UICollectionViewDelegateProtocol, UICollectionViewDataSourceProtocol {
        var list: List<T> = listOf()
        var loading: Boolean = false

        init {
            native.calculationContext.reactiveScope(onLoad = {
//                loading = true
//                native.reloadData()
            }) {
                list = items.await()
                loading = false
                native.reloadData()
            }
        }
        val registered = HashSet<String>()

        @Suppress("CONFLICTING_OVERLOADS", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun collectionView(collectionView: UICollectionView, cellForItemAtIndexPath: NSIndexPath): UICollectionViewCell {
            if (registered.add("main")) {
                collectionView.registerClass(object_getClass(ObsUICollectionViewCell<T>())!!, "main")
            }
            @Suppress("UNCHECKED_CAST") val cell = collectionView.dequeueReusableCellWithReuseIdentifier("main", cellForItemAtIndexPath) as ObsUICollectionViewCell<T>
            if(altCellRef.add(cell)) collectionView.calculationContext.onRemove { cell.shutdown() }
//                ?: run {
//                val vw = native.extensionViewWriter ?: throw IllegalStateException("No view writer attached")
//                vw!!.element(ObsUICollectionViewCell<T>()) {
//                    render(vw, data)
//                }
//                vw.rootCreated as? ObsUICollectionViewCell<T> ?: throw IllegalStateException("No view created")
//            }
            if(loading) {
                cell.suppressRemeasure = true
                cell.data.unset()
                cell.setNeedsNewMeasure()
                cell.suppressRemeasure = false
            } else {
                list.getOrNull(cellForItemAtIndexPath.row.toInt())?.let {
                    cell.suppressRemeasure = true
                    cell.data.value = it
                    cell.setNeedsNewMeasure()
                    cell.suppressRemeasure = false
                }
            }
            if(!cell.ready) {
                val vw = native.extensionViewWriter ?: throw IllegalStateException("No view writer attached")
                render(vw.targeting(cell), cell.data)
                cell.ready = true
            }
            return cell
        }

        override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = if(loading) placeholders.toLong() else list.size.toLong()
    }
    native.setDataSource(source)
    native.setDelegate(source)
    native.extensionStrongRef = source
}

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
   native.scrollToItemAtIndexPath(
       NSIndexPath.indexPathForRow(index.toLong(), 0L),
       when(align) {
           Align.Start -> UICollectionViewScrollPositionLeft or UICollectionViewScrollPositionTop
           Align.Center -> UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally
           Align.End -> UICollectionViewScrollPositionRight or UICollectionViewScrollPositionBottom
           else -> UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally
       },
       animate
   )
}