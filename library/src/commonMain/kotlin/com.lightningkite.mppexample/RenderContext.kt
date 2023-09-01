package com.lightningkite.mppexample

open class RenderContext {
    companion object {
        val None = NoRenderContext()
    }
}

class NoRenderContext : RenderContext()

data class ButtonRenderContext(
    val size: ButtonSize
) : RenderContext()

val ViewContext.renderContextStack by viewContextAddon(arrayListOf<RenderContext>())
val ViewContext.renderContext get() = renderContextStack.lastOrNull() ?: RenderContext.None
inline fun ViewContext.withRenderContext(renderContext: RenderContext, action: () -> Unit) {
    renderContextStack.add(renderContext)
    try {
        action()
    } finally {
        renderContextStack.removeLast()
    }
}
