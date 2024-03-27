package com.lightningkite.kiteui.views.direct

import com.lightningkite.kiteui.models.Dimension
import com.lightningkite.kiteui.models.rem
import com.lightningkite.kiteui.reactive.Property
import com.lightningkite.kiteui.views.NView
import com.lightningkite.kiteui.views.RView
import com.lightningkite.kiteui.views.ViewDsl
import com.lightningkite.kiteui.views.ViewWriter
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NTwoPane : NView

@JvmInline
value class TwoPane(override val native: NTwoPane) : RView<NTwoPane>

@ViewDsl
expect fun ViewWriter.twoPane(
    setup: TwoPane.() -> Unit = {},
    leftPaneSize: Dimension = 25.rem,
    rightPaneMinSize: Dimension = 40.rem,
    right: ContainingView.() -> Unit = {},
    left: ContainingView.() -> Unit = {},
): Unit