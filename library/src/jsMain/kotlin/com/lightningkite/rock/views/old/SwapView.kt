package com.lightningkite.rock.views.old


import com.lightningkite.rock.models.withTheme
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.reactiveScope
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.ViewDsl
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias SwapView = HTMLDivElement

@ViewDsl
actual fun ViewContext.swapView(child: Readable<ViewContext.() -> Unit>): Unit {
    val theme = this.theme

    box {
        val container = stack.last()
        val derivedContext = derive(container)
        var oldView: HTMLElement? = null

        reactiveScope {
            withTheme(theme) {
                with(child.current){
                    invoke(derivedContext)
                }
            }
            val newView = lastChild as HTMLElement? ?: return@reactiveScope
            oldView?.let { view ->
                removeChild(view)
            }
            oldView = newView
        }
    }
}
