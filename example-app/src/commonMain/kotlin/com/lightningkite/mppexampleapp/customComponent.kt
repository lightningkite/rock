package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewContext

expect class CustomComponent {
    var src: String
}
expect fun ViewContext.customComponent(setup: CustomComponent.()->Unit)