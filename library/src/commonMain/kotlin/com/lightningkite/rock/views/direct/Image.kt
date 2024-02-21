package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.ImageScaleType
import com.lightningkite.rock.models.ImageSource
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NImage : NView

@JvmInline
value class Image(override val native: NImage) : RView<NImage>

@ViewDsl
expect fun ViewWriter.image(setup: Image.() -> Unit = {}): Unit
expect var Image.source: ImageSource?
expect var Image.scaleType: ImageScaleType
expect var Image.description: String?

@ViewDsl
expect fun ViewWriter.zoomableImage(setup: Image.() -> Unit = {}): Unit