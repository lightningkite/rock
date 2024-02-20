package com.lightningkite.rock.views

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSStyleSheet
import org.w3c.dom.css.get
import org.w3c.dom.events.Event
import kotlin.time.Duration

object DynamicCSS {
    val customStyleSheet: CSSStyleSheet by lazy {
        val sheet = document.createElement("style") as HTMLStyleElement
        sheet.title = "generated-css"
        document.head!!.appendChild(sheet)
        document.styleSheets.let {
            for (i in 0 until it.length) {
                val copy = it.get(i)!!
                if (copy.title == sheet.title) return@let copy as CSSStyleSheet
            }
            throw IllegalStateException()
        }
    }

    init {
        // basis rules
        style("*", mapOf("box-sizing" to "border-box", "line-height" to "unset", /*"overflow" to "hidden"*/))
        style("h1", mapOf("font-size" to "2rem"))
        style("h2", mapOf("font-size" to "1.6rem"))
        style("h3", mapOf("font-size" to "1.4rem"))
        style("h4", mapOf("font-size" to "1.3rem"))
        style("h5", mapOf("font-size" to "1.2rem"))
        style("h6", mapOf("font-size" to "1.1rem"))
        style(".subtext", mapOf("font-size" to "0.8rem"))
//        style.visibility = if (value) "visible" else "hidden"
        style(
            ".visibleOnParentHover",
            mapOf("visibility" to "hidden", "width" to "max-content", "height" to "max-content")
        )
        style(":hover>.visibleOnParentHover", mapOf("visibility" to "visible"))
        style(":hover.visibleOnParentHover", mapOf("visibility" to "visible"))
        style(
            "body", mapOf(
                "height" to "100vh",
                "max-height" to "100vh",
                "max-width" to "100vw",
            )
        )

        style(
            "body > div", mapOf(
                "height" to "100vh",
                "max-height" to "100vh",
                "max-width" to "100vw",
            )
        )

        style(
            "a", mapOf(
                "text-decoration" to "none",
                "color" to "unset",
            )
        )

        style(
            "a:visited", mapOf(
                "color" to "unset",
            )
        )

        style(
            "button, input, textarea, select", mapOf(
                "background" to "none",
                "border-width" to "0",
                "outline-width" to "0",
                "font" to "unset",
                "color" to "unset",
                "text-align" to "start",
            )
        )

        style(
            "input.sameThemeText, textarea.sameThemeText", mapOf(
                "border-bottom-style" to "solid",
                "border-bottom-width" to "1px",
                "border-radius" to "0",
            )
        )

        style(
            "input.sameThemeText:focus, textarea.sameThemeText:focus", mapOf(
                "border-radius" to "0",
            )
        )

        style(
            "input:focus textarea:focus", mapOf(
                "outline" to "inherit",
            )
        )

        style(
            ".toggle-button", mapOf(
                "display" to "flex",
                "align-items" to "center",
            )
        )

        style(
            ".toggle-button > span", mapOf(
                "flex-grow" to "1",
                "flex-shrink" to "1",
            )
        )

        style(
            ".spinner", mapOf(
                "width" to "32px !important",
                "height" to "32px !important",
                "opacity" to "0.5 !important",
                "background" to "none !important",
                "box-shadow" to "none !important",
                "border-style" to "solid !important",
                "border-color" to "currentColor currentColor currentColor transparent !important",
                "border-width" to "5px !important",
                "border-radius" to "50% !important",
                "animation" to "spin 2s infinite linear !important",
            )
        )

        style(
            ".rock-swap", mapOf(
                "display" to "block",
                "position" to "relative",
            )
        )

        style(
            ".rock-swap > *", mapOf(
                "position" to "absolute",
                "top" to "0",
                "left" to "0",
                "right" to "0",
                "bottom" to "0",
                "max-width" to "100%",
                "max-height" to "100%",
            )
        )

        style(
            ".rock-stack", mapOf(
                "display" to "grid",
                "grid-template-columns" to "100%",
                "grid-template-rows" to "100%",
            )
        )

        style(
            ".rock-stack > *", mapOf(
                "grid-column-start" to "1",
                "grid-column-end" to "1",
                "grid-row-start" to "1",
                "grid-row-end" to "1",
                "align-self" to "stretch",
                "justify-self" to "stretch",
            )
        )

        style(
            ".rock-stack > .hStart", mapOf(
                "justify-self" to "start",
            )
        )

        style(
            ".rock-stack > .hCenter", mapOf(
                "justify-self" to "center",
            )
        )

        style(
            ".rock-stack > .hStretch", mapOf(
                "justify-self" to "stretch",
            )
        )

        style(
            ".rock-stack > .hEnd", mapOf(
                "justify-self" to "end",
            )
        )

        style(
            ".rock-stack > .vStart", mapOf(
                "align-self" to "start",
            )
        )

        style(
            ".rock-stack > .vCenter", mapOf(
                "align-self" to "center",
            )
        )

        style(
            ".rock-stack > .vStretch", mapOf(
                "align-self" to "stretch",
            )
        )

        style(
            ".rock-stack > .vEnd", mapOf(
                "align-self" to "end",
            )
        )

        style(
            ".rock-row", mapOf(
                "display" to "flex",
                "flex-direction" to "row",
            )
        )
//        style(
//            ".rock-row > *", mapOf(
//                "max-width" to "unset",
//            )
//        )

        style(
            ".rock-row > .vStart", mapOf(
                "align-self" to "start",
            )
        )

        style(
            ".rock-row > .vCenter", mapOf(
                "align-self" to "center",
            )
        )

        style(
            ".rock-row > .vStretch", mapOf(
                "align-self" to "stretch",
            )
        )

        style(
            ".rock-row > .vEnd", mapOf(
                "align-self" to "end",
            )
        )

        style(
            ".rock-col", mapOf(
                "display" to "flex",
                "flex-direction" to "column",
            )
        )

//        style(
//            ".rock-col > *", mapOf(
//                "max-height" to "unset",
//            )
//        )

        style(
            ".rock-col > .hStart", mapOf(
                "align-self" to "start",
            )
        )

        style(
            ".rock-col > .hCenter", mapOf(
                "align-self" to "center",
            )
        )

        style(
            ".rock-col > .hStretch", mapOf(
                "align-self" to "stretch",
            )
        )

        style(
            ".rock-col > .hEnd", mapOf(
                "align-self" to "end",
            )
        )

        style(
            "p.loading:not(.inclBack), h1.loading:not(.inclBack), h2.loading:not(.inclBack), h3.loading:not(.inclBack), h4.loading:not(.inclBack), h5.loading:not(.inclBack), h6.loading:not(.inclBack), img.loading:not(.inclBack), input.loading:not(.inclBack), select.loading:not(.inclBack), textarea.loading:not(.inclBack)",
            mapOf(
                "min-height" to "1em",
                "background" to "color-mix(in srgb, currentColor, transparent 70%) !important",
                "animation" to "flickerAnimation 2s infinite !important",
                "animation-timing-function" to "linear",
            )
        )

        style(
            "button", mapOf(
                "position" to "relative",
            )
        )
        style(
            "button.loading:after", mapOf(
                "opacity" to "0.5 !important",
                "content" to "\"\"",
//                "display" to "none",
                "pointer-events" to "none",
                "position" to "absolute",
                "top" to "calc(50% - 20px)",
                "left" to "calc(50% - 20px)",
                "width" to "32px !important",
                "height" to "32px !important",
                "background" to "none !important",
                "box-shadow" to "none !important",
                "border-style" to "solid !important",
                "border-color" to "currentColor currentColor currentColor transparent !important",
                "border-width" to "6px !important",
                "border-radius" to "50% !important",
                "transition" to "all .3s ease",
                "animation" to "spin 2s infinite linear !important",
            )
        )
        style(
            "button.loading > *", mapOf(
                "opacity" to "0.15",
            )
        )

        style(
            ".clickable", mapOf(
                "cursor" to "pointer",
            )
        )

        style(
            ".switch", mapOf(
                "position" to "relative",
                "overflow" to "visible",
                "height" to "1.5rem",
                "width" to "3rem",
                "cursor" to "pointer",
                "appearance" to "none",
                "-webkit-appearance" to "none",
                "border-radius" to "9999px !important",
                "background-color" to "rgba(100, 116, 139, 0.377)",
                "transition" to "all .3s ease",
            )
        )

        style(
            ".switch:checked", mapOf(
                "background-color" to "rgba(236, 72, 153, 1)",
            )
        )

        style(
            ".switch::before", mapOf(
                "position" to "absolute",
                "content" to "\"\"",
                "left" to "calc(1.5rem - 1.6rem)",
                "top" to "calc(1.5rem - 1.6rem)",
                "display" to "block",
                "height" to "1.6rem",
                "width" to "1.6rem",
                "max-width" to "unset",
                "max-height" to "unset",
                "cursor" to "pointer",
                "border" to "1px solid rgba(100, 116, 139, 0.527)",
                "border-radius" to "9999px !important",
                "background-color" to "rgba(255, 255, 255, 1)",
                "box-shadow" to "0 3px 10px rgba(100, 116, 139, 0.327)",
                "transition" to "all .3s ease",
            )
        )

        style(
            ".switch:hover::before", mapOf(
                "box-shadow" to "0 0 0px 8px rgba(0, 0, 0, .15)"
            )
        )

        style(
            ".switch:checked:hover::before", mapOf(
                "box-shadow" to "0 0 0px 8px rgba(236, 72, 153, .15)"
            )
        )

        style(
            ".switch:checked:before", mapOf(
                "left" to "calc(3rem - 1.6rem)",
                "border-color" to "rgba(236, 72, 153, 1)",
            )
        )

        style(
            ".checkbox", mapOf(
                "appearance" to "none",
                "width" to "1.5rem",
                "height" to "1.5rem",
                "position" to "relative",
                "padding" to "0px !important",
//                "border-width" to "0.25rem",
//                "border-style" to "solid",
            )
        )
        style(
            ".checkbox::after", mapOf(
                "position" to "absolute",
                "content" to "\"\"",
                "display" to "block",
                "width" to "0.8rem",
                "height" to "0.3rem",
                "top" to "0.4rem",
                "left" to "0.25rem",
                "border-left-color" to "currentColor",
                "border-bottom-color" to "currentColor",
                "border-left-style" to "solid",
                "border-bottom-style" to "solid",
                "border-left-width" to "0.2rem",
                "border-bottom-width" to "0.2rem",
                "opacity" to "0.4",
                "transform" to "rotate(-45deg) scale(0,0)",
                "transition-property" to "opacity, transform",
                "transition-duration" to "0.15s",
                "transition-timing-function" to "linear",
            )
        )
        style(
            ":checked.checkbox::after", mapOf(
                "opacity" to "1",
                "transform" to "rotate(-45deg)"
            )
        )
        style(
            ".radio", mapOf(
                "appearance" to "none",
                "width" to "1.5rem",
                "height" to "1.5rem",
                "position" to "relative",
                "border-radius" to "999px !important",
                "padding" to "0px !important",
                "border-width" to "0.1rem",
                "border-style" to "solid",
            )
        )
        style(
            ".radio::after", mapOf(
                "position" to "absolute",
                "border-radius" to "999px",
                "content" to "\"\"",
                "display" to "block",
                "width" to "1rem",
                "height" to "1rem",
                "top" to "0.15rem",
                "left" to "0.15rem",
                "background-color" to "currentColor",
                "opacity" to "0.4",
                "transform" to "scale(0,0)",
                "transition-property" to "opacity, transform",
                "transition-duration" to "0.15s",
                "transition-timing-function" to "linear",
            )
        )
        style(
            ":checked.radio::after", mapOf(
                "opacity" to "1",
                "transform" to "none"
            )
        )

        style(
            ".crowd", mapOf(
                "padding" to "0 !important",
            )
        )

        style(
            ".crowd > *", mapOf(
                "margin" to "0px !important",
                "--margin" to "0px !important",
            )
        )

        style(
            ".rock-label.rock-label", mapOf(
                "display" to "flex",
                "flex-direction" to "column",
                "align-items" to "stretch",
            )
        )

        style(
            ".rock-label.rock-label > :nth-child(1):nth-child(1):nth-child(1)", mapOf(
                "font-size" to "0.8rem",
                "margin-bottom" to "0px",
            )
        )

        style(
            ".rock-label.rock-label > :nth-child(2):nth-child(2):nth-child(2)", mapOf(
                "margin-top" to "0.25rem",
            )
        )

        style(
            "input:not(.mightTransition).editable.editable, textarea:not(.mightTransition).editable.editable, select:not(.mightTransition).editable.editable", mapOf(
                "border-bottom-color" to "currentColor",
                "border-bottom-width" to "1px",
                "border-bottom-style" to "solid",
            )
        )

        style(
            "input:not(.mightTransition).editable.editable:focus, textarea:not(.mightTransition).editable.editable:focus, select:not(.mightTransition).editable.editable:focus", mapOf(
                "border-bottom-color" to "currentColor",
                "border-bottom-width" to "2px",
                "border-bottom-style" to "solid",
                "outline" to "none",
            )
        )

        style(
            "*", mapOf(
                "scrollbar-color" to "#999 #0000",
                "scrollbar-width" to "thin",
                "scrollbar-gutter" to "auto",
                "flex-shrink" to "0",
                "max-width" to "calc(100% - var(--margin, 0) * 2 + 1)",
                "max-height" to "calc(100% - var(--margin, 0) * 2 + 1)",
                "min-height" to "0",
                "min-width" to "0",
                "margin" to "0px",
                "--margin" to "0px",
                "padding" to "0",
            )
        )

        style(
            "input", mapOf(
                "min-height" to "1.5rem",
                "min-width" to "1.5rem",
            )
        )

        style(
            "::placeholder", mapOf(
                "color" to "currentColor",
                "opacity" to "0.3",
            )
        )

        style(
            ".rock-separator", mapOf(
                "background-color" to "currentColor",
                "opacity" to "0.25",
                "min-width" to "1px",
                "min-height" to "1px",
            )
        )

        style(
            "iframe#webpack-dev-server-client-overlay", mapOf(
                "display" to "none !important"
            )
        )

        style(
            ".recycler", mapOf(
                "overflow-y" to "auto"
            )
        )

        style(
            ".recycler > *", mapOf(
                "max-height" to "unset",
            )
        )

        style(
            ".recycler-horz", mapOf(
                "display" to "flex",
                "flex-direction" to "row",
                "overflow-x" to "auto",
            )
        )

        style(
            ".recycler-horz > *", mapOf(
                "max-width" to "unset",
            )
        )

        style(
            ".recycler-grid", mapOf(
                "display" to "flex",
                "flex-direction" to "row",
                "overflow-x" to "auto",
            )
        )

        style(
            ".scroll-vertical > *", mapOf(
                "max-height" to "unset",
            )
        )

        style(
            ".scroll-vertical", mapOf(
                "overflow-y" to "auto",
            )
        )

        style(
            ".scroll-horizontal > *", mapOf(
                "max-width" to "unset",
            )
        )

        style(
            ".scroll-horizontal", mapOf(
                "overflow-x" to "auto",
            )
        )

        style(
            "::-webkit-scrollbar", mapOf(
                "background" to "#0000",
            )
        )

        style(
            "::-webkit-scrollbar-thumb", mapOf(
                "background" to "color-mix(in srgb, currentColor 20%, transparent)",
                "-webkit-border-radius" to "4px",
            )
        )

        style(
            "::-webkit-scrollbar-corner", mapOf(
                "background" to "#0000"
            )
        )

        rule(
            """
            @keyframes flickerAnimation {
                0% {
                    opacity: 1;
                }
                50% {
                    opacity: 0.5;
                }
                100% {
                    opacity: 1;
                }
            }
        """.trimIndent()
        )
        rule(
            """
            @keyframes spin {
                from {
                    transform: rotate(0deg);
                }
                to {
                    transform: rotate(360deg);
                }
            }
        """.trimIndent()
        )

//        style(
//            ".rock-row > [hidden]", mapOf(
//                "width" to "0px !important",
//                "margin-left" to "0px !important",
//                "margin-right" to "0px !important",
//                "transform" to "scaleX(0)",
//            )
//        )
//        style(
//            ".rock-col > [hidden]", mapOf(
//                "height" to "0px !important",
//                "margin-top" to "0px !important",
//                "margin-bottom" to "0px !important",
//                "transform" to "scaleY(0)",
//            )
//        )
//        style(
//            ".rock-stack > [hidden]", mapOf(
////                "width" to "0px !important",
////                "height" to "0px !important",
//                "transform" to "scale(0, 0)",
//            )
//        )
        style(
            "[hidden]", mapOf(
                "display" to "none !important",
            )
        )

        style(
            ":not(.unrock)", mapOf(
                "transition-duration" to "0.15s",
                "transition-timing-function" to "linear",
                "transition-delay" to "0s",
                "transition-property" to "color, background-image, background-color, outline-color, box-shadow, border-radius, opacity",
            )
        )

        style(
            ":not(.unrock).animatingShowHide", mapOf(
                "overflow" to "hidden",
                "minWidth" to "0px",
                "minHeight" to "0px",
            )
        )

        style(
            ".notransition *", mapOf(
                "transition" to "none !important"
            )
        )

        style(
            ".dismissBackground", mapOf(
                "z-index" to "998",
            )
        )
        style(
            ".dismissBackground + *", mapOf(
                "z-index" to "999",
            )
        )
//        style(
//            "div:not(.mightTransition):not(.mightTransition)", mapOf(
//                "pointer-events" to "none",
//            )
//        )
//        style(
//            "div:not(.mightTransition) > *", mapOf(
//                "pointer-events" to "all",
//            )
//        )
//        recyclerView
//        contentScroll
//        content
//        barScroll
//        barContent
        style(".contentScroll::-webkit-scrollbar", mapOf(
//            "display" to "none"
        ))
        style(".contentScroll", mapOf(
            "scrollbar-width" to "none",
            "overflow-anchor" to "none",
        ))
        style(".contentScroll > *", mapOf(
            "overflow-anchor" to "revert",
        ))
        style(".viewPager", mapOf(
            "overflow-x" to "scroll",
            "scroll-snap-type" to "x mandatory",
            "display" to "flex",
            "flex-direction" to "row",
        ))
        style(".viewPager > *", mapOf(
            "width" to "100%",
            "height" to "100%",
            "scroll-snap-align" to "center",
        ))
    }

    fun rule(rule: String, index: Int = 0): Int {
        return customStyleSheet.insertRule(rule, index)
    }

    private val transitionHandled = HashSet<String>()
    fun transition(transition: ScreenTransition): String {
        if (!transitionHandled.add(transition.name)) return "transition-${transition.name}"
        fun StringBuilder.extracted(part: ScreenTransitionPart) {
            for ((key, value) in part.from) append("$key: $value; ")
            append("} to { ")
            for ((key, value) in part.to) append("$key: $value; ")
            append("}")
        }

        rule(buildString {
            append("@keyframes transition-${transition.name}-enter { from { ")
            extracted(transition.enter)
        }, 0)
        rule(buildString {
            append("@keyframes transition-${transition.name}-exit { from { ")
            extracted(transition.exit)
        }, 0)
        return "transition-${transition.name}"
    }

    private val fontHandled = HashSet<String>()
    fun font(font: Font): String {
        if (!fontHandled.add(font.cssFontFamilyName)) return font.cssFontFamilyName
        if (font.url != null) {
            document.head!!.appendChild((document.createElement("link") as HTMLLinkElement).apply {
                rel = "stylesheet"
                type = "text/css"
                href = font.url
            })
        }
        if (font.direct != null) {
            font.direct.normal?.let {
                rule("@font-face {font-family: '${font.cssFontFamilyName}';font-style: normal;font-weight: normal;src:url('${it}');}")
            }
            font.direct.bold?.let {
                rule("@font-face {font-family: '${font.cssFontFamilyName}';font-style: normal;font-weight: bold;src:url('${it}');}")
            }
            font.direct.italic?.let {
                rule("@font-face {font-family: '${font.cssFontFamilyName}';font-style: italic;font-weight: normal;src:url('${it}');}")
            }
            font.direct.boldItalic?.let {
                rule("@font-face {font-family: '${font.cssFontFamilyName}';font-style: italic;font-weight: bold;src:url('${it}');}")
            }
        }
        return font.cssFontFamilyName
    }

    fun style(selector: String, map: Map<String, String>) {
        val wrapSelector = selector//":not(.unrock) $selector"
        rule(
            """$wrapSelector { ${map.entries.joinToString("; ") { "${it.key}: ${it.value}" }} }""",
            0
        )
    }

    fun tempStyle(selector: String, map: Map<String, String>): () -> Unit {
        val wrapSelector = selector//":not(.unrock) $selector"
        val content = """$wrapSelector { ${map.entries.joinToString("; ") { "${it.key}: ${it.value}" }} }"""
        rule(
            content,
            0
        )
        val rule = customStyleSheet.cssRules.get(0)
        return {
            customStyleSheet.cssRules.let {
                (
                        0..<it.length).find { index -> it.get(index) === rule }
            }?.let {
                customStyleSheet.deleteRule(it)
            }
        }
    }

    private fun Dimension.toBoxShadow(): String {
        if (value == "0px")
            return "none"
        val offsetX = 0.px.value
        val offsetY = value
        val blur = (this * 2).value
        val spread = 0.px.value
        return "$offsetX $offsetY $blur $spread #77777799"
    }

    private fun Paint.toCss() = when (this) {
        is Color -> this.toWeb()
        is LinearGradient -> "linear-gradient(${angle.turns}turn, ${joinGradientStops(stops)})"
        is RadialGradient -> "radial-gradient(circle at center, ${joinGradientStops(stops)})"
    }

    private fun joinGradientStops(stops: List<GradientStop>): String {
        return stops.joinToString {
            "${it.color.toWeb()} ${it.ratio * 100}%"
        }
    }


    fun themeInteractive(theme: Theme): String {
        theme(
            theme.down(),
            listOf(".clickable:active .theme-${theme.id}", ".clickable:active.theme-${theme.id}"),
            includeMaybeTransition = true
        )
        theme(
            theme.hover(),
            listOf(".clickable:hover .theme-${theme.id}", ".clickable:hover.theme-${theme.id}"),
            includeMaybeTransition = true
        )
        theme(
            theme.focus(),
            listOf(".clickable:focus-visible .theme-${theme.id}", ".clickable:focus-visible.theme-${theme.id}"),
            includeMaybeTransition = true
        )
        theme(
            theme.disabled(),
            listOf(".clickable:disabled:disabled .theme-${theme.id}", ".clickable:disabled:disabled.theme-${theme.id}"),
            includeMaybeTransition = false
        )

        theme(
            theme.selected(),
            listOf(
                "input:checked.checkResponsive .theme-${theme.id}",
                "input:checked.checkResponsive.theme-${theme.id}",
                "input:checked+.checkResponsive .theme-${theme.id}",
                "input:checked+.checkResponsive.theme-${theme.id}",
            ),
            includeMaybeTransition = true
        )
        theme(
            theme.selected().hover(),
            listOf(
                "input:checked.checkResponsive:hover .theme-${theme.id}",
                "input:checked.checkResponsive:hover.theme-${theme.id}",
                "input:checked+.checkResponsive:hover .theme-${theme.id}",
                "input:checked+.checkResponsive:hover.theme-${theme.id}",
            ),
            includeMaybeTransition = true
        )
        theme(
            theme.selected().focus(),
            listOf(
                "input:checked.checkResponsive:focus-visible .theme-${theme.id}",
                "input:checked.checkResponsive:focus-visible.theme-${theme.id}",
                "input:checked+.checkResponsive:focus-visible .theme-${theme.id}",
                "input:checked+.checkResponsive:focus-visible.theme-${theme.id}",
            ),
            includeMaybeTransition = true
        )
        theme(
            theme.selected().disabled(),
            listOf(
                "input:checked.checkResponsive:disabled .theme-${theme.id}",
                "input:checked.checkResponsive:disabled.theme-${theme.id}",
                "input:checked+.checkResponsive:disabled .theme-${theme.id}",
                "input:checked+.checkResponsive:disabled.theme-${theme.id}",
            ),
            includeMaybeTransition = true
        )

        return theme(theme)
    }

    private val themeHandled = HashSet<String>()
    fun theme(
        theme: Theme,
        asSelectors: List<String> = listOf(".theme-${theme.id}"),
        includeMaybeTransition: Boolean = false
    ): String {
        val includeSelectors = asSelectors.filter { themeHandled.add(it) }
        if (includeSelectors.isEmpty()) return "theme-${theme.id}"
        fun sel(vararg plus: String): String {
            return includeSelectors.asSequence().flatMap { plus.asSequence().map { p -> "$it$p" } }.joinToString(", ")
        }
        style(
            sel(".mightTransition:not(.isRoot)", ".forcePadding"), mapOf(
                "padding" to theme.defaultSpacing.value,
            )
        )
        style(
            sel(".mightTransition:not(.marginless)",  ".viewDraws:not(.marginless)", ".forcePadding"), mapOf(
                "margin" to "var(--nextMargin, ${theme.defaultSpacing.value})",
                "--margin" to "var(--nextMargin, ${theme.defaultSpacing.value})",
            )
        )
        style(
            if (includeMaybeTransition) sel(".mightTransition") else sel(".transition"),
            when (val it = theme.background) {
                is Color -> mapOf("background-color" to it.toCss())
                is LinearGradient -> mapOf(
                    "background-image" to "linear-gradient(${it.angle.turns}turn, ${joinGradientStops(it.stops)})",
                    "background-attachment" to (if (it.screenStatic) "fixed" else "unset"),
                )

                is RadialGradient -> mapOf(
                    "background-image" to "radial-gradient(circle at center, ${joinGradientStops(it.stops)})",
                    "background-attachment" to (if (it.screenStatic) "fixed" else "unset"),
                )
            }
        )
        style(
            if (includeMaybeTransition) sel(".mightTransition:not(.marginless)") else sel(".transition:not(.marginless)"),
            mapOf(
                "outline-width" to theme.outlineWidth.value,
                "box-shadow" to theme.elevation.toBoxShadow(),
                "outline-style" to if (theme.outlineWidth != 0.px) "solid" else "none",
            )
        )
        style(sel("> *"), mapOf(
            "--nextMargin" to theme.defaultSpacing.value,
        ))
        style(
            sel(".mightTransition:not(.marginless)"), mapOf(
                "border-top-left-radius" to theme.cornerRadii.topLeft.value,
                "border-top-right-radius" to theme.cornerRadii.topRight.value,
                "border-bottom-left-radius" to theme.cornerRadii.bottomLeft.value,
                "border-bottom-right-radius" to theme.cornerRadii.bottomRight.value,
            )
        )
        style(
            sel(".title"), mapOf(
                "font-family" to font(theme.title.font),
                "font-weight" to if (theme.title.bold) "bold" else "normal",
                "font-style" to if (theme.title.italic) "italic" else "normal",
                "text-transform" to if (theme.title.allCaps) "uppercase" else "none",
                "line-height" to theme.title.lineSpacingMultiplier.toString(),
                "letter-spacing" to theme.title.additionalLetterSpacing.toString(),
            )
        )
        style(
            sel(""), mapOf(
                "color" to theme.foreground.toCss(),
                "font-family" to font(theme.body.font),
                "font-weight" to if (theme.body.bold) "bold" else "normal",
                "font-style" to if (theme.body.italic) "italic" else "normal",
                "text-transform" to if (theme.body.allCaps) "uppercase" else "none",
                "line-height" to theme.body.lineSpacingMultiplier.toString(),
                "letter-spacing" to theme.body.additionalLetterSpacing.toString(),
                "outline-color" to theme.outline.toCss(),
            )
        )
        style(
            sel(".dismissBackground"), mapOf(
                "border-radius" to "0",
                "margin" to "0px",
                "--margin" to "0px",
                "outline-width" to "0",
            ) + when (val it = theme.background.applyAlpha(0.5f)) {
                is Color -> mapOf("background-color" to it.toCss())
                is LinearGradient -> mapOf(
                    "background-image" to "linear-gradient(${it.angle.turns}turn, ${joinGradientStops(it.stops)})",
                    "background-attachment" to (if (it.screenStatic) "fixed" else "unset"),
                )

                is RadialGradient -> mapOf(
                    "background-image" to "radial-gradient(circle at center, ${joinGradientStops(it.stops)})",
                    "background-attachment" to (if (it.screenStatic) "fixed" else "unset"),
                )
            }
        )

        return "theme-${theme.id}"
    }
}


fun js(vararg entries: Pair<String, Any?>): dynamic {
    val out = js("{}")
    for (entry in entries) out[entry.first] = entry.second
    return out
}

private var tempId = 0
fun HTMLElement.animateHidden(value: Boolean) {
    val prev = this.asDynamic().__rock__goalHidden ?: hidden
    if (value == prev) return

    classList.add("animatingShowHide")

    val myStyle = window.getComputedStyle(this)
    val transitionTime = myStyle.transitionDuration.let { Duration.parse(it) }
    val totalTime = transitionTime.inWholeMilliseconds.toDouble()
    var oldAnimTime = totalTime
    (this.asDynamic().__rock__hiddenAnim as? Animation)?.let {
//        it.commitStyles()
        oldAnimTime = it.currentTime
        it.cancel()
    }
    (this.asDynamic().__rock__hiddenAnim2 as? Animation)?.let {
        it.cancel()
    }
    this.asDynamic().__rock__goalHidden = value
    hidden = false
    val parent = generateSequence(this) { it.parentElement as? HTMLElement }.drop(1)
        .firstOrNull { !it.classList.contains("toggle-button") } ?: return
    val parentStyle = window.getComputedStyle(parent)
    val x =
        parentStyle.display == "flex" && parentStyle.flexDirection.contains("row")// && myStyle.width.none { it.isDigit() }
    val y =
        parentStyle.display == "flex" && parentStyle.flexDirection.contains("column")// && myStyle.height.none { it.isDigit() }

    val before = js("{}")
    val after = js("{}")
    val full = if(value) before else after
    val fullTransform = ArrayList<String>()
    val gone = if(value) after else before
    val goneTransform = ArrayList<String>()

    var fullWidth = ""
    var fullHeight = ""
    var marginLeft = ""
    var marginRight = ""
    var marginTop = ""
    var marginBottom = ""
    var paddingLeft = ""
    var paddingRight = ""
    var paddingTop = ""
    var paddingBottom = ""
    if(hidden) {
        hidden = false
        fullWidth = myStyle.width
        fullHeight = myStyle.height
        marginLeft = myStyle.marginLeft
        marginRight = myStyle.marginRight
        marginTop = myStyle.marginTop
        marginBottom = myStyle.marginBottom
        paddingLeft = myStyle.paddingLeft
        paddingRight = myStyle.paddingRight
        paddingTop = myStyle.paddingTop
        paddingBottom = myStyle.paddingBottom
        hidden = true
    } else {
        fullWidth = myStyle.width
        fullHeight = myStyle.height
        marginLeft = myStyle.marginLeft
        marginRight = myStyle.marginRight
        marginTop = myStyle.marginTop
        marginBottom = myStyle.marginBottom
        paddingLeft = myStyle.paddingLeft
        paddingRight = myStyle.paddingRight
        paddingTop = myStyle.paddingTop
        paddingBottom = myStyle.paddingBottom
    }

    if(x) {
        goneTransform.add("scaleX(0)")
        fullTransform.add("scaleX(1)")
        gone.marginLeft = "0px"
        gone.paddingLeft = "0px"
        gone.marginRight = "0px"
        gone.paddingRight = "0px"
        full.marginLeft = marginLeft
        full.paddingLeft = paddingLeft
        full.marginRight = marginRight
        full.paddingRight = paddingRight
        gone.width = "0px"
        full.width = fullWidth
    }
    if(y) {
        goneTransform.add("scaleY(0)")
        fullTransform.add("scaleY(1)")
        gone.marginTop = "0px"
        gone.paddingTop = "0px"
        gone.marginBottom = "0px"
        gone.paddingBottom = "0px"
        full.marginTop = marginTop
        full.paddingTop = paddingTop
        full.marginBottom = marginBottom
        full.paddingBottom = paddingBottom
        gone.height = "0px"
        full.height = fullHeight
    }
    if(!x && !y) {
        full.opacity = "1"
        gone.opacity = "0"
    }
//    gone.transform = goneTransform.takeIf { it.isNotEmpty() }?.joinToString(" ") ?: "none"
//    full.transform = fullTransform.takeIf { it.isNotEmpty() }?.joinToString(" ") ?: "none"

//    this.animate(
//        arrayOf(
//            js("transform" to ((if(value) fullTransform else goneTransform).takeIf { it.isNotEmpty() }?.joinToString(" ") ?: "none")),
//            js("transform" to ((if(value) goneTransform else fullTransform).takeIf { it.isNotEmpty() }?.joinToString(" ") ?: "none")),
//        ),
//        js(
//            "duration" to totalTime,
////            "easing" to "cubic-bezier(0.5, 1, 0.89, 1)"
////            "easing" to "cubic-bezier(0, 0.55, 0.45, 1)"
//            "easing" to "cubic-bezier(0.16, 1, 0.3, 1)"
////            "easing" to "cubic-bezier(0.33, 1, 0.68, 1)"
//        )
//    ).let {
//        it.currentTime = (totalTime - oldAnimTime).coerceAtLeast(0.0)
//        it.onfinish = { ev ->
//            if(this.asDynamic().__rock__hiddenAnim2 == it) {
//                this.asDynamic().__rock__hiddenAnim2 = null
//            }
//        }
//        it.oncancel = { ev ->
//            if(this.asDynamic().__rock__hiddenAnim2 == it) {
//                this.asDynamic().__rock__hiddenAnim2 = null
//            }
//        }
//        it.onremove = { ev ->
//            if(this.asDynamic().__rock__hiddenAnim2 == it) {
//                this.asDynamic().__rock__hiddenAnim2 = null
//            }
//        }
//        this.asDynamic().__rock__hiddenAnim2 = it
//    }

    this.animate(
        arrayOf(before, after),
        js(
            "duration" to totalTime
        )
    ).let {
        it.currentTime = (totalTime - oldAnimTime).coerceAtLeast(0.0)
        it.onfinish = { ev ->
            if(this.asDynamic().__rock__hiddenAnim == it) {
                hidden = value
                classList.remove("animatingShowHide")
                this.asDynamic().__rock__hiddenAnim = null
            }
        }
        it.oncancel = { ev ->
            if(this.asDynamic().__rock__hiddenAnim == it) {
                hidden = value
                classList.remove("animatingShowHide")
                this.asDynamic().__rock__hiddenAnim = null
            }
        }
        it.onremove = { ev ->
            if(this.asDynamic().__rock__hiddenAnim == it) {
                hidden = value
                classList.remove("animatingShowHide")
                this.asDynamic().__rock__hiddenAnim = null
            }
        }
        this.asDynamic().__rock__hiddenAnim = it
    }
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
inline fun HTMLElement.animate(keyframes: Array<dynamic>, options: dynamic): Animation =
    this.asDynamic().animate(keyframes, options) as Animation

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
inline fun HTMLElement.getAnimations(): Array<Animation> = this.asDynamic().getAnimations as Array<Animation>
external interface Animation {
    var oncancel: ((Event) -> Unit)?
    var onfinish: ((Event) -> Unit)?
    var onremove: ((Event) -> Unit)?
    fun cancel()
    fun commitStyles()
    fun finish()
    fun pause()
    fun play()
    fun reverse()
    var currentTime: Double
    var startTime: Double
}