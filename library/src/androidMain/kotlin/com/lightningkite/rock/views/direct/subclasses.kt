package com.lightningkite.rock.views.direct

import android.content.Context
import android.widget.FrameLayout
import com.lightningkite.rock.navigation.RockNavigator

class LinkFrameLayout(context: Context): FrameLayout(context) {
    lateinit var navigator: RockNavigator
}