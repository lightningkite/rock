package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = NativeLink

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit): Unit = element(NativeLink()) {
    handleThemeControl(this) {
        setup(ExternalLink(this))
    }
}

actual inline var ExternalLink.to: String
    get() = native.toUrl ?: ""
    set(value) {
        native.toUrl = value
    }
actual inline var ExternalLink.newTab: Boolean
    get() = native.newTab
    set(value) {
        native.newTab = value
    }