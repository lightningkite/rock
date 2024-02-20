package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NContainingView : NView

@JvmInline
value class ContainingView(override val native: NContainingView) : RView<NContainingView>

expect var ContainingView.spacing: Dimension

@ViewDsl
expect fun ViewWriter.stack(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.col(setup: ContainingView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.row(setup: ContainingView.() -> Unit = {}): Unit