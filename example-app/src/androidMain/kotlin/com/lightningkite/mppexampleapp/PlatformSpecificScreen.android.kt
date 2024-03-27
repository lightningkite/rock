package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.docs.code
import com.lightningkite.mppexampleapp.docs.example
import com.lightningkite.kiteui.models.px
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*

actual fun ViewWriter.platformSpecific() {
    stack {
        spacing = 0.px
        important - space()
        important - space()
    }
}