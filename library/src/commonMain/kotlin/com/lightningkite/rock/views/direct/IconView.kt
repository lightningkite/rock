package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.models.ImageScaleType
import com.lightningkite.rock.models.ImageSource
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NIconView : NView

@JvmInline
value class IconView(override val native: NIconView) : RView<NIconView>

@ViewDsl
expect fun ViewWriter.iconActual(setup: IconView.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.icon(noinline setup: IconView.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; iconActual(setup) }
expect var IconView.source: Icon?
expect var IconView.description: String?
