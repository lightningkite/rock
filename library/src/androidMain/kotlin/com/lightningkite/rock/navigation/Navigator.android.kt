package com.lightningkite.rock.navigation

actual object PlatformNavigator: RockNavigator by LocalNavigator({ PlatformNavigator.routes }, LocalNavigator({ PlatformNavigator.routes }, null)) {
    private lateinit var _routes: Routes
    actual override var routes: Routes
        get() = _routes
        set(value) {
            _routes = value

            // The navigation stack could be recreated using savedInstanceState data in RockActivity.onCreate; only
            // navigate to root if not
            if (isStackEmpty())
                navigate(routes.parse(UrlLikePath(listOf(), mapOf())) ?: routes.fallback)
        }
}