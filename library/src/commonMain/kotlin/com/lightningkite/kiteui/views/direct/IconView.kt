package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Icon
import com.lightningkite.kiteui.models.ImageScaleType
import com.lightningkite.kiteui.models.ImageSource
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
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
