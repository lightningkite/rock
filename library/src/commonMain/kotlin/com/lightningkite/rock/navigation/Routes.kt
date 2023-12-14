package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.Constant
import com.lightningkite.rock.reactive.Listenable
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.space
import kotlin.reflect.KClass

class Routes(
    val parsers: List<(UrlLikePath)->RockScreen?>,
    val renderers: Map<KClass<out RockScreen>, (RockScreen)->RouteRendered?>,
    val fallback: RockScreen
) {
    fun render(screen: RockScreen) = renderers.get(screen::class)?.invoke(screen)
    fun parse(path: UrlLikePath) = parsers.asSequence().mapNotNull { it(path) }.firstOrNull()
}

data class RouteRendered(
    val urlLikePath: UrlLikePath,
    val listenables: List<Listenable>
)

data class UrlLikePath(
    val segments: List<String>,
    val parameters: Map<String, String>
) {
    companion object {
        val EMPTY = UrlLikePath(listOf(), mapOf())
    }
}

interface RockScreen {
    val title: Readable<String> get() = Constant(this::class.toString().removePrefix("class ").removeSuffix("Screen").camelToHuman())
    fun ViewWriter.render()
    object Empty: RockScreen {
        override fun ViewWriter.render() {
            space { }
        }
    }
    open class Direct(title: String = "", val render: ViewWriter.()->Unit): RockScreen {
        override fun ViewWriter.render(): Unit = this@Direct.render(this)
        override val title: Readable<String> = Constant(title)
    }
}

private val camelRegex = Regex("([a-z])([A-Z]+)")
private fun String.camelToHuman(): String = this.replace(camelRegex) { it.groupValues[1] + " " + it.groupValues[2] }
