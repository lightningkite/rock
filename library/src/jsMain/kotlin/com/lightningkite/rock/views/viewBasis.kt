package com.lightningkite.rock.views

import com.lightningkite.rock.Cancellable
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.reactive.*
import kotlinx.browser.document
import org.w3c.dom.*

inline fun <T : HTMLElement> ViewWriter.containsNext(name: String, noinline setup: T.() -> Unit): ViewWrapper =
    wrapNext<T>(document.createElement(name) as T, setup)

inline fun <T : HTMLElement> ViewWriter.element(name: String, noinline setup: T.() -> Unit) =
    element(document.createElement(name) as T, setup)