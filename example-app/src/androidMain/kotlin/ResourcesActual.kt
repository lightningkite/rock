package com.lightningkite.mppexampleapp

import androidx.core.content.res.ResourcesCompat
import com.lightningkite.rock.models.*
import com.lightningkite.rock.views.AndroidAppContext

actual object Resources {
    actual val fontsMontserrat: Font = ResourcesCompat.getFont(AndroidAppContext.applicationCtx, R.font.fonts_montserrat)!!
    actual val imagesMammoth: ImageResource = ImageResource(R.drawable.images_mammoth)
    actual val imagesSolera: ImageResource = ImageResource(R.drawable.images_solera)
}