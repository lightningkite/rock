package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = HTMLElement

actual val NView.calculationContext: CalculationContext
    get() = TODO()

actual var NView.exists: Boolean
    get() = throw NotImplementedError()
    set(value) {
         TODO()
    }

actual var NView.visible: Boolean
    get() = throw NotImplementedError()
    set(value) {
        TODO()
    }

actual var NView.alpha: Double
    get() = throw NotImplementedError()
    set(value) {
        TODO()
    }

actual var NView.rotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        TODO()
    }

actual fun NView.clearChildren() {
    TODO()
}
actual fun NView.addChild(child: NView) {
    TODO()
}

actual fun ViewWriter.setTheme(calculate: suspend ()-> Theme?): ViewWrapper {
    TODO()
}
