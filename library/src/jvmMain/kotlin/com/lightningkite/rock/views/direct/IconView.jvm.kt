package com.lightningkite.rock.views.direct

import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NIconView = HTMLElement

@ViewDsl
actual fun ViewWriter.iconActual(setup: IconView.() -> Unit) {
}

actual inline var IconView.source: Icon?
    get() = TODO()
    set(value) {
    }

actual inline var IconView.description: String?
    get() = TODO()
    set(value) {
    }
