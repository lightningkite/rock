package com.lightningkite.mppexampleapp

import com.lightningkite.kiteui.models.*
import com.lightningkite.kiteui.views.AndroidAppContext

actual object Resources {
    actual val fontsMontserrat: Font = AndroidAppContext.applicationCtx.resources.getFont(R.font.fonts_montserrat)
    actual val imagesMammoth: ImageResource = ImageResource(R.drawable.images_mammoth)
    actual val imagesSolera: ImageResource = ImageResource(R.drawable.images_solera)
}