package com.lightningkite.rock.navigation

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
    fun ViewContext.render()
    object Empty: RockScreen {
        override fun ViewContext.render() {
            space { }
        }
    }
}

class RedirectException(val screen: RockScreen) : Exception()