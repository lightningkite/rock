package com.lightningkite.rock.navigation
import com.lightningkite.rock.reactive.Readable
import android.content.Context
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.shared
import com.lightningkite.rock.views.AndroidAppContext

private val localNavigator = LocalNavigator({ PlatformNavigator.routes }, LocalNavigator({ PlatformNavigator.routes }, null))
actual object PlatformNavigator: RockNavigator by localNavigator {
    private lateinit var _routes: Routes
    actual override var routes: Routes
        get() = _routes
        set(value) {
            _routes = value
            // The navigation stack could be recreated using savedInstanceState data in RockActivity.onCreate; only
            // navigate to root if not

            if (localNavigator.stack.value.isEmpty())
                navigate(routes.parse(UrlLikePath(listOf(), mapOf())) ?: routes.fallback)
        }

    fun saveStack(): Array<String> =
        localNavigator.stack.value.mapNotNull { it::class.qualifiedName }.toTypedArray()

    fun restoreStack(navStack: Array<String>) =
            navStack.mapNotNull(PlatformNavigator::getRockScreenInstance).forEach(PlatformNavigator::navigate)

    private fun getRockScreenInstance(qualifiedName: String): RockScreen? =
        Class.forName(qualifiedName).getDeclaredField("INSTANCE").get(null) as? RockScreen
}