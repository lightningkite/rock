package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.delay
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.*
import kotlin.random.Random

fun ViewWriter.iosTest() {
    val theme = M3Theme(
        foreground = Color.white,
        backgroundAdjust = 0.5f,
        primary = Color.blue.toHSV().copy(saturation = 0.5f, value = 0.5f).toRGB(),
        secondary = Color.green.toHSV().copy(saturation = 0.5f, value = 0.5f).toRGB(),
    )
    col {
        button { text { content = "Test" } }
    } in setTheme {
        theme
    }
}
