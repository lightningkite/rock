package com.lightningkite.rock.views

import com.lightningkite.rock.models.ScreenTransitions
import com.lightningkite.rock.models.Theme
import com.lightningkite.rock.navigation.DummyRockNavigator
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.reactive.PersistentProperty
import com.lightningkite.rock.reactive.ReactiveScope
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
fun <T> viewContextAddon(init: T): ReadWriteProperty<ViewContext, T> = object : ReadWriteProperty<ViewContext, T> {
    override fun getValue(thisRef: ViewContext, property: KProperty<*>): T =
        thisRef.addons.getOrPut(property.name) { init } as T

    override fun setValue(thisRef: ViewContext, property: KProperty<*>, value: T) {
        thisRef.addons[property.name] = value
    }
}

var ViewContext.navigator by viewContextAddon<RockNavigator>(DummyRockNavigator())
var ViewContext.screenTransitions by viewContextAddon(ScreenTransitions.HorizontalSlide)
var ViewContext.themeStack by viewContextAddon(listOf<ReactiveScope.() -> Theme>())
val ViewContext.getTheme: ReactiveScope.() -> Theme get() = themeStack.lastOrNull() ?: throw IllegalStateException("No theme set")
inline fun ViewContext.withTheme(theme: Theme, action: () -> Unit) {
    val old = themeStack
    themeStack += { theme }
    try {
        action()
    } finally {
        themeStack = old
    }
}

inline fun <reified T> ViewContext.persistentProperty(
    key: String,
    defaultValue: T,
    overrideDebugName: String? = null
) = PersistentProperty<T>(
    key = key,
    defaultValue = defaultValue,
    overrideDebugName = overrideDebugName
)