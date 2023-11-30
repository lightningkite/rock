package com.lightningkite.rock.dom

import org.apache.commons.lang3.StringEscapeUtils.escapeHtml4
import java.lang.Appendable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSSStyleDeclarationImpl(): CSSStyleDeclaration() {
}
actual abstract class CSSStyleDeclaration {
    val priorities = HashMap<String, String>()
    val properties = HashMap<String, String>()
    class PropertyDelegate(val key: String): ReadWriteProperty<CSSStyleDeclaration, String> {
        override fun getValue(thisRef: CSSStyleDeclaration, property: KProperty<*>): String = thisRef.properties[key] ?: ""
        override fun setValue(thisRef: CSSStyleDeclaration, property: KProperty<*>, value: String) {
            thisRef.properties[key] = value
        }
    }
    actual open var cssText: String by PropertyDelegate("text")
    actual open var cssFloat: String by PropertyDelegate("float")
    actual open var alignContent: String by PropertyDelegate("align-content")
    actual open var alignItems: String by PropertyDelegate("align-items")
    actual open var alignSelf: String by PropertyDelegate("align-self")
    actual open var animation: String by PropertyDelegate("animation")
    actual open var animationDelay: String by PropertyDelegate("animation-delay")
    actual open var animationDirection: String by PropertyDelegate("animation-direction")
    actual open var animationDuration: String by PropertyDelegate("animation-duration")
    actual open var animationFillMode: String by PropertyDelegate("animation-fill-mode")
    actual open var animationIterationCount: String by PropertyDelegate("animation-iteration-count")
    actual open var animationName: String by PropertyDelegate("animation-name")
    actual open var animationPlayState: String by PropertyDelegate("animation-play-state")
    actual open var animationTimingFunction: String by PropertyDelegate("animation-timing-function")
    actual open var backfaceVisibility: String by PropertyDelegate("backface-visibility")
    actual open var background: String by PropertyDelegate("background")
    actual open var backgroundAttachment: String by PropertyDelegate("background-attachment")
    actual open var backgroundClip: String by PropertyDelegate("background-clip")
    actual open var backgroundColor: String by PropertyDelegate("background-color")
    actual open var backgroundImage: String by PropertyDelegate("background-image")
    actual open var backgroundOrigin: String by PropertyDelegate("background-origin")
    actual open var backgroundPosition: String by PropertyDelegate("background-position")
    actual open var backgroundRepeat: String by PropertyDelegate("background-repeat")
    actual open var backgroundSize: String by PropertyDelegate("background-size")
    actual open var border: String by PropertyDelegate("border")
    actual open var borderBottom: String by PropertyDelegate("border-bottom")
    actual open var borderBottomColor: String by PropertyDelegate("border-bottom-color")
    actual open var borderBottomLeftRadius: String by PropertyDelegate("border-bottom-left-radius")
    actual open var borderBottomRightRadius: String by PropertyDelegate("border-bottom-right-radius")
    actual open var borderBottomStyle: String by PropertyDelegate("border-bottom-style")
    actual open var borderBottomWidth: String by PropertyDelegate("border-bottom-width")
    actual open var borderCollapse: String by PropertyDelegate("border-collapse")
    actual open var borderColor: String by PropertyDelegate("border-color")
    actual open var borderImage: String by PropertyDelegate("border-image")
    actual open var borderImageOutset: String by PropertyDelegate("border-image-outset")
    actual open var borderImageRepeat: String by PropertyDelegate("border-image-repeat")
    actual open var borderImageSlice: String by PropertyDelegate("border-image-slice")
    actual open var borderImageSource: String by PropertyDelegate("border-image-source")
    actual open var borderImageWidth: String by PropertyDelegate("border-image-width")
    actual open var borderLeft: String by PropertyDelegate("border-left")
    actual open var borderLeftColor: String by PropertyDelegate("border-left-color")
    actual open var borderLeftStyle: String by PropertyDelegate("border-left-style")
    actual open var borderLeftWidth: String by PropertyDelegate("border-left-width")
    actual open var borderRadius: String by PropertyDelegate("border-radius")
    actual open var borderRight: String by PropertyDelegate("border-right")
    actual open var borderRightColor: String by PropertyDelegate("border-right-color")
    actual open var borderRightStyle: String by PropertyDelegate("border-right-style")
    actual open var borderRightWidth: String by PropertyDelegate("border-right-width")
    actual open var borderSpacing: String by PropertyDelegate("border-spacing")
    actual open var borderStyle: String by PropertyDelegate("border-style")
    actual open var borderTop: String by PropertyDelegate("border-top")
    actual open var borderTopColor: String by PropertyDelegate("border-top-color")
    actual open var borderTopLeftRadius: String by PropertyDelegate("border-top-left-radius")
    actual open var borderTopRightRadius: String by PropertyDelegate("border-top-right-radius")
    actual open var borderTopStyle: String by PropertyDelegate("border-top-style")
    actual open var borderTopWidth: String by PropertyDelegate("border-top-width")
    actual open var borderWidth: String by PropertyDelegate("border-width")
    actual open var bottom: String by PropertyDelegate("bottom")
    actual open var boxDecorationBreak: String by PropertyDelegate("box-decoration-break")
    actual open var boxShadow: String by PropertyDelegate("box-shadow")
    actual open var boxSizing: String by PropertyDelegate("box-sizing")
    actual open var breakAfter: String by PropertyDelegate("break-after")
    actual open var breakBefore: String by PropertyDelegate("break-before")
    actual open var breakInside: String by PropertyDelegate("break-inside")
    actual open var captionSide: String by PropertyDelegate("caption-side")
    actual open var clear: String by PropertyDelegate("clear")
    actual open var clip: String by PropertyDelegate("clip")
    actual open var color: String by PropertyDelegate("color")
    actual open var columnCount: String by PropertyDelegate("column-count")
    actual open var columnFill: String by PropertyDelegate("column-fill")
    actual open var columnGap: String by PropertyDelegate("column-gap")
    actual open var columnRule: String by PropertyDelegate("column-rule")
    actual open var columnRuleColor: String by PropertyDelegate("column-rule-color")
    actual open var columnRuleStyle: String by PropertyDelegate("column-rule-style")
    actual open var columnRuleWidth: String by PropertyDelegate("column-rule-width")
    actual open var columnSpan: String by PropertyDelegate("column-span")
    actual open var columnWidth: String by PropertyDelegate("column-width")
    actual open var columns: String by PropertyDelegate("columns")
    actual open var content: String by PropertyDelegate("content")
    actual open var counterIncrement: String by PropertyDelegate("counter-increment")
    actual open var counterReset: String by PropertyDelegate("counter-reset")
    actual open var cursor: String by PropertyDelegate("cursor")
    actual open var direction: String by PropertyDelegate("direction")
    actual open var display: String by PropertyDelegate("display")
    actual open var emptyCells: String by PropertyDelegate("empty-cells")
    actual open var filter: String by PropertyDelegate("filter")
    actual open var flex: String by PropertyDelegate("flex")
    actual open var flexBasis: String by PropertyDelegate("flex-basis")
    actual open var flexDirection: String by PropertyDelegate("flex-direction")
    actual open var flexFlow: String by PropertyDelegate("flex-flow")
    actual open var flexGrow: String by PropertyDelegate("flex-grow")
    actual open var flexShrink: String by PropertyDelegate("flex-shrink")
    actual open var flexWrap: String by PropertyDelegate("flex-wrap")
    actual open var font: String by PropertyDelegate("font")
    actual open var fontFamily: String by PropertyDelegate("font-family")
    actual open var fontFeatureSettings: String by PropertyDelegate("font-feature-settings")
    actual open var fontKerning: String by PropertyDelegate("font-kerning")
    actual open var fontLanguageOverride: String by PropertyDelegate("font-language-override")
    actual open var fontSize: String by PropertyDelegate("font-size")
    actual open var fontSizeAdjust: String by PropertyDelegate("font-size-adjust")
    actual open var fontStretch: String by PropertyDelegate("font-stretch")
    actual open var fontStyle: String by PropertyDelegate("font-style")
    actual open var fontSynthesis: String by PropertyDelegate("font-synthesis")
    actual open var fontVariant: String by PropertyDelegate("font-variant")
    actual open var fontVariantAlternates: String by PropertyDelegate("font-variant-alternates")
    actual open var fontVariantCaps: String by PropertyDelegate("font-variant-caps")
    actual open var fontVariantEastAsian: String by PropertyDelegate("font-variant-east-asian")
    actual open var fontVariantLigatures: String by PropertyDelegate("font-variant-ligatures")
    actual open var fontVariantNumeric: String by PropertyDelegate("font-variant-numeric")
    actual open var fontVariantPosition: String by PropertyDelegate("font-variant-position")
    actual open var fontWeight: String by PropertyDelegate("font-weight")
    actual open var hangingPunctuation: String by PropertyDelegate("hanging-punctuation")
    actual open var height: String by PropertyDelegate("height")
    actual open var hyphens: String by PropertyDelegate("hyphens")
    actual open var imageOrientation: String by PropertyDelegate("image-orientation")
    actual open var imageRendering: String by PropertyDelegate("image-rendering")
    actual open var imageResolution: String by PropertyDelegate("image-resolution")
    actual open var imeMode: String by PropertyDelegate("ime-mode")
    actual open var justifyContent: String by PropertyDelegate("justify-content")
    actual open var left: String by PropertyDelegate("left")
    actual open var letterSpacing: String by PropertyDelegate("letter-spacing")
    actual open var lineBreak: String by PropertyDelegate("line-break")
    actual open var lineHeight: String by PropertyDelegate("line-height")
    actual open var listStyle: String by PropertyDelegate("list-style")
    actual open var listStyleImage: String by PropertyDelegate("list-style-image")
    actual open var listStylePosition: String by PropertyDelegate("list-style-position")
    actual open var listStyleType: String by PropertyDelegate("list-style-type")
    actual open var margin: String by PropertyDelegate("margin")
    actual open var marginBottom: String by PropertyDelegate("margin-bottom")
    actual open var marginLeft: String by PropertyDelegate("margin-left")
    actual open var marginRight: String by PropertyDelegate("margin-right")
    actual open var marginTop: String by PropertyDelegate("margin-top")
    actual open var mark: String by PropertyDelegate("mark")
    actual open var markAfter: String by PropertyDelegate("mark-after")
    actual open var markBefore: String by PropertyDelegate("mark-before")
    actual open var marks: String by PropertyDelegate("marks")
    actual open var marqueeDirection: String by PropertyDelegate("marquee-direction")
    actual open var marqueePlayCount: String by PropertyDelegate("marquee-play-count")
    actual open var marqueeSpeed: String by PropertyDelegate("marquee-speed")
    actual open var marqueeStyle: String by PropertyDelegate("marquee-style")
    actual open var mask: String by PropertyDelegate("mask")
    actual open var maskType: String by PropertyDelegate("mask-type")
    actual open var maxHeight: String by PropertyDelegate("max-height")
    actual open var maxWidth: String by PropertyDelegate("max-width")
    actual open var minHeight: String by PropertyDelegate("min-height")
    actual open var minWidth: String by PropertyDelegate("min-width")
    actual open var navDown: String by PropertyDelegate("nav-down")
    actual open var navIndex: String by PropertyDelegate("nav-index")
    actual open var navLeft: String by PropertyDelegate("nav-left")
    actual open var navRight: String by PropertyDelegate("nav-right")
    actual open var navUp: String by PropertyDelegate("nav-up")
    actual open var objectFit: String by PropertyDelegate("object-fit")
    actual open var objectPosition: String by PropertyDelegate("object-position")
    actual open var opacity: String by PropertyDelegate("opacity")
    actual open var order: String by PropertyDelegate("order")
    actual open var orphans: String by PropertyDelegate("orphans")
    actual open var outline: String by PropertyDelegate("outline")
    actual open var outlineColor: String by PropertyDelegate("outline-color")
    actual open var outlineOffset: String by PropertyDelegate("outline-offset")
    actual open var outlineStyle: String by PropertyDelegate("outline-style")
    actual open var outlineWidth: String by PropertyDelegate("outline-width")
    actual open var overflowWrap: String by PropertyDelegate("overflow-wrap")
    actual open var overflowX: String by PropertyDelegate("overflow-x")
    actual open var overflowY: String by PropertyDelegate("overflow-y")
    actual open var padding: String by PropertyDelegate("padding")
    actual open var paddingBottom: String by PropertyDelegate("padding-bottom")
    actual open var paddingLeft: String by PropertyDelegate("padding-left")
    actual open var paddingRight: String by PropertyDelegate("padding-right")
    actual open var paddingTop: String by PropertyDelegate("padding-top")
    actual open var pageBreakAfter: String by PropertyDelegate("page-break-after")
    actual open var pageBreakBefore: String by PropertyDelegate("page-break-before")
    actual open var pageBreakInside: String by PropertyDelegate("page-break-inside")
    actual open var perspective: String by PropertyDelegate("perspective")
    actual open var perspectiveOrigin: String by PropertyDelegate("perspective-origin")
    actual open var phonemes: String by PropertyDelegate("phonemes")
    actual open var position: String by PropertyDelegate("position")
    actual open var quotes: String by PropertyDelegate("quotes")
    actual open var resize: String by PropertyDelegate("resize")
    actual open var rest: String by PropertyDelegate("rest")
    actual open var restAfter: String by PropertyDelegate("rest-after")
    actual open var restBefore: String by PropertyDelegate("rest-before")
    actual open var right: String by PropertyDelegate("right")
    actual open var tabSize: String by PropertyDelegate("tab-size")
    actual open var tableLayout: String by PropertyDelegate("table-layout")
    actual open var textAlign: String by PropertyDelegate("text-align")
    actual open var textAlignLast: String by PropertyDelegate("text-align-last")
    actual open var textCombineUpright: String by PropertyDelegate("text-combine-upright")
    actual open var textDecoration: String by PropertyDelegate("text-decoration")
    actual open var textDecorationColor: String by PropertyDelegate("text-decoration-color")
    actual open var textDecorationLine: String by PropertyDelegate("text-decoration-line")
    actual open var textDecorationStyle: String by PropertyDelegate("text-decoration-style")
    actual open var textIndent: String by PropertyDelegate("text-indent")
    actual open var textJustify: String by PropertyDelegate("text-justify")
    actual open var textOrientation: String by PropertyDelegate("text-orientation")
    actual open var textOverflow: String by PropertyDelegate("text-overflow")
    actual open var textShadow: String by PropertyDelegate("text-shadow")
    actual open var textTransform: String by PropertyDelegate("text-transform")
    actual open var textUnderlinePosition: String by PropertyDelegate("text-underline-position")
    actual open var top: String by PropertyDelegate("top")
    actual open var transform: String by PropertyDelegate("transform")
    actual open var transformOrigin: String by PropertyDelegate("transform-origin")
    actual open var transformStyle: String by PropertyDelegate("transform-style")
    actual open var transition: String by PropertyDelegate("transition")
    actual open var transitionDelay: String by PropertyDelegate("transition-delay")
    actual open var transitionDuration: String by PropertyDelegate("transition-duration")
    actual open var transitionProperty: String by PropertyDelegate("transition-property")
    actual open var transitionTimingFunction: String by PropertyDelegate("transition-timing-function")
    actual open var unicodeBidi: String by PropertyDelegate("unicode-bidi")
    actual open var verticalAlign: String by PropertyDelegate("vertical-align")
    actual open var visibility: String by PropertyDelegate("visibility")
    actual open var voiceBalance: String by PropertyDelegate("voice-balance")
    actual open var voiceDuration: String by PropertyDelegate("voice-duration")
    actual open var voicePitch: String by PropertyDelegate("voice-pitch")
    actual open var voicePitchRange: String by PropertyDelegate("voice-pitch-range")
    actual open var voiceRate: String by PropertyDelegate("voice-rate")
    actual open var voiceStress: String by PropertyDelegate("voice-stress")
    actual open var voiceVolume: String by PropertyDelegate("voice-volume")
    actual open var whiteSpace: String by PropertyDelegate("white-space")
    actual open var widows: String by PropertyDelegate("widows")
    actual open var width: String by PropertyDelegate("width")
    actual open var wordBreak: String by PropertyDelegate("word-break")
    actual open var wordSpacing: String by PropertyDelegate("word-spacing")
    actual open var wordWrap: String by PropertyDelegate("word-wrap")
    actual open var writingMode: String by PropertyDelegate("writing-mode")
    actual open var zIndex: String by PropertyDelegate("z-index")
    actual fun getPropertyValue(property: String): String = properties[property] ?: ""
    actual fun getPropertyPriority(property: String): String = priorities[property] ?: ""
    actual fun setProperty(property: String, value: String, priority: String) {
        properties[property] = value
        priorities[property] = priority
    }
    actual fun setPropertyValue(property: String, value: String) {
        properties[property] = value
    }
    actual fun setPropertyPriority(property: String, priority: String) {
        priorities[property] = priority
    }
    actual fun removeProperty(property: String): String {
        priorities.remove(property)
        return properties.remove(property) ?: ""
    }
}

actual abstract class Node {
    val children = ArrayList<Node>()
    actual fun appendChild(node: Node): Node {
        children.add(node)
        return node
    }
    actual fun replaceChild(node: Node, child: Node): Node {
        children.set(children.indexOf(node), child)
        return child
    }
    abstract fun render(out: Appendable)
    actual open var nodeValue: String?
        get() = null
        set(value) {

        }
}
actual abstract class CharacterData: Node() {}
actual open class Text actual constructor(var data: String): CharacterData() {
    actual open val wholeText: String get() = data
    override fun render(out: Appendable) { out.append(escapeHtml4(data)) }
    override var nodeValue: String?
        get() = data
        set(value) { data = value ?: "" }
}
actual abstract class Element: Node() {
    actual open val tagName: String get() = "unknown"
    actual open var className: String by attributeString("class")
    actual open var id: String by attributeString("id")
    actual open var slot: String by attributeString("slot")

    val attributes = HashMap<String, String>()
    val textContent get() = object: ReadWriteProperty<Node, String> {
        override fun getValue(thisRef: Node, property: KProperty<*>): String = (children.getOrNull(0) as? Text)?.data ?: ""
        override fun setValue(thisRef: Node, property: KProperty<*>, value: String) { children.clear(); children.add(Text(value)) }
    }
    fun attributeString(key: String) = object: ReadWriteProperty<Node, String> {
        override fun getValue(thisRef: Node, property: KProperty<*>): String = attributes[key] ?: ""
        override fun setValue(thisRef: Node, property: KProperty<*>, value: String) { attributes[key] = value }
    }
    fun attributeNullableString(key: String) = object: ReadWriteProperty<Node, String?> {
        override fun getValue(thisRef: Node, property: KProperty<*>): String? = attributes[key]
        override fun setValue(thisRef: Node, property: KProperty<*>, value: String?) { if(value != null) attributes[key] = value else attributes.remove(key) }
    }
    fun attributeDouble(key: String) = object: ReadWriteProperty<Node, Double> {
        override fun getValue(thisRef: Node, property: KProperty<*>): Double = attributes[key]?.toDoubleOrNull() ?: 0.0
        override fun setValue(thisRef: Node, property: KProperty<*>, value: Double) { attributes[key] = value.toString() }
    }
    fun attributeInt(key: String) = object: ReadWriteProperty<Node, Int> {
        override fun getValue(thisRef: Node, property: KProperty<*>): Int = attributes[key]?.toIntOrNull() ?: 0
        override fun setValue(thisRef: Node, property: KProperty<*>, value: Int) { attributes[key] = value.toString() }
    }
    fun attributeBoolean(key: String) = object: ReadWriteProperty<Node, Boolean> {
        override fun getValue(thisRef: Node, property: KProperty<*>): Boolean = !attributes[key].isNullOrBlank()
        override fun setValue(thisRef: Node, property: KProperty<*>, value: Boolean) {
            if(value) attributes[key] = "true"
            else attributes.remove(key)
        }
    }

    override fun render(out: Appendable) {
        out.append('<')
        out.append(tagName)
        attributes.entries.forEach { (key, value) ->
            out.append(' ')
            out.append(key)
            out.append("='")
            out.append(value)
            out.append('\'')
        }
        if(children.isEmpty()) {
            out.append("/>")
        } else {
            out.append('>')
            children.forEach { it.render(out) }
            out.append("</")
            out.append(tagName)
            out.append('>')
        }
    }
}

class HTMLElementImpl(override val tagName: String): HTMLElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLElement: Element() {
    actual open var accessKey: String by attributeString("accesskey")
    actual open var dir: String by attributeString("dir")
    actual open var draggable: Boolean by attributeBoolean("draggable")
    actual open var hidden: Boolean by attributeBoolean("hidden")
    actual open var lang: String by attributeString("lang")
    actual open var spellcheck: Boolean by attributeBoolean("spellcheck")
    actual abstract val style: CSSStyleDeclaration  // style
    actual open var title: String by attributeString("title")
    actual open var translate: Boolean by attributeBoolean("translate")
}

class HTMLAnchorElementImpl(override val tagName: String): HTMLAnchorElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
    override var href: String = ""
}
actual abstract class HTMLAnchorElement: HTMLElement() {
    actual open var download: String by attributeString("download")
    actual abstract var href: String  // href
    actual open var hreflang: String by attributeString("hreflang")
    actual open var ping: String by attributeString("ping")
    actual open var rel: String by attributeString("rel")
    actual open var shape: String by attributeString("shape")
    actual open var target: String by attributeString("target")
}
class HTMLAppletElementImpl(override val tagName: String): HTMLAppletElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLAppletElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var alt: String by attributeString("alt")
    actual open var code: String by attributeString("code")
}
class HTMLAreaElementImpl(override val tagName: String): HTMLAreaElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
    override var href: String = ""
}
actual abstract class HTMLAreaElement: HTMLElement() {
    actual open var alt: String by attributeString("alt")
    actual open var coords: String by attributeString("coords")
    actual open var download: String by attributeString("download")
    actual abstract var href: String  // href
    actual open var ping: String by attributeString("ping")
    actual open var rel: String by attributeString("rel")
    actual open var shape: String by attributeString("shape")
    actual open var target: String by attributeString("target")
}
class HTMLMediaElementImpl(override val tagName: String): HTMLMediaElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMediaElement: HTMLElement() {
    actual open var crossOrigin: String? by attributeNullableString("crossorigin")
    actual open var autoplay: Boolean by attributeBoolean("autoplay")
    //  actual open var buffered: String by attributeString("buffered")
    actual open var controls: Boolean by attributeBoolean("controls")
    actual open var loop: Boolean by attributeBoolean("loop")
    actual open var muted: Boolean by attributeBoolean("muted")
    actual open var preload: String by attributeString("preload")
    actual open var src: String by attributeString("src")
}
class HTMLAudioElementImpl(override val tagName: String): HTMLAudioElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLAudioElement: HTMLMediaElement() {
}
class HTMLBaseElementImpl(override val tagName: String): HTMLBaseElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLBaseElement: HTMLElement() {
    actual open var href: String by attributeString("href")
    actual open var target: String by attributeString("target")
}
class HTMLBodyElementImpl(override val tagName: String): HTMLBodyElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLBodyElement: HTMLElement() {
    actual open var background: String by attributeString("background")
}
class HTMLButtonElementImpl(override val tagName: String): HTMLButtonElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLButtonElement: HTMLElement() {
    //  actual open var autofocus: String by attributeString("autofocus")
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var formAction: String by attributeString("formaction")
    actual open var formEnctype: String by attributeString("formenctype")
    actual open var formMethod: String by attributeString("formmethod")
    actual open var formNoValidate: Boolean by attributeBoolean("formnovalidate")
    actual open var formTarget: String by attributeString("formtarget")
    actual open var name: String by attributeString("name")
    actual open var type: String by attributeString("type")
    actual open var value: String by attributeString("value")
}
class HTMLCanvasElementImpl(override val tagName: String): HTMLCanvasElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLCanvasElement: HTMLElement() {
    actual open var height: Int by attributeInt("height")
    actual open var width: Int by attributeInt("width")
}
class HTMLTableCaptionElementImpl(override val tagName: String): HTMLTableCaptionElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableCaptionElement: HTMLElement() {
    actual open var align: String by attributeString("align")
}
class HTMLTableColElementImpl(override val tagName: String): HTMLTableColElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableColElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var span: Int by attributeInt("span")
}
class HTMLDataElementImpl(override val tagName: String): HTMLDataElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLDataElement: HTMLElement() {
    actual open var value: String by attributeString("value")
}
class HTMLModElementImpl(override val tagName: String): HTMLModElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLModElement: HTMLElement() {
    actual open var cite: String by attributeString("cite")
    actual open var dateTime: String by attributeString("datetime")
}
class HTMLDetailsElementImpl(override val tagName: String): HTMLDetailsElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLDetailsElement: HTMLElement() {
    actual open var open: Boolean by attributeBoolean("open")
}
class HTMLDialogElementImpl(override val tagName: String): HTMLDialogElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLDialogElement: HTMLElement() {
    actual open var open: Boolean by attributeBoolean("open")
}
class HTMLEmbedElementImpl(override val tagName: String): HTMLEmbedElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLEmbedElement: HTMLElement() {
    actual open var height: String by attributeString("height")
    actual open var src: String by attributeString("src")
    actual open var type: String by attributeString("type")
    actual open var width: String by attributeString("width")
}
class HTMLFieldSetElementImpl(override val tagName: String): HTMLFieldSetElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLFieldSetElement: HTMLElement() {
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var name: String by attributeString("name")
}
class HTMLFontElementImpl(override val tagName: String): HTMLFontElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLFontElement: HTMLElement() {
    actual open var color: String by attributeString("color")
}
class HTMLFormElementImpl(override val tagName: String): HTMLFormElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLFormElement: HTMLElement() {
    actual open var acceptCharset: String by attributeString("accept-charset")
    actual open var action: String by attributeString("action")
    actual open var autocomplete: String by attributeString("autocomplete")
    actual open var enctype: String by attributeString("enctype")
    actual open var method: String by attributeString("method")
    actual open var name: String by attributeString("name")
    actual open var noValidate: Boolean by attributeBoolean("novalidate")
    actual open var target: String by attributeString("target")
}
class HTMLHRElementImpl(override val tagName: String): HTMLHRElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLHRElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var color: String by attributeString("color")
}
class HTMLIFrameElementImpl(override val tagName: String): HTMLIFrameElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLIFrameElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    //  actual open var allow: String by attributeString("allow")
//  actual open var csp: String by attributeString("csp") Experimental
    actual open var height: String by attributeString("height")
    //  actual open var loading: String by attributeString("loading") Experimental
    actual open var name: String by attributeString("name")
    //  actual open var referrerpolicy: String by attributeString("referrerpolicy")
//  actual open var sandbox: String by attributeString("sandbox")
    actual open var src: String by attributeString("src")
    actual open var srcdoc: String by attributeString("srcdoc")
    actual open var width: String by attributeString("width")
}
class HTMLImageElementImpl(override val tagName: String): HTMLImageElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLImageElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var alt: String by attributeString("alt")
    actual open var border: String by attributeString("border")
    actual open var height: Int by attributeInt("height")
    actual open var isMap: Boolean by attributeBoolean("ismap")
    //  actual open var loading: String by attributeString("loading") Experimental
//  actual open var referrerpolicy: String by attributeString("referrerpolicy")
    actual open var sizes: String by attributeString("sizes")
    actual open var src: String by attributeString("src")
    actual open var srcset: String by attributeString("srcset")
    actual open var useMap: String by attributeString("usemap")
    actual open var width: Int by attributeInt("width")
}
class HTMLInputElementImpl(override val tagName: String): HTMLInputElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLInputElement: HTMLElement() {
    actual open var accept: String by attributeString("accept")
    actual open var alt: String by attributeString("alt")
    actual open var autocomplete: String by attributeString("autocomplete")
    actual open var autofocus: Boolean by attributeBoolean("autofocus")
    actual open var checked: Boolean by attributeBoolean("checked")
    actual open var dirName: String by attributeString("dirname")
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var formAction: String by attributeString("formaction")
    actual open var formEnctype: String by attributeString("formenctype")
    actual open var formMethod: String by attributeString("formmethod")
    actual open var formNoValidate: Boolean by attributeBoolean("formnovalidate")
    actual open var formTarget: String by attributeString("formtarget")
    actual open var height: Int by attributeInt("height")
    //  actual open var list: String by attributeString("list")
    actual open var max: String by attributeString("max")
    actual open var maxLength: Int by attributeInt("maxlength")
    actual open var minLength: Int by attributeInt("minlength")
    actual open var min: String by attributeString("min")
    actual open var multiple: Boolean by attributeBoolean("multiple")
    actual open var name: String by attributeString("name")
    actual open var pattern: String by attributeString("pattern")
    actual open var placeholder: String by attributeString("placeholder")
    actual open var readOnly: Boolean by attributeBoolean("readonly")
    actual open var required: Boolean by attributeBoolean("required")
    actual open var size: Int by attributeInt("size")
    actual open var src: String by attributeString("src")
    actual open var step: String by attributeString("step")
    actual open var type: String by attributeString("type")
    actual open var useMap: String by attributeString("usemap")
    actual open var value: String by attributeString("value")
    actual open var width: Int by attributeInt("width")
}
class HTMLKeygenElementImpl(override val tagName: String): HTMLKeygenElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLKeygenElement: HTMLElement() {
    actual open var autofocus: Boolean by attributeBoolean("autofocus")
    actual open var challenge: String by attributeString("challenge")
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var keytype: String by attributeString("keytype")
    actual open var name: String by attributeString("name")
}
class HTMLLabelElementImpl(override val tagName: String): HTMLLabelElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLLabelElement: HTMLElement() {
    actual open var htmlFor: String by attributeString("for")
//  actual open var form: String by attributeString("form")
}
class HTMLLIElementImpl(override val tagName: String): HTMLLIElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLLIElement: HTMLElement() {
    actual open var value: Int by attributeInt("value")
}
class HTMLLinkElementImpl(override val tagName: String): HTMLLinkElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLLinkElement: HTMLElement() {
    actual open var crossOrigin: String? by attributeNullableString("crossorigin")
    actual open var href: String by attributeString("href")
    actual open var hreflang: String by attributeString("hreflang")
    actual open var media: String by attributeString("media")
    actual open var referrerPolicy: String by attributeString("referrerpolicy")
    actual open var rel: String by attributeString("rel")
    //  actual open var sizes: String by attributeString("sizes")
    actual open var type: String by attributeString("type")
}
class HTMLMapElementImpl(override val tagName: String): HTMLMapElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMapElement: HTMLElement() {
    actual open var name: String by attributeString("name")
}
class HTMLMarqueeElementImpl(override val tagName: String): HTMLMarqueeElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMarqueeElement: HTMLElement() {
    actual open var bgColor: String by attributeString("bgcolor")
    actual open var loop: Int by attributeInt("loop")
}
class HTMLMenuElementImpl(override val tagName: String): HTMLMenuElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMenuElement: HTMLElement() {
    actual open var type: String by attributeString("type")
}
class HTMLMetaElementImpl(override val tagName: String): HTMLMetaElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMetaElement: HTMLElement() {
    actual open var content: String by attributeString("content")
    actual open var httpEquiv: String by attributeString("http-equiv")
    actual open var name: String by attributeString("name")
}
class HTMLMeterElementImpl(override val tagName: String): HTMLMeterElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLMeterElement: HTMLElement() {
    //  actual open var form: String by attributeString("form")
    actual open var high: Double = 0.0  // high
    actual open var low: Double = 0.0  // low
    actual open var max: Double = 0.0  // max
    actual open var min: Double = 0.0  // min
    actual open var optimum: Double = 0.0  // optimum
    actual open var value: Double = 0.0  // value
}
class HTMLObjectElementImpl(override val tagName: String): HTMLObjectElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLObjectElement: HTMLElement() {
    actual open var border: String by attributeString("border")
    actual open var data: String by attributeString("data")
    //  actual open var form: String by attributeString("form")
    actual open var height: String by attributeString("height")
    actual open var name: String by attributeString("name")
    actual open var type: String by attributeString("type")
    actual open var useMap: String by attributeString("usemap")
    actual open var width: String by attributeString("width")
}
class HTMLOListElementImpl(override val tagName: String): HTMLOListElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLOListElement: HTMLElement() {
    actual open var reversed: Boolean by attributeBoolean("reversed")
    actual open var start: Int by attributeInt("start")
    actual open var type: String by attributeString("type")
}
class HTMLOptGroupElementImpl(override val tagName: String): HTMLOptGroupElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLOptGroupElement: HTMLElement() {
    actual open var disabled: Boolean by attributeBoolean("disabled")
    actual open var label: String by attributeString("label")
}
class HTMLOptionElementImpl(override val tagName: String): HTMLOptionElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLOptionElement: HTMLElement() {
    actual open var disabled: Boolean by attributeBoolean("disabled")
    actual open var label: String by attributeString("label")
    actual open var selected: Boolean by attributeBoolean("selected")
    actual open var value: String by attributeString("value")
}
class HTMLOutputElementImpl(override val tagName: String): HTMLOutputElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLOutputElement: HTMLElement() {
    //  actual open var htmlFor: String by attributeString("for")
//  actual open var form: String by attributeString("form")
    actual open var name: String by attributeString("name")
}
class HTMLParamElementImpl(override val tagName: String): HTMLParamElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLParamElement: HTMLElement() {
    actual open var name: String by attributeString("name")
    actual open var value: String by attributeString("value")
}
class HTMLProgressElementImpl(override val tagName: String): HTMLProgressElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLProgressElement: HTMLElement() {
    //  actual open var form: String by attributeString("form")
    actual open var max: Double = 0.0  // max
    actual open var value: Double = 0.0  // value
}
class HTMLQuoteElementImpl(override val tagName: String): HTMLQuoteElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLQuoteElement: HTMLElement() {
    actual open var cite: String by attributeString("cite")
}
class HTMLScriptElementImpl(override val tagName: String): HTMLScriptElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLScriptElement: HTMLElement() {
    actual open var async: Boolean by attributeBoolean("async")
    actual open var charset: String by attributeString("charset")
    actual open var crossOrigin: String? by attributeNullableString("crossorigin")
    actual open var defer: Boolean by attributeBoolean("defer")
    //  actual open var integrity: String by attributeString("integrity")
//  actual open var referrerPolicy: String by attributeString("referrerpolicy")
    actual open var src: String by attributeString("src")
    actual open var type: String by attributeString("type")
}
class HTMLSelectElementImpl(override val tagName: String): HTMLSelectElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLSelectElement: HTMLElement() {
    actual open var autocomplete: String by attributeString("autocomplete")
    actual open var autofocus: Boolean by attributeBoolean("autofocus")
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var multiple: Boolean by attributeBoolean("multiple")
    actual open var name: String by attributeString("name")
    actual open var required: Boolean by attributeBoolean("required")
    actual open var size: Int by attributeInt("size")
}
class HTMLSourceElementImpl(override val tagName: String): HTMLSourceElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLSourceElement: HTMLElement() {
    actual open var media: String by attributeString("media")
    actual open var sizes: String by attributeString("sizes")
    actual open var src: String by attributeString("src")
    actual open var srcset: String by attributeString("srcset")
    actual open var type: String by attributeString("type")
}
class HTMLStyleElementImpl(override val tagName: String): HTMLStyleElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLStyleElement: HTMLElement() {
    actual open var media: String by attributeString("media")
    actual open var type: String by attributeString("type")
}
class HTMLTableElementImpl(override val tagName: String): HTMLTableElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var border: String by attributeString("border")
    actual open var summary: String by attributeString("summary")
}
class HTMLTableSectionElementImpl(override val tagName: String): HTMLTableSectionElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableSectionElement: HTMLElement() {
    actual open var align: String by attributeString("align")
}
class HTMLTableCellElementImpl(override val tagName: String): HTMLTableCellElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableCellElement: HTMLElement() {
    actual open var align: String by attributeString("align")
    actual open var colSpan: Int by attributeInt("colspan")
    actual open var headers: String by attributeString("headers")
    actual open var rowSpan: Int by attributeInt("rowspan")
}
class HTMLTextAreaElementImpl(override val tagName: String): HTMLTextAreaElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTextAreaElement: HTMLElement() {
    actual open var autocomplete: String by attributeString("autocomplete")
    actual open var autofocus: Boolean by attributeBoolean("autofocus")
    actual open var cols: Int by attributeInt("cols")
    actual open var dirName: String by attributeString("dirname")
    actual open var disabled: Boolean by attributeBoolean("disabled")
    //  actual open var form: String by attributeString("form")
    actual open var inputMode: String by attributeString("inputmode")
    actual open var maxLength: Int by attributeInt("maxlength")
    actual open var minLength: Int by attributeInt("minlength")
    actual open var name: String by attributeString("name")
    actual open var placeholder: String by attributeString("placeholder")
    actual open var readOnly: Boolean by attributeBoolean("readonly")
    actual open var required: Boolean by attributeBoolean("required")
    actual open var rows: Int by attributeInt("rows")
    actual open var wrap: String by attributeString("wrap")
    actual open var value: String by textContent
}
class HTMLTimeElementImpl(override val tagName: String): HTMLTimeElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTimeElement: HTMLElement() {
    actual open var dateTime: String by attributeString("datetime")
}
class HTMLTableRowElementImpl(override val tagName: String): HTMLTableRowElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTableRowElement: HTMLElement() {
    actual open var align: String by attributeString("align")
}
class HTMLTrackElementImpl(override val tagName: String): HTMLTrackElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLTrackElement: HTMLElement() {
    actual open var default: Boolean by attributeBoolean("default")
    actual open var kind: String by attributeString("kind")
    actual open var label: String by attributeString("label")
    actual open var src: String by attributeString("src")
    actual open var srclang: String by attributeString("srclang")
}
class HTMLVideoElementImpl(override val tagName: String): HTMLVideoElement() {
    override val style: CSSStyleDeclaration = CSSStyleDeclarationImpl()
}
actual abstract class HTMLVideoElement: HTMLMediaElement() {
    //  actual open var buffered: String by attributeString("buffered")
    actual open var height: Int by attributeInt("height")
    actual open var playsInline: Boolean by attributeBoolean("playsinline")
    actual open var poster: String by attributeString("poster")
    actual open var width: Int by attributeInt("width")
}

actual inline fun createElement(tagName: String): HTMLElement = createElementByTagName(tagName)
fun createElementByTagName(tagName: String): HTMLElement {
    return when(tagName) {
        "a" -> HTMLAnchorElementImpl(tagName)
        "applet" -> HTMLAppletElementImpl(tagName)
        "area" -> HTMLAreaElementImpl(tagName)
        "audio" -> HTMLAudioElementImpl(tagName)
        "base" -> HTMLBaseElementImpl(tagName)
        "blockquote" -> HTMLQuoteElementImpl(tagName)
        "body" -> HTMLBodyElementImpl(tagName)
        "button" -> HTMLButtonElementImpl(tagName)
        "canvas" -> HTMLCanvasElementImpl(tagName)
        "caption" -> HTMLTableCaptionElementImpl(tagName)
        "col" -> HTMLTableColElementImpl(tagName)
        "data" -> HTMLDataElementImpl(tagName)
        "del" -> HTMLModElementImpl(tagName)
        "details" -> HTMLDetailsElementImpl(tagName)
        "dialog" -> HTMLDialogElementImpl(tagName)
        "embed" -> HTMLEmbedElementImpl(tagName)
        "fieldset" -> HTMLFieldSetElementImpl(tagName)
        "font" -> HTMLFontElementImpl(tagName)
        "form" -> HTMLFormElementImpl(tagName)
        "hr" -> HTMLHRElementImpl(tagName)
        "iframe" -> HTMLIFrameElementImpl(tagName)
        "img" -> HTMLImageElementImpl(tagName)
        "input" -> HTMLInputElementImpl(tagName)
        "ins" -> HTMLModElementImpl(tagName)
        "keygen" -> HTMLKeygenElementImpl(tagName)
        "label" -> HTMLLabelElementImpl(tagName)
        "li" -> HTMLLIElementImpl(tagName)
        "link" -> HTMLLinkElementImpl(tagName)
        "map" -> HTMLMapElementImpl(tagName)
        "marquee" -> HTMLMarqueeElementImpl(tagName)
        "menu" -> HTMLMenuElementImpl(tagName)
        "meta" -> HTMLMetaElementImpl(tagName)
        "meter" -> HTMLMeterElementImpl(tagName)
        "object" -> HTMLObjectElementImpl(tagName)
        "ol" -> HTMLOListElementImpl(tagName)
        "optgroup" -> HTMLOptGroupElementImpl(tagName)
        "option" -> HTMLOptionElementImpl(tagName)
        "output" -> HTMLOutputElementImpl(tagName)
        "param" -> HTMLParamElementImpl(tagName)
        "progress" -> HTMLProgressElementImpl(tagName)
        "q" -> HTMLQuoteElementImpl(tagName)
        "script" -> HTMLScriptElementImpl(tagName)
        "select" -> HTMLSelectElementImpl(tagName)
        "source" -> HTMLSourceElementImpl(tagName)
        "style" -> HTMLStyleElementImpl(tagName)
        "table" -> HTMLTableElementImpl(tagName)
        "tbody" -> HTMLTableSectionElementImpl(tagName)
        "td" -> HTMLTableCellElementImpl(tagName)
        "textarea" -> HTMLTextAreaElementImpl(tagName)
        "tfoot" -> HTMLTableSectionElementImpl(tagName)
        "th" -> HTMLTableCellElementImpl(tagName)
        "thead" -> HTMLTableSectionElementImpl(tagName)
        "time" -> HTMLTimeElementImpl(tagName)
        "tr" -> HTMLTableRowElementImpl(tagName)
        "track" -> HTMLTrackElementImpl(tagName)
        "video" -> HTMLVideoElementImpl(tagName)
        else -> HTMLElementImpl(tagName)
    }
}

actual inline fun HTMLElement.getChild(index: Int): Node? = this.children.getOrNull(index)
actual inline fun HTMLElement.addOnClick(crossinline action: () -> Unit) = Unit
actual inline fun HTMLInputElement.addOnValue(crossinline action: (string: String) -> Unit) = Unit
actual inline fun HTMLInputElement.addOnChange(crossinline action: (on: Boolean) -> Unit) = Unit
actual inline fun HTMLTextAreaElement.addOnValue(crossinline action: (string: String) -> Unit) = Unit