package com.lightningkite.rock.views.direct

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.*
import org.w3c.dom.*


@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit
): ViewWrapper {
    containsNext<HTMLDivElement>("div") {
        onclick = {
            // TODO
        }
        style.position = "relative"
        element<HTMLDivElement>("div") {
            if (!requireClick) classList.add("visibleOnParentHover")
            style.position = "absolute"
            style.zIndex = "9999"
            if (preferredDirection.horizontal) {
                if (preferredDirection.after) {
                    style.left = "100%"
                } else {
                    style.right = "0"
                }
                when (preferredDirection.align) {
                    Align.Start -> style.bottom = "0"
                    Align.End -> style.top = "0"
                    else -> style.top = "calc(50% - 0)"
                }
            } else {
                if (preferredDirection.after) {
                    style.top = "100%"
                } else {
                    style.bottom = "0"
                }
                when (preferredDirection.align) {
                    Align.Start -> style.right = "0"
                    Align.End -> style.left = "0"
                    else -> style.left = "calc(50% - 0)"
                }
            }
            setup()
        }
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.textPopover(message: String): ViewWrapper = hasPopover {
    text {
        content = message
    } in card
}

@ViewModifierDsl3
actual fun ViewWriter.weight(amount: Float): ViewWrapper {
    beforeNextElementSetup {
        style.flexGrow = "$amount"
        style.flexShrink = "$amount"
        style.flexBasis = "0"
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual fun ViewWriter.gravity(horizontal: Align, vertical: Align): ViewWrapper {
    beforeNextElementSetup {
        classList.add("h${horizontal}", "v${vertical}")
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.scrolls: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("scroll-vertical")
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.scrollsHorizontally: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("scroll-horizontal")
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual fun ViewWriter.sizedBox(constraints: SizeConstraints): ViewWrapper {
    beforeNextElementSetup {

        if (constraints.minHeight == null) style.removeProperty("minHeight")
        else style.minHeight = constraints.minHeight.value

        if (constraints.maxHeight == null) style.removeProperty("maxHeight")
        else style.maxHeight = constraints.maxHeight.value

        if (constraints.minWidth == null) style.removeProperty("minWidth")
        else style.minWidth = constraints.minWidth.value

        if (constraints.maxWidth == null) style.removeProperty("maxWidth")
        else style.maxWidth = constraints.maxWidth.value

        if (constraints.width == null) style.removeProperty("width")
        else style.width = constraints.width.value

        if (constraints.height == null) style.removeProperty("height")
        else style.height = constraints.height.value
    }
    return ViewWrapper
}

@ViewModifierDsl3
actual val ViewWriter.marginless: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("marginless")
        }
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.withDefaultPadding: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("forcePadding")
        }
        return ViewWrapper
    }
// End