package com.lightningkite.kiteui.navigation

import com.lightningkite.kiteui.reactive.Constant
import com.lightningkite.kiteui.reactive.Readable
import com.lightningkite.kiteui.views.ViewWriter
import com.lightningkite.kiteui.views.direct.space

interface KiteUiScreen {
    val title: Readable<String>
        get() = Constant(
            this::class.simpleName.toString().camelToHuman().removeSuffix(" Screen")
        )
    fun ViewWriter.render()
    object Empty: KiteUiScreen {
        override fun ViewWriter.render() {
            space { }
        }
    }
    open class Direct(title: String = "", val render: ViewWriter.()->Unit): KiteUiScreen {
        override fun ViewWriter.render(): Unit = this@Direct.render(this)
        override val title: Readable<String> = Constant(title)
    }
}
private val camelRegex = Regex("([a-z])([A-Z]+)")
private fun String.camelToHuman(): String = this.replace(camelRegex) { it.groupValues[1] + " " + it.groupValues[2] }