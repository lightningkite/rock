package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

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