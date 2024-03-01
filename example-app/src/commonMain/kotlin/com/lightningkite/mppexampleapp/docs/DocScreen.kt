package com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

interface DocScreen: RockScreen {
    val covers: List<String>
}

val ViewWriter.code: ViewWrapper get() = tweakTheme {
    it.copy(body = FontAndStyle(font = systemDefaultFixedWidthFont))
}

fun ViewWriter.example(
    codeText: String,
    action: ViewWriter.()->Unit
) {
    card - row {
        expanding - scrollsHorizontally - code - text(codeText)
        separator()
        expanding - action()
    }
}

fun ViewWriter.article(
    setup: ContainingView.()->Unit
) {
    stack {
        gravity(Align.Center, Align.Stretch) - sizedBox(SizeConstraints(width = 80.rem)) - scrolls - col {
            setup()
        }
    }
}