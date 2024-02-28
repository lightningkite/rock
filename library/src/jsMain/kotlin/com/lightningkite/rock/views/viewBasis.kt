package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import kotlinx.browser.document
import org.w3c.dom.*

inline fun <T : HTMLElement> ViewWriter.wrapNext(name: String, noinline setup: T.() -> Unit): ViewWrapper =
    wrapNext<T>(document.createElement(name) as T, setup)

inline fun <T : HTMLElement> ViewWriter.element(name: String, noinline setup: T.() -> Unit) =
    element(document.createElement(name) as T, setup)