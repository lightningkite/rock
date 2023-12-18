package com.lightningkite.rock.navigation

actual class PlatformNavigator actual constructor(routes: Routes) : RockNavigator by LocalNavigator(routes)