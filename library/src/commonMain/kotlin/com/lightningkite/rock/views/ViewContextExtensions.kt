package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockNavigator
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
fun <T> viewWriterAddon(init: T): ReadWriteProperty<ViewWriter, T> = object : ReadWriteProperty<ViewWriter, T> {
    override fun getValue(thisRef: ViewWriter, property: KProperty<*>): T =
        thisRef.addons.getOrPut(property.name) { init } as T

    override fun setValue(thisRef: ViewWriter, property: KProperty<*>, value: T) {
        thisRef.addons[property.name] = value
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> viewWriterAddonLateInit(): ReadWriteProperty<ViewWriter, T> = object : ReadWriteProperty<ViewWriter, T> {
    override fun getValue(thisRef: ViewWriter, property: KProperty<*>): T =
        thisRef.addons.getOrPut(property.name) { throw IllegalStateException("${property.name} has not been initialized.") } as T

    override fun setValue(thisRef: ViewWriter, property: KProperty<*>, value: T) {
        thisRef.addons[property.name] = value
    }
}

var ViewWriter.navigator by viewWriterAddonLateInit<RockNavigator>()
//var ViewContext.screenTransitions by viewContextAddon(ScreenTransitions.HorizontalSlide)

@ViewModifierDsl3 inline fun ViewWriter.withTheme(theme: Theme, action: () -> Unit) = withThemeGetter({theme}, action)
@ViewModifierDsl3 fun ViewWriter.setTheme(calculate: suspend ()-> Theme?): ViewWrapper {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    return themeModifier { calculate() ?: it() }
}
@ViewModifierDsl3 inline fun ViewWriter.themeFromLast(crossinline calculate: suspend (Theme)->Theme?): ViewWrapper {
    transitionNextView = ViewWriter.TransitionNextView.Yes
    return themeModifier { val e = it(); calculate(e) ?: e }
}
@ViewModifierDsl3 inline fun ViewWriter.tweakTheme(crossinline calculate: suspend (Theme)->Theme?): ViewWrapper {
    return themeModifier { val e = it(); calculate(e) ?: e }
}
@ViewModifierDsl3 val ViewWriter.card: ViewWrapper get() = themeFromLast { it }
@ViewModifierDsl3 val ViewWriter.dialog: ViewWrapper get() = themeFromLast { it.dialog() }
@ViewModifierDsl3 val ViewWriter.hover: ViewWrapper get() = themeFromLast { it.hover() }
@ViewModifierDsl3 val ViewWriter.down: ViewWrapper get() = themeFromLast { it.down() }
@ViewModifierDsl3 val ViewWriter.selected: ViewWrapper get() = themeFromLast { it.selected() }
@ViewModifierDsl3 val ViewWriter.unselected: ViewWrapper get() = themeFromLast { it.unselected() }
@ViewModifierDsl3 val ViewWriter.disabled: ViewWrapper get() = themeFromLast { it.disabled() }
@ViewModifierDsl3 val ViewWriter.bar: ViewWrapper get() = themeFromLast { it.bar() }
@ViewModifierDsl3 val ViewWriter.important: ViewWrapper get() = themeFromLast { it.important() }
@ViewModifierDsl3 val ViewWriter.critical: ViewWrapper get() = themeFromLast { it.critical() }
@ViewModifierDsl3 val ViewWriter.warning: ViewWrapper get() = themeFromLast { it.warning() }
@ViewModifierDsl3 val ViewWriter.danger: ViewWrapper get() = themeFromLast { it.danger() }
@ViewModifierDsl3 val ViewWriter.affirmative: ViewWrapper get() = themeFromLast { it.affirmative() }
