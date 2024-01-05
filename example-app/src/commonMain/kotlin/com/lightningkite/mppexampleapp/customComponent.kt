package com.lightningkite.mppexampleapp

import com.lightningkite.rock.views.ViewWriter

expect class CustomComponent {
    var src: String
}
expect fun ViewWriter.customComponent(setup: CustomComponent.()->Unit)

expect fun gcCheck()