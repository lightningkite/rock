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
    }

    fun rule(rule: String, index: Int = 0) = customStyleSheet.insertRule(rule, index)

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

        theme(theme.down(), ".theme-${theme.id}.clickable:active", includeBackAlways = true)
        theme(theme.hover(), ".theme-${theme.id}.clickable:hover", includeBackAlways = true)
        theme(theme.disabled(), ".theme-${theme.id}.clickable:disabled")

        theme(theme.unselected(), ".toggle-button > .theme-${theme.id}.clickable", includeBackAlways = true)
        theme(theme.unselected().hover(), ".toggle-button > .theme-${theme.id}.clickable:hover", includeBackAlways = true)
        theme(theme.unselected().disabled(), ".toggle-button > .theme-${theme.id}.clickable:disabled", includeBackAlways = true)

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
        } + mapOf(
            "color" to theme.foreground.toCss(),
            "--spacing" to theme.spacing.value,
            "--foreground" to theme.foreground.toCss(),
            "font-family" to font(theme.body.font),
            "font-weight" to if (theme.body.bold) "bold" else "normal",
            "font-style" to if (theme.body.italic) "italic" else "normal",
            "text-transform" to if (theme.body.allCaps) "uppercase" else "none",
            "line-height" to theme.body.lineSpacingMultiplier.toString(),
            "letter-spacing" to theme.body.additionalLetterSpacing.toString(),
        )
        val border = mapOf(
            "outline-width" to theme.outlineWidth.value,
            "box-shadow" to theme.elevation.toBoxShadow(),
        )
        style(
            asSelector, mapOf(
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
        style("$asSelector.inclMargin", mapOf("margin" to theme.spacing.value))
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
        style("$asSelector.dismissBackground", mapOf("border-radius" to "0", "margin" to "0", "outline-width" to "0", "opacity" to "0.5"))
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