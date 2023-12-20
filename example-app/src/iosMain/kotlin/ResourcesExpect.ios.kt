package com.lightningkite.mppexampleapp

import com.lightningkite.rock.models.Font
import com.lightningkite.rock.models.ImageResource
import com.lightningkite.rock.models.systemDefaultFont

actual object Resources {
    actual val fontsMontserrat: Font
        get() = systemDefaultFont
    actual val imagesMammoth: ImageResource
        get() = ImageResource("")
    actual val imagesSolera: ImageResource
        get() = ImageResource("")
}