package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.Constant
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.direct.space
import kotlin.reflect.KClass

class Routes(
    val parsers: List<(UrlLikePath)->RockScreen?>,
    val renderers: Map<KClass<out RockScreen>, (RockScreen)->UrlLikePath?>,
    val fallback: RockScreen
) {
    fun render(screen: RockScreen) = renderers.get(screen::class)?.invoke(screen)
    fun parse(path: UrlLikePath) = parsers.asSequence().mapNotNull { it(path) }.firstOrNull()
}

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
    fun ViewContext.render()
    object Empty: RockScreen {
        override fun ViewContext.render() {
            space { }
        }
    }
}

private val camelRegex = Regex("([a-z])([A-Z]+)")
private fun String.camelToHuman(): String = this.replace(camelRegex) { it.groupValues[1] + " " + it.groupValues[2] }

class RedirectException(val screen: RockScreen) : Exception()