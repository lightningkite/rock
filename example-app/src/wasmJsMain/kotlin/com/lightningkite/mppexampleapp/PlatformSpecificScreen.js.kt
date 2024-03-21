package com.lightningkite.mppexampleapp

import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import kotlinx.browser.window
import org.w3c.dom.DOMRect
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.math.abs
import kotlin.math.roundToInt

val data = Property<List<String>>((0..2500).map { "Item $it" }.toList())

actual fun ViewWriter.platformSpecific() {
}