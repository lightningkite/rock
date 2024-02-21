package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmInline
import kotlin.contracts.*

expect class NActivityIndicator : NView

@JvmInline
value class ActivityIndicator(override val native: NActivityIndicator) : RView<NActivityIndicator>

@ViewDsl
expect fun ViewWriter.activityIndicatorActual(setup: ActivityIndicator.()->Unit = {}): Unit
@OptIn(ExperimentalContracts::class) @ViewDsl inline fun ViewWriter.activityIndicator(noinline setup: ActivityIndicator.() -> Unit = {}) { contract { callsInPlace(setup, InvocationKind.EXACTLY_ONCE) }; activityIndicatorActual(setup) }
