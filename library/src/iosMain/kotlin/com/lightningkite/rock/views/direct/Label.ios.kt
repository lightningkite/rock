package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import platform.UIKit.UILabel
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NLabel = UIView

@ViewDsl
actual fun ViewWriter.labelActual(setup: Label.() -> Unit): Unit = col {
    subtext {  }
    setup(Label(native))
}

actual inline var Label.content: String
    get() = (native.subviews[0] as UILabel).text ?: ""
    set(value) { (native.subviews[0] as UILabel).text = value }