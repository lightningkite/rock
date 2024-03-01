package com.lightningkite.rock.views.direct

import android.animation.LayoutTransition
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.views.animationsEnabled

class LinkFrameLayout(context: Context): SlightlyModifiedFrameLayout(context) {
    lateinit var navigator: RockNavigator
}

class RockLayoutTransition: LayoutTransition() {
    override fun addChild(parent: ViewGroup?, child: View?) {
        if (animationsEnabled) super.addChild(parent, child)
    }

    override fun removeChild(parent: ViewGroup?, child: View?) {
        if (animationsEnabled) super.removeChild(parent, child)
    }

    override fun showChild(parent: ViewGroup?, child: View?, oldVisibility: Int) {
        if (animationsEnabled) super.showChild(parent, child, oldVisibility)
    }

    override fun hideChild(parent: ViewGroup?, child: View?, newVisibility: Int) {
        if (animationsEnabled) super.hideChild(parent, child, newVisibility)
    }

}