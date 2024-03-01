package com.lightningkite.rock.views.direct

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.() -> Unit
): ViewWrapper {
    beforeNextElementSetup {
        val pos = this
        var existingElement:  HTMLElement? = null
        var hoverOriginal = false
        var hoverPopup = false
        fun maybeRemoveElement() {
            if(!hoverOriginal && !hoverPopup) {
                existingElement?.let { it.parentElement?.removeChild(it) }
                existingElement = null
            }
        }
        fun makeElement() {
            if(existingElement != null) return
            with(targeting(document.body!!)) {
                element<HTMLDivElement>("div") {
                    existingElement = this
                    style.position = "absolute"
                    style.zIndex = "999"
                    style.height = "max-content"
                    fun reposition() {
                        val r = pos.getBoundingClientRect()
                        style.removeProperty("top")
                        style.removeProperty("left")
                        style.removeProperty("right")
                        style.removeProperty("bottom")
                        style.removeProperty("transform")
                        if (preferredDirection.horizontal) {
                            if (preferredDirection.after) {
                                style.left = "${r.right}px"
                            } else {
                                style.right = "${r.left}px"
                            }
                            when (preferredDirection.align) {
                                Align.Start -> style.bottom = "${r.bottom}px"
                                Align.End -> style.top = "${r.top}px"
                                else -> {
                                    style.top = "${(r.top + r.bottom) / 2}px"
                                    style.transform = "translateY(-50%)"
                                }
                            }
                        } else {
                            if (preferredDirection.after) {
                                style.top = "${r.bottom}px"
                            } else {
                                style.bottom = "${r.top}px"
                            }
                            when (preferredDirection.align) {
                                Align.Start -> style.right = "${r.right}px"
                                Align.End -> style.left = "${r.left}px"
                                else -> {
                                    style.left = "${(r.left + r.right) / 2}px"
                                    style.transform = "translateX(-50%)"
                                }
                            }
                        }
                    }
                    reposition()
                    this.onmouseenter = {
                        hoverPopup = true
                        makeElement()
                    }
                    this.onmouseleave = {
                        hoverPopup = false
                        maybeRemoveElement()
                    }
                    window.addEventListener("scroll", { reposition() }, true)
                    setup()
                }
            }
        }
        this.onmouseenter = {
            hoverOriginal = true
            makeElement()
        }
        this.onmouseleave = {
            hoverOriginal = false
            maybeRemoveElement()
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
        (currentView as? HTMLDivElement)?.let(::ContainingView)?.spacing = 0.px
        return ViewWrapper
    }

@ViewModifierDsl3
actual val ViewWriter.padded: ViewWrapper
    get() {
        beforeNextElementSetup {
            classList.add("forcePadding")
        }
        return ViewWrapper
    }


// End

@ViewModifierDsl3
actual fun ViewWriter.onlyWhen(default: Boolean, condition: suspend () -> Boolean): ViewWrapper {
    wrapNext(document.createElement("div") as HTMLDivElement) {
        className = "hidingContainer"
        hidden = !default
        var last = !default
        window.setTimeout({
            calculationContext.reactiveScope {
                val child = firstElementChild as? HTMLElement ?: return@reactiveScope
                val value = !condition()
                if (value == last) return@reactiveScope
                last = value
                if (animationsEnabled) {
                    classList.add("animatingShowHide")

                    val myStyle = window.getComputedStyle(child)
                    val transitionTime = myStyle.transitionDuration.let { Duration.parseOrNull(it) } ?: 150.milliseconds
                    val totalTime = transitionTime.inWholeMilliseconds.toDouble()
                    var oldAnimTime = totalTime
                    (this.asDynamic().__rock__hiddenAnim as? Animation)?.let {
                        oldAnimTime = it.currentTime
                        it.cancel()
                    }
                    (this.asDynamic().__rock__hiddenAnim2 as? Animation)?.let {
                        it.cancel()
                    }
                    this.asDynamic().__rock__goalHidden = value
                    hidden = false
                    val parent = generateSequence(this as HTMLElement) { it.parentElement as? HTMLElement }.drop(1)
                        .firstOrNull { !it.classList.contains("toggle-button") } ?: return@reactiveScope
                    val parentStyle = window.getComputedStyle(parent)
                    val x =
                        parentStyle.display == "flex" && parentStyle.flexDirection.contains("row")// && myStyle.width.none { it.isDigit() }
                    val y =
                        parentStyle.display == "flex" && parentStyle.flexDirection.contains("column")// && myStyle.height.none { it.isDigit() }

                    val before = js("{}")
                    val after = js("{}")
                    val full = if (value) before else after
                    val fullTransform = ArrayList<String>()
                    val gone = if (value) after else before
                    val goneTransform = ArrayList<String>()

                    var fullWidth = ""
                    var fullHeight = ""
                    var gap = ""
                    if (hidden) {
                        hidden = false
                        fullWidth = myStyle.width
                        fullHeight = myStyle.height
                        gap = parentStyle.columnGap
                        hidden = true
                    } else {
                        fullWidth = myStyle.width
                        fullHeight = myStyle.height
                        gap = parentStyle.columnGap
                    }
                    child.style.width = myStyle.width
                    child.style.maxWidth = "unset"
                    child.style.height = myStyle.height
                    child.style.maxHeight = "unset"

                    if (x) {
                        goneTransform.add("scaleX(0)")
                        fullTransform.add("scaleX(1)")
                        gone.marginLeft = "calc($gap / -2.0)"
                        gone.paddingLeft = "0px"
                        gone.marginRight = "calc($gap / -2.0)"
                        gone.paddingRight = "0px"
                        gone.width = "0px"
                        gone.minWidth = "0px"
                        gone.maxWidth = "0px"
                        full.width = fullWidth
                        full.minWidth = fullWidth
                        full.maxWidth = fullWidth
                    }
                    if (y) {
                        goneTransform.add("scaleY(0)")
                        fullTransform.add("scaleY(1)")
                        gone.marginTop = "calc($gap / -2.0)"
                        gone.paddingTop = "0px"
                        gone.marginBottom = "calc($gap / -2.0)"
                        gone.paddingBottom = "0px"
                        gone.height = "0px"
                        gone.minHeight = "0px"
                        gone.maxHeight = "0px"
                        full.height = fullHeight
                        full.minHeight = fullHeight
                        full.maxHeight = fullHeight
                    }
                    if (!x && !y) {
                        full.opacity = "1"
                        gone.opacity = "0"
                    }
                    goneTransform.takeUnless { it.isEmpty() }?.let {
//                        gone.transform = it.joinToString(" ")
//                        gone.transformOrigin = "top left"
                    }
                    fullTransform.takeUnless { it.isEmpty() }?.let {
//                        full.transform = it.joinToString(" ")
//                        full.transformOrigin = "top left"
                    }
                    this.animate(
                        arrayOf(before, after),
                        js(
                            "duration" to totalTime,
                            "easing" to "ease-out"
                        )
                    ).let {
                        it.currentTime = (totalTime - oldAnimTime).coerceAtLeast(0.0)
                        it.onfinish = { ev ->
                            if (this.asDynamic().__rock__hiddenAnim == it) {
                                hidden = value
                                classList.remove("animatingShowHide")
                                this.asDynamic().__rock__hiddenAnim = null
                                child.style.removeProperty("width")
                                child.style.removeProperty("maxWidth")
                                child.style.removeProperty("height")
                                child.style.removeProperty("maxHeight")
                            }
                        }
                        it.oncancel = { ev ->
                            if (this.asDynamic().__rock__hiddenAnim == it) {
                                hidden = value
                                classList.remove("animatingShowHide")
                                this.asDynamic().__rock__hiddenAnim = null
                                child.style.removeProperty("width")
                                child.style.removeProperty("maxWidth")
                                child.style.removeProperty("height")
                                child.style.removeProperty("maxHeight")
                            }
                        }
                        it.onremove = { ev ->
                            if (this.asDynamic().__rock__hiddenAnim == it) {
                                hidden = value
                                classList.remove("animatingShowHide")
                                this.asDynamic().__rock__hiddenAnim = null
                                child.style.removeProperty("width")
                                child.style.removeProperty("maxWidth")
                                child.style.removeProperty("height")
                                child.style.removeProperty("maxHeight")
                            }
                        }
                        this.asDynamic().__rock__hiddenAnim = it
                    }
                } else {
                    hidden = !value
                }
            }
        }, 1)
    }
    return ViewWrapper
}