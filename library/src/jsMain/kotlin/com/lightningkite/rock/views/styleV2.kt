package com.lightningkite.rock.views

import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.LinearGradient
import com.lightningkite.rock.models.RadialGradient
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.reactiveScope
import org.w3c.dom.asList


fun ViewWriter.handleTheme(
    view: NView,
    viewDraws: Boolean = true,
    isControl: Boolean = false,
) {
    val transition = transitionNextView
    transitionNextView = ViewWriter.TransitionNextView.No
    val currentTheme = currentTheme
    val isRoot = isRoot
    this.isRoot = false
    val changedThemes = changedThemes
    this.changedThemes = false

    view.calculationContext.reactiveScope {
        val virtualClasses = view.classList.asList().toMutableSet()
        if (viewDraws) virtualClasses.add("viewDraws")
        if (isRoot) virtualClasses.add("isRoot")

        val theme = currentTheme()

        val mightTransition = isControl || transition != ViewWriter.TransitionNextView.No
        val shouldTransition = when (transition) {
            ViewWriter.TransitionNextView.No -> false
            ViewWriter.TransitionNextView.Yes -> true
            is ViewWriter.TransitionNextView.Maybe -> transition.logic()
        }
        if (shouldTransition) virtualClasses.add("transition")
        else virtualClasses.remove("transition")
        if (mightTransition) virtualClasses.add("mightTransition")
        else virtualClasses.remove("mightTransition")

        virtualClasses.removeAll { it.startsWith("theme-") }
        virtualClasses.add(DynamicCSS.themeInteractive(theme))

        view.className = virtualClasses.joinToString(" ")
    }
}
