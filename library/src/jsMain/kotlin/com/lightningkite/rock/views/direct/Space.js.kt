package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.times
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.element
import com.lightningkite.rock.views.reactiveScope
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NSpace = HTMLElement

@ViewDsl
actual inline fun ViewWriter.spaceActual(crossinline setup: Space.() -> Unit): Unit = element<NSpace>("span") {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        style.width = (getter().spacing).value
        style.height = (getter().spacing).value
    }
    setup(s)
}

actual fun ViewWriter.space(multiplier: Double, setup: Space.() -> Unit): Unit = element<NSpace>("span") {
    val getter = currentTheme
    val s = Space(this)
    s.reactiveScope {
        style.width = (getter().spacing * multiplier).value
        style.height = (getter().spacing * multiplier).value
    }
    setup(s)
}