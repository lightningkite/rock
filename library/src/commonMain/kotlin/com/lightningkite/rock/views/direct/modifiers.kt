package com.lightningkite.rock.views.direct

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.PopoverPreferredDirection
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.views.ViewModifierDsl3
import com.lightningkite.rock.views.ViewWriter

@ViewModifierDsl3
expect fun ViewWriter.hasPopover(requireClick: Boolean = false, preferredDirection: PopoverPreferredDirection = PopoverPreferredDirection.belowRight, setup: ViewWriter.()->Unit): ViewWrapper
@ViewModifierDsl3
expect fun ViewWriter.weight(amount: Float): ViewWrapper
@ViewModifierDsl3
expect fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper
@ViewModifierDsl3
expect val ViewWriter.scrolls: ViewWrapper
@ViewModifierDsl3
expect val ViewWriter.scrollsHorizontally: ViewWrapper
@ViewModifierDsl3
expect fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper
@ViewModifierDsl3
fun ViewWriter.sizeConstraints(
    minWidth: Dimension? = null,
    maxWidth: Dimension? = null,
    minHeight: Dimension? = null,
    maxHeight: Dimension? = null,
    width: Dimension? = null,
    height: Dimension? = null,
): ViewWrapper = sizedBox(SizeConstraints(
    minWidth = minWidth,
    maxWidth = maxWidth,
    minHeight = minHeight,
    maxHeight = maxHeight,
    width = width,
    height = height
))
@ViewModifierDsl3
expect val ViewWriter.marginless: ViewWrapper
@ViewModifierDsl3
expect val ViewWriter.withDefaultPadding: ViewWrapper