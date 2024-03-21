package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import kotlinx.browser.document
import org.w3c.dom.*

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.wrapNext(name: String, wrapperConstructor: (T)->N, noinline setup: N.() -> Unit): ViewWrapper =
    wrapNext<N>(wrapperConstructor(document.createElement(name) as T), setup)

inline fun <T : HTMLElement, N: NView2<T>> ViewWriter.element(name: String, wrapperConstructor: (T)->N, noinline setup: N.() -> Unit) =
    element(wrapperConstructor(document.createElement(name) as T), setup)

inline fun <T : HTMLElement> ViewWriter.element(name: String, noinline setup: NView3<T>.() -> Unit) =
    element(NView3(document.createElement(name) as T), setup)