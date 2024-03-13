package com.lightningkite.rock.views.direct

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@ViewModifierDsl3
actual fun ViewWriter.hasPopover(
    requireClick: Boolean,
    preferredDirection: PopoverPreferredDirection,
    setup: ViewWriter.(popoverContext: PopoverContext) -> Unit
): ViewWrapper {
    beforeNextElementSetup {
        val theme = currentTheme
        val pos = this
        val sourceElement = this
        var existingElement: HTMLElement? = null
        var existingDismisser: HTMLElement? = null
        val maxDist = 64
        var stayOpen = false
        var close = {}
        fun makeElement() {
            if (existingElement != null) return
            with(targeting(document.body!!)) {
                stayOpen = false
                element<HTMLDivElement>("div") {
                    existingElement = this
                    style.position = "absolute"
                    style.zIndex = "999"
//                    style.transform = "scale(0,0)"
                    style.opacity = "0"
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
                                style.right = "${window.innerWidth - r.left}px"
                            }
                            when (preferredDirection.align) {
                                Align.Start -> style.bottom = "${window.innerHeight - r.bottom}px"
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
                                style.bottom = "${window.innerHeight - r.top}px"
                            }
                            when (preferredDirection.align) {
                                Align.Start -> style.right = "${window.innerWidth - r.right}px"
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
                        makeElement()
                    }
                    val currentElement = this
                    val mouseMove = { it: Event ->
                        it as MouseEvent
                        if (!stayOpen) {
                            val clientRect = sourceElement.getBoundingClientRect()
                            val popUpRect = currentElement.getBoundingClientRect()
                            if (min(
                                    maxOf(
                                        it.x - popUpRect.right,
                                        popUpRect.left - it.x,
                                        it.y - popUpRect.bottom,
                                        popUpRect.top - it.y,
                                    ), maxOf(
                                        it.x - clientRect.right,
                                        clientRect.left - it.x,
                                        it.y - clientRect.bottom,
                                        clientRect.top - it.y,
                                    )
                                ) > maxDist
                            ) close()
                        }
                    }
                    close = {
                        window.removeEventListener("mousemove", mouseMove)
                        existingElement?.style?.opacity = "0"
//                        existingElement?.style?.transform = "scale(0,0)"
                        existingDismisser?.style?.opacity = "0"
                        window.setTimeout({
                            existingElement?.let { it.parentElement?.removeChild(it) }
                            existingElement = null
                            existingDismisser?.let { it.parentElement?.removeChild(it) }
                            existingDismisser = null
                        }, 150)
                        close = {}
                    }
                    window.setTimeout({
                        style.opacity = "1"
//                        style.transform = "none"
                    }, 16)
                    window.addEventListener("mousemove", mouseMove)
                    window.addEventListener("scroll", { reposition() }, true)
                    setup(object : PopoverContext {
                        override fun close() {
                            close()
                        }
                    })
                }
            }
        }
        this.addEventListener("click", {
            makeElement()
            stayOpen = true
            val dismisser = document.createElement("div") as HTMLDivElement
            dismisser.style.position = "absolute"
            dismisser.style.left = "0"
            dismisser.style.right = "0"
            dismisser.style.bottom = "0"
            dismisser.style.top = "0"
            dismisser.style.opacity = "0"
            dismisser.onclick = {
                close()
            }
            window.setTimeout({
                dismisser.style.opacity = "1"
            }, 16)
            dismisser.calculationContext.reactiveScope {
                dismisser.style.backgroundColor = theme().background.closestColor().withAlpha(0.5f).toWeb()
            }
            existingDismisser = dismisser
            document.body!!.insertBefore(dismisser, existingElement)
        })
        if(!requireClick) {
            this.onmouseenter = {
                makeElement()
            }
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