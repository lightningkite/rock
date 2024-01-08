package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NActivityIndicator : NView

@JvmInline
value class ActivityIndicator(override val native: NActivityIndicator) : RView<NActivityIndicator>

@ViewDsl
expect fun ViewWriter.activityIndicator(setup: ActivityIndicator.() -> Unit = {}): Unit