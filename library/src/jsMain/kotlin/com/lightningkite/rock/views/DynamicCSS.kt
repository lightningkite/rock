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
        style("*", mapOf("box-sizing" to "border-box"))
        style("h1", mapOf("font-size" to "2rem"))
        style("h2", mapOf("font-size" to "1.6rem"))
        style("h3", mapOf("font-size" to "1.4rem"))
        style("h4", mapOf("font-size" to "1.3rem"))
        style("h5", mapOf("font-size" to "1.2rem"))
        style("h6", mapOf("font-size" to "1.1rem"))
    }

    fun rule(rule: String, index: Int = 0) = customStyleSheet.insertRule(rule.also { println(it) }, index)

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
        return font.cssFontFamilyName
    }

    fun style(selector: String, map: Map<String, String>) {
        rule(
            """$selector { ${map.entries.joinToString("; ") { "${it.key}: ${it.value};" }} }""",
            0
        )
    }

    private val themeInteractiveHandled = HashSet<String>()
    fun themeInteractive(theme: Theme): String {
        theme(theme.down(), ".theme-${theme.id}.interactive:active")
        theme(theme.hover(), ".theme-${theme.id}.interactive:hover")
        theme(theme.selected(), ".theme-${theme.id}.interactive~:checked")
        theme(theme.selected().hover(), ".theme-${theme.id}.interactive~:checked:hover")
        theme(theme.disabled(), ".theme-${theme.id}.interactive:disabled")
        return theme(theme)
    }

    private val themeHandled = HashSet<String>()
    fun theme(theme: Theme, asSelector: String = ".theme-${theme.id}"): String {
        if (!themeHandled.add(asSelector)) return "theme-${theme.id}"
        style(
            asSelector, mapOf(
                "color" to theme.foreground.toCss(),
                "font-family" to font(theme.body.font),
                "font-weight" to if (theme.body.bold) "bold" else "normal",
                "font-style" to if (theme.body.italic) "italic" else "normal",
                "text-transform" to if (theme.body.allCaps) "uppercase" else "none",
                "line-height" to theme.body.lineSpacingMultiplier.toString(),
                "letter-spacing" to theme.body.additionalLetterSpacing.toString(),
                "transition-property" to "color, background-image, background-color, border-color, box-shadow, border",
                "transition-duration" to "0.25s",
                "transition-timing-function" to "linear",
                "transition-delay" to "0s",
            )
        )
        style(
            "$asSelector.inclBack", when (val it = theme.background) {
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
            "$asSelector.inclMargin", mapOf(
                "margin" to theme.spacing.value,
            )
        )
        style(
            "$asSelector.inclBorder", mapOf(
                "border-style" to if (theme.outlineWidth != 0.px) "solid" else "none",
                "border-color" to theme.outline.toCss(),
                "padding" to theme.spacing.value,
                "box-shadow" to theme.elevation.toBoxShadow(),
                "border-top-left-radius" to theme.cornerRadii.topLeft.value,
                "border-top-right-radius" to theme.cornerRadii.topRight.value,
                "border-bottom-left-radius" to theme.cornerRadii.bottomLeft.value,
                "border-bottom-right-radius" to theme.cornerRadii.bottomRight.value,
            )
        )
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
        return "theme-${theme.id}"
    }

    private fun Dimension.toBoxShadow(): String {
        if (value == "0px")
            return "none"
        val offsetX = 0.px.value
        val offsetY = value
        val blur = 4.px.value
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