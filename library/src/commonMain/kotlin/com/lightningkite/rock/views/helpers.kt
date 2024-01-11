package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewModifierDsl3
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*

@ViewModifierDsl3 val ViewWriter.centered get() = gravity(Align.Center, Align.Center)
@ViewModifierDsl3 val ViewWriter.expanding get() = weight(1f)
@ViewModifierDsl3 operator fun ViewWrapper.minus(unit: Unit) = Unit
@ViewModifierDsl3 operator fun ViewWrapper.minus(wrapper: ViewWrapper) = ViewWrapper

@ViewModifierDsl3 fun ViewWriter.maxWidthCentered(width: Dimension) = centered - sizedBox(SizeConstraints(maxWidth = width))
@ViewModifierDsl3 fun ViewWriter.maxHeight(height: Dimension) = sizedBox(SizeConstraints(maxHeight = height))

@ViewDsl
fun ViewWriter.icon(icon: suspend ()->Icon, description: String, setup: Image.()->Unit = {}) {
    image {
        val currentTheme = currentTheme
        ::source { icon().toImageSource(currentTheme().foreground) }
        this.description = description
        setup(this)
    }
}

@ViewModifierDsl3 inline fun ViewWriter.maybeThemeFromLast(crossinline calculate: suspend (Theme)-> Theme?): ViewWrapper {
    var x: Theme? = null
    transitionNextView = ViewWriter.TransitionNextView.Maybe { x != null }
    return themeModifier {
        val previous = it()
        val result = calculate(previous)
        x = result
        result ?: previous
    }
}

val Icon.Companion.empty get() = Icon(2.rem, 2.rem, 0, -960, 960, 960, listOf())