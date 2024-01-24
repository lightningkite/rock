package com.lightningkite.rock.views

import com.lightningkite.rock.models.*
import kotlinx.browser.document
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSStyleSheet
import org.w3c.dom.css.get

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
        style("*", mapOf("box-sizing" to "border-box", "line-height" to "unset"))
        style("h1", mapOf("font-size" to "2rem"))
        style("h2", mapOf("font-size" to "1.6rem"))
        style("h3", mapOf("font-size" to "1.4rem"))
        style("h4", mapOf("font-size" to "1.3rem"))
        style("h5", mapOf("font-size" to "1.2rem"))
        style("h6", mapOf("font-size" to "1.1rem"))
        style(".subtext", mapOf("font-size" to "0.8rem"))
//        style.visibility = if (value) "visible" else "hidden"
        style(".visibleOnParentHover", mapOf("visibility" to "hidden"))
        style(":hover>.visibleOnParentHover", mapOf("visibility" to "visible"))
        style(":hover.visibleOnParentHover", mapOf("visibility" to "visible"))
        style("body", mapOf(
            "height" to "100vh",
            "max-height" to "100vh",
            "max-width" to "100vw",
            "--foreground" to "gray",
        ))

        style("body > div", mapOf(
            "height" to "100vh",
            "max-height" to "100vh",
            "max-width" to "100vw",
        ))

        style("a", mapOf(
            "text-decoration" to "none",
            "color" to "unset",
        ))

        style("a:visited", mapOf(
            "color" to "unset",
        ))

        style("button, input, textarea, select", mapOf(
            "background" to "none",
            "border-width" to "0",
            "outline-width" to "0",
            "font" to "unset",
            "color" to "unset",
            "text-align" to "start",
        ))

        style("input.sameThemeText, textarea.sameThemeText", mapOf(
            "border-bottom-style" to "solid",
            "border-bottom-width" to "1px",
            "border-radius" to "0",
        ))

        style("input.sameThemeText:focus, textarea.sameThemeText:focus", mapOf(
            "border-radius" to "0",
        ))

        style("input:focus textarea:focus", mapOf(
            "outline" to "inherit",
        ))

        style(".toggle-button", mapOf(
            "display" to "flex",
            "align-items" to "center",
        ))

        style(".toggle-button > span", mapOf(
            "flex-grow" to "1",
            "flex-shrink" to "1",
        ))

        style(".spinner", mapOf(
            "width" to "32px !important",
            "height" to "32px !important",
            "opacity" to "0.5 !important",
            "background" to "none !important",
            "box-shadow" to "none !important",
            "border-style" to "solid !important",
            "border-color" to "var(--foreground) var(--foreground) var(--foreground) transparent !important",
            "border-width" to "5px !important",
            "border-radius" to "50% !important",
            "animation" to "spin 2s infinite linear !important",
        ))

        style(".rock-swap", mapOf(
            "position" to "relative",
        ))

        style(".rock-swap > *", mapOf(
            "position" to "absolute",
            "top" to "0",
            "left" to "0",
            "right" to "0",
            "bottom" to "0",
            "max-width" to "100%",
            "max-height" to "100%",
        ))

        style(".rock-stack", mapOf(
            "display" to "grid !important",
            "grid-template-columns" to "100%",
            "grid-template-rows" to "100%",
        ))

        style(".rock-stack > *", mapOf(
            "grid-column-start" to "1",
            "grid-column-end" to "1",
            "grid-row-start" to "1",
            "grid-row-end" to "1",
            "align-self" to "stretch",
            "justify-self" to "stretch",
        ))

        style(".rock-stack > .hStart", mapOf(
            "justify-self" to "start",
        ))

        style(".rock-stack > .hCenter", mapOf(
            "justify-self" to "center",
        ))

        style(".rock-stack > .hStretch", mapOf(
            "justify-self" to "stretch",
        ))

        style(".rock-stack > .hEnd", mapOf(
            "justify-self" to "end",
        ))

        style(".rock-stack > .vStart", mapOf(
            "align-self" to "start",
        ))

        style(".rock-stack > .vCenter", mapOf(
            "align-self" to "center",
        ))

        style(".rock-stack > .vStretch", mapOf(
            "align-self" to "stretch",
        ))

        style(".rock-stack > .vEnd", mapOf(
            "align-self" to "end",
        ))

        style(".rock-row", mapOf(
            "display" to "flex",
            "flex-direction" to "row",)
        )
        style(".rock-row > *", mapOf(
            "max-width" to "unset",
        ))

        style(".rock-row > .vStart", mapOf(
            "align-self" to "start",
        ))

        style(".rock-row > .vCenter", mapOf(
            "align-self" to "center",
        ))

        style(".rock-row > .vStretch", mapOf(
            "align-self" to "stretch",
        ))

        style(".rock-row > .vEnd", mapOf(
            "align-self" to "end",
        ))

        style(".rock-col", mapOf(
            "display" to "flex",
            "flex-direction" to "column",
        ))

        style(".rock-col > *", mapOf(
            "max-height" to "unset",
        ))

        style(".rock-col > .hStart", mapOf(
            "align-self" to "start",
        ))

        style(".rock-col > .hCenter", mapOf(
            "align-self" to "center",
        ))

        style(".rock-col > .hStretch", mapOf(
            "align-self" to "stretch",
        ))

        style(".rock-col > .hEnd", mapOf(
            "align-self" to "end",
        ))

        style("p.loading:not(.inclBack), h1.loading:not(.inclBack), h2.loading:not(.inclBack), h3.loading:not(.inclBack), h4.loading:not(.inclBack), h5.loading:not(.inclBack), h6.loading:not(.inclBack), img.loading:not(.inclBack), input.loading:not(.inclBack), select.loading:not(.inclBack), textarea.loading:not(.inclBack)", mapOf(
            "min-height" to "1em",
            "background" to "color-mix(in srgb, currentColor, transparent 70%) !important",
            "animation" to "flickerAnimation 2s infinite !important",
            "animation-timing-function" to "linear",
        ))

        style("button.loading", mapOf(
            "position" to "relative",)
        )
        style("button.loading:after", mapOf(
            "content" to "\"\"",
            "position" to "absolute",
            "top" to "calc(50% - 20px)",
            "left" to "calc(50% - 20px)",
            "width" to "32px !important",
            "height" to "32px !important",
            "opacity" to "0.5 !important",
            "background" to "none !important",
            "box-shadow" to "none !important",
            "border-style" to "solid !important",
            "border-color" to "var(--foreground) var(--foreground) var(--foreground) transparent !important",
            "border-width" to "6px !important",
            "border-radius" to "50% !important",
            "animation" to "spin 2s infinite linear !important",
        ))

        style(".clickable", mapOf(
            "cursor" to "pointer",
        ))

        style(".switch", mapOf(
            "position" to "relative",
            "height" to "1.5rem",
            "width" to "3rem",
            "cursor" to "pointer",
            "appearance" to "none",
            "-webkit-appearance" to "none",
            "border-radius" to "9999px !important",
            "background-color" to "rgba(100, 116, 139, 0.377)",
            "transition" to "all .3s ease",
        ))

        style(".switch:checked", mapOf(
            "background-color" to "rgba(236, 72, 153, 1)",
        ))

        style(".switch::before", mapOf(
            "position" to "absolute",
            "content" to "\"\"",
            "left" to "calc(1.5rem - 1.6rem)",
            "top" to "calc(1.5rem - 1.6rem)",
            "display" to "block",
            "height" to "1.6rem",
            "width" to "1.6rem",
            "cursor" to "pointer",
            "border" to "1px solid rgba(100, 116, 139, 0.527)",
            "border-radius" to "9999px !important",
            "background-color" to "rgba(255, 255, 255, 1)",
            "box-shadow" to "0 3px 10px rgba(100, 116, 139, 0.327)",
            "transition" to "all .3s ease",
        ))

        style(".switch:hover::before", mapOf(
            "box-shadow" to "0 0 0px 8px rgba(0, 0, 0, .15)"
        ))

        style(".switch:checked:hover::before", mapOf(
            "box-shadow"  to "0 0 0px 8px rgba(236, 72, 153, .15)"
        ))

        style(".switch:checked:before", mapOf(
            "transform"  to "translateX(100%)",
            "border-color" to "rgba(236, 72, 153, 1)",
        ))

        style(".crowd", mapOf(
            "padding" to "0 !important",
        ))

        style(".crowd > *", mapOf(
            "margin" to "0 !important",
        ))

        style(".rock-label", mapOf(
            "display" to "flex",
            "flex-direction" to "column",
            "align-items" to "stretch",
        ))

        style(".rock-label > :nth-child(1).inclMargin", mapOf(
            "font-size" to "0.8rem",
            "margin-bottom" to "0px",
        ))

        style(".rock-label > :nth-child(2).inclMargin", mapOf(
            "margin-top" to "0.25rem",
        ))

        style("*", mapOf(
            "scrollbar-color" to "#999 #0000",
            "scrollbar-width" to "thin",
            "scrollbar-gutter" to "auto",
            "flex-shrink" to "0",
            "max-width" to "calc(100% - var(--margin, 0) * 2)",
            "max-height" to "calc(100% - var(--margin, 0) * 2)",
            "min-height" to "0",
            "min-width" to "0",
        ))

        style("input", mapOf(
            "min-height" to "2rem",
            "min-width" to "2rem",
        ))

        style("::placeholder", mapOf(
            "color" to "var(--foreground)",
            "opacity" to "0.3",
        ))

        style(".rock-separator", mapOf(
            "background-color" to "var(--foreground)",
            "opacity" to "0.25",
            "min-width" to "1px",
            "min-height" to "1px",
        ))

        style("[hidden]", mapOf(
            "visibility" to "hidden !important",
            "display" to "none !important",
        ))

        style("iframe#webpack-dev-server-client-overlay", mapOf(
            "display" to "none !important"
        ))

        style(".recycler", mapOf(
            "overflow-y" to "auto"
        ))

        style(".recycler > *", mapOf(
            "max-height" to "unset",
        ))

        style(".recycler-horz", mapOf(
            "display" to "flex",
            "flex-direction" to "row",
            "overflow-x" to "auto",
        ))

        style(".recycler-horz > *", mapOf(
            "max-width" to "unset",
        ))

        style(".recycler-grid", mapOf(
            "display" to "flex",
            "flex-direction" to "row",
            "overflow-x" to "auto",
        ))

        style(".scroll-vertical > *", mapOf(
            "max-height" to "unset",
        ))

        style(".scroll-vertical", mapOf(
            "overflow-y" to "auto",
        ))

        style(".scroll-horizontal > *", mapOf(
            "max-width" to "unset",
        ))

        style(".scroll-horizontal", mapOf(
            "overflow-x" to "auto",
        ))

        style("::-webkit-scrollbar", mapOf(
            "background" to "#0000",
        ))

        style("::-webkit-scrollbar-thumb", mapOf(
            "background" to "color-mix(in srgb, currentColor 20%, transparent)",
            "-webkit-border-radius" to "4px",
        ))

        style("::-webkit-scrollbar-corner", mapOf(
            "background" to "#0000"
        ))

        rule("""
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
        """.trimIndent())
        rule("""
            @keyframes spin {
                from {
                    transform: rotate(0deg);
                }
                to {
                    transform: rotate(360deg);
                }
            }
        """.trimIndent())
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

    private val themeInteractiveHandled = HashSet<String>()
    fun themeInteractive(theme: Theme): String {

        theme(theme.down(), ".theme-${theme.id}.clickable:active", includeBackAlways = true)
        theme(theme.hover(), ".theme-${theme.id}.clickable:hover", includeBackAlways = true)
        theme(theme.disabled(), ".theme-${theme.id}.clickable:disabled")

        theme(theme.unselected(), ".toggle-button > .theme-${theme.id}.clickable")
        theme(theme.unselected().hover(), ".toggle-button > .theme-${theme.id}.clickable:hover")
        theme(theme.unselected().disabled(), ".toggle-button > .theme-${theme.id}.clickable:disabled")

        theme(theme.selected(), ".theme-${theme.id}.clickable:checked", includeBackAlways = true)
        theme(theme.selected().hover(), ".theme-${theme.id}.clickable:checked:hover", includeBackAlways = true)
        theme(theme.selected(), ":checked+.theme-${theme.id}.clickable.checkResponsive", includeBackAlways = true)
        theme(theme.selected().hover(), ":checked+.theme-${theme.id}.clickable:hover.checkResponsive", includeBackAlways = true )
        theme(theme.selected().disabled(), ".theme-${theme.id}.clickable:checked:disabled", includeBackAlways = true)

        return theme(theme)
    }

    private val themeHandled = HashSet<String>()
    fun theme(theme: Theme, asSelector: String = ".theme-${theme.id}", includeBackAlways: Boolean = false): String {
        if (!themeHandled.add(asSelector)) return "theme-${theme.id}"
        val back = when (val it = theme.background) {
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
        val border = mapOf(
            "outline-width" to theme.outlineWidth.value,
            "box-shadow" to theme.elevation.toBoxShadow(),
        )
        style(
            asSelector, mapOf(
                "color" to theme.foreground.toCss(),
                "--spacing" to theme.spacing.value,
                "--foreground" to theme.foreground.toCss(),
                "font-family" to font(theme.body.font),
                "font-weight" to if (theme.body.bold) "bold" else "normal",
                "font-style" to if (theme.body.italic) "italic" else "normal",
                "text-transform" to if (theme.body.allCaps) "uppercase" else "none",
                "line-height" to theme.body.lineSpacingMultiplier.toString(),
                "letter-spacing" to theme.body.additionalLetterSpacing.toString(),
                "outline-style" to if (theme.outlineWidth != 0.px) "solid" else "none",
                "outline-color" to theme.outline.toCss(),
                "outline-width" to 0.px.value,
                "border-top-left-radius" to theme.cornerRadii.topLeft.value,
                "border-top-right-radius" to theme.cornerRadii.topRight.value,
                "border-bottom-left-radius" to theme.cornerRadii.bottomLeft.value,
                "border-bottom-right-radius" to theme.cornerRadii.bottomRight.value,
                "transition-property" to "color, background-image, background-color, outline-color, box-shadow, border-radius",
                "transition-duration" to "0.15s",
                "transition-timing-function" to "linear",
                "transition-delay" to "0s",
            ) + (if (includeBackAlways) back + border else mapOf())
        )
        style("$asSelector.inclMargin", mapOf("margin" to theme.spacing.value, "--margin" to theme.spacing.value))
        style("$asSelector.addPadding", mapOf("padding" to theme.spacing.value))
        style(
            "$asSelector.title", mapOf(
                "font-family" to font(theme.title.font),
                "font-weight" to if (theme.title.bold) "bold" else "normal",
                "font-style" to if (theme.title.italic) "italic" else "normal",
                "text-transform" to if (theme.title.allCaps) "uppercase" else "none",
                "line-height" to theme.title.lineSpacingMultiplier.toString(),
                "letter-spacing" to theme.title.additionalLetterSpacing.toString(),
            )
        )


        style("$asSelector.sameThemeText", mapOf("border-bottom-color" to theme.hover().background.toCss()))
        style(
            "$asSelector.sameThemeText:hover",
            mapOf("border-bottom-color" to theme.hover().hover().background.toCss())
        )
        style(
            "$asSelector.sameThemeText:hover:focus",
            mapOf("border-bottom-color" to theme.selected().background.toCss())
        )
        style("$asSelector.sameThemeText:focus", mapOf("border-bottom-color" to theme.selected().background.toCss()))
        if (includeBackAlways) {
            style("$asSelector.inclBack", mapOf("padding" to theme.spacing.value))
        } else {
            style("$asSelector.inclBack", back)
            style("$asSelector.inclBorder", border + mapOf("padding" to theme.spacing.value))
        }
        style("$asSelector.dismissBackground", mapOf(
            "border-radius" to "0",
            "margin" to "0",
            "--margin" to "0",
            "outline-width" to "0",
            "z-index" to "999",
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
        })
        return "theme-${theme.id}"
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
}