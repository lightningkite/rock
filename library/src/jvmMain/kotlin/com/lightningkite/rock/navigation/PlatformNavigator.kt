package com.lightningkite.rock.navigation

import com.lightningkite.rock.reactive.*

actual class PlatformNavigator actual constructor(
    override val routes: Routes
) : RockNavigator by LocalNavigator(routes)
