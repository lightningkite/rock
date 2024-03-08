package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.docs.code
import com.lightningkite.mppexampleapp.docs.example
import com.lightningkite.rock.models.px
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

actual fun ViewWriter.platformSpecific() {
    stack {
        spacing = 0.px
        important - space()
        important - space()
    }
}