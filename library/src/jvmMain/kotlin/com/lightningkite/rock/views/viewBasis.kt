package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.dom.HTMLElement
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NView = HTMLElement

actual val NView.calculationContext: CalculationContext
    get() = TODO()

actual var NView.opacity: Double
    get() = TODO()
    set(value) { TODO()  }

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

actual var NView.spacing: Dimension
    get() = throw NotImplementedError()
    set(value) {
        TODO()
    }

actual var NView.ignoreInteraction: Boolean
    get() = TODO()
    set(value) { }

actual var NView.nativeRotation: Angle
    get() = throw NotImplementedError()
    set(value) {
        TODO()
    }

actual fun NView.clearNViews() {
    TODO()
}
actual fun NView.addNView(child: NView) {
    TODO()
}
