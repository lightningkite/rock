package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.objc.UIViewWithSizeOverridesProtocol
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.useContents
import platform.CoreGraphics.*
import platform.Foundation.NSCoder
import platform.Foundation.NSIndexPath
import platform.QuartzCore.CATextLayer
import platform.UIKit.*
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.objc.object_getClass
import kotlin.experimental.ExperimentalObjCName
import kotlin.math.max

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRecyclerView = UICollectionView

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.recyclerViewActual(setup: RecyclerView.() -> Unit): Unit = element(
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
    handleTheme(this, viewDraws = false)
    extensionViewWriter = newViews()
    setup(RecyclerView(this))
}

@OptIn(ExperimentalForeignApi::class)
@ViewDsl
actual fun ViewWriter.horizontalRecyclerViewActual(setup: RecyclerView.() -> Unit): Unit = element(
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
    handleTheme(this, viewDraws = false)
    extensionViewWriter = newViews()
    setup(RecyclerView(this))
}

actual var RecyclerView.columns: Int
    get() = 1  // TODO
    set(value) {
        val existingConfig = (native.collectionViewLayout as? UICollectionViewCompositionalLayout)?.configuration
        if(existingConfig?.scrollDirection == UICollectionViewScrollDirection.UICollectionViewScrollDirectionHorizontal) {
            val size = NSCollectionLayoutSize.sizeWithWidthDimension(
                width = NSCollectionLayoutDimension.estimatedDimension(500.0),
                heightDimension = NSCollectionLayoutDimension.fractionalHeightDimension(1.0),
            )
            native.collectionViewLayout = UICollectionViewCompositionalLayout(
                NSCollectionLayoutSection.sectionWithGroup(
                    NSCollectionLayoutGroup.verticalGroupWithLayoutSize(
                        layoutSize = size,
                        subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                            layoutSize = size
                        ),
                        count = value.toLong(),
                    )
                )
            ).apply {
                existingConfig?.let { configuration = it }
            }
        } else {
            val size = NSCollectionLayoutSize.sizeWithWidthDimension(
                width = NSCollectionLayoutDimension.fractionalWidthDimension(1.0),
                heightDimension = NSCollectionLayoutDimension.estimatedDimension(1000.0),
            )
            native.collectionViewLayout = UICollectionViewCompositionalLayout(
                NSCollectionLayoutSection.sectionWithGroup(
                    NSCollectionLayoutGroup.horizontalGroupWithLayoutSize(
                        layoutSize = size,
                        subitem = NSCollectionLayoutItem.itemWithLayoutSize(
                            layoutSize = size
                        ),
                        count = value.toLong(),
                    )
                )
            ).apply {
                existingConfig?.let { configuration = it }
            }
        }
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

    var debugDescriptionInfo: String = ""
    var debugDescriptionInfo2: String = ""
    override fun debugDescription(): String? = "${super.debugDescription()} $debugDescriptionInfo $debugDescriptionInfo2"

//    var lockWidth = false
//    var lockHeight = false

    val data = LateInitProperty<T>()
    var ready = false
    var suppressRemeasure = false
    private val sizeCache: MutableMap<Size, List<Size>> = HashMap()
    override fun sizeThatFits(size: CValue<CGSize>): CValue<CGSize> = frameLayoutSizeThatFits(size, sizeCache)
    override fun layoutSubviews() = frameLayoutLayoutSubviews(sizeCache)
    override fun subviewDidChangeSizing(view: UIView?) {
        if(suppressRemeasure) return
        frameLayoutSubviewDidChangeSizing(view, sizeCache)
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
        var before = layoutAttributes.size.useContents { "${width.toInt()} x ${height.toInt()}" }
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
        debugDescriptionInfo = before + " -> " + layoutAttributes.size.useContents { "${width.toInt()} x ${height.toInt()} " }
        return layoutAttributes
    }


    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        return super.hitTest(point, withEvent).takeUnless { it == this }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun <T> UICollectionView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) {
    val altCellRef = HashSet<UICollectionViewCell>()
    calculationContext.onRemove {
        altCellRef.forEach { it.shutdown() }
        altCellRef.clear()
    }
    val placeholders = 5
    val source = GeneralCollectionDelegate(altCellRef, render, placeholders, extensionViewWriter!!)
    calculationContext.reactiveScope(onLoad = {
        source.loading = true
        reloadData()
    }) {
        source.list = items.await()
        source.loading = false
        reloadData()
    }
    setDataSource(source)
    setDelegate(source)
    extensionStrongRef = source
}

@Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES", "RETURN_TYPE_MISMATCH_ON_INHERITANCE", "MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED")
class GeneralCollectionDelegate<T>(
    private val altCellRef: HashSet<UICollectionViewCell>,
    private val render: ViewWriter.(value: Readable<T>) -> Unit,
    private val placeholders: Int,
    private val viewWriter: ViewWriter,
) : NSObject(), UICollectionViewDelegateProtocol, UICollectionViewDataSourceProtocol {
    var list: List<T> = listOf()
    var loading: Boolean = false
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
        collectionView.withoutAnimation {
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
                val vw = viewWriter
                cell.suppressRemeasure = true
                render(vw.targeting(cell), cell.data)
                cell.suppressRemeasure = false
                cell.ready = true
            }
        }
        return cell
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun scrollViewDidScroll(scrollView: UIScrollView) {
        val native = scrollView as UICollectionView
        native.bounds.useContents {
            val outerBounds = this
            (native.indexPathsForVisibleItems as List<NSIndexPath>).filter {
                native.cellForItemAtIndexPath(it)?.frame?.useContents {
                    this.origin.x >= outerBounds.origin.x &&
                    this.origin.y >= outerBounds.origin.y &&
                    this.origin.x + this.size.width <= outerBounds.origin.x + outerBounds.size.width &&
                    this.origin.y + this.size.height <= outerBounds.origin.y + outerBounds.size.height
                } == true
            }.rangeOfOrNull { it.row.toInt() }?.let {
                it.start.let { if(firstVisibleIndex.value != it) firstVisibleIndex.value = it }
                it.endInclusive.let { if(lastVisibleIndex.value != it) lastVisibleIndex.value = it }
            }
        }
        native.indexPathForItemAtPoint(native.frame.useContents { CGPointMake(size.width / 2, size.height / 2) })?.row?.toInt()?.let {
            if(centerIndex.value != it) centerIndex.value = it
        }
    }

    override fun collectionView(collectionView: UICollectionView, numberOfItemsInSection: NSInteger): NSInteger = if(loading) placeholders.toLong() else list.size.toLong()

    val firstVisibleIndex = Property(0)
    val centerIndex = Property(0)
    val lastVisibleIndex = Property(0)
}

inline fun <S, T: Comparable<T>> Iterable<S>.rangeOfOrNull(calculate: (S)->T): ClosedRange<T>? {
    var min: T? = null
    var max: T? = null
    for(item in this) {
        val value = calculate(item)
        if(min == null || value < min) min = value
        if(max == null || value > max) max = value
    }
    if(min == null || max == null) return null
    else return min..max
}

actual fun <T> RecyclerView.children(
    items: Readable<List<T>>,
    render: ViewWriter.(value: Readable<T>) -> Unit
) = native.children(items, render)

actual fun RecyclerView.scrollToIndex(
    index: Int,
    align: Align?,
    animate: Boolean
) {
    if(index in 0..<native.numberOfItemsInSection(0L)) {
        native.scrollToItemAtIndexPath(
            NSIndexPath.indexPathForRow(index.toLong(), 0L),
            when (align) {
                Align.Start -> UICollectionViewScrollPositionLeft or UICollectionViewScrollPositionTop
                Align.Center -> UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally
                Align.End -> UICollectionViewScrollPositionRight or UICollectionViewScrollPositionBottom
                else -> UICollectionViewScrollPositionCenteredVertically or UICollectionViewScrollPositionCenteredHorizontally
            },
            animate
        )
    }
}

actual val RecyclerView.firstVisibleIndex: Readable<Int>
    get() = (native.delegate as? GeneralCollectionDelegate<*>)?.firstVisibleIndex ?: Constant(0)

actual val RecyclerView.lastVisibleIndex: Readable<Int>
    get() = (native.delegate as? GeneralCollectionDelegate<*>)?.lastVisibleIndex ?: Constant(0)

val RecyclerView.centerIndex: Readable<Int>
    get() = (native.delegate as? GeneralCollectionDelegate<*>)?.centerIndex ?: Constant(0)