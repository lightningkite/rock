package com.lightningkite.rock.navigation
import com.lightningkite.rock.reactive.Readable
import android.content.Context
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.shared
import com.lightningkite.rock.views.AndroidAppContext


actual object PlatformNavigator: RockNavigator by LocalNavigator({ PlatformNavigator.routes }, LocalNavigator({ PlatformNavigator.routes }, null)) {
    private lateinit var _routes: Routes
    actual override var routes: Routes
        get() = _routes
        set(value) {
            _routes = value
            // The navigation stack could be recreated using savedInstanceState data in RockActivity.onCreate; only
            // navigate to root if not
            if (!ready()) navigate(routes.parse(UrlLikePath(listOf(), mapOf())) ?: routes.fallback)
        }

    fun getRockScreenInstance(qualifiedName: String): RockScreen? =
        Class.forName(qualifiedName).getDeclaredField("INSTANCE").get(null) as? RockScreen
}