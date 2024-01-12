package com.lightningkite.rock.navigation
import com.lightningkite.rock.reactive.Readable
import android.content.Context


actual class PlatformNavigator actual constructor(routes: Routes) : RockNavigator by LocalNavigator(routes, LocalNavigator(routes, null).also {
    it.stack.value = listOf()
})