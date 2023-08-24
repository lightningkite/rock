package com.lightningkite.mppexample

import kotlinx.browser.document
import org.w3c.dom.css.CSSStyleSheet
import org.w3c.dom.css.get

actual typealias Dimension = Double

actual val DimensionZero get() = 0.0

actual typealias Font = String

actual val systemDefaultFont: Font get() = "Helvetica"

actual sealed class ImageSource actual constructor()
actual class ImageResource(val relativeUrl: String) : ImageSource()
