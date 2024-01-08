package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NTextView : NView

@JvmInline
value class TextView(override val native: NTextView) : RView<NTextView>

@ViewDsl
expect fun ViewWriter.h1(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.h2(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.h3(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.h4(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.h5(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.h6(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.text(setup: TextView.() -> Unit = {}): Unit
@ViewDsl
expect fun ViewWriter.subtext(setup: TextView.() -> Unit = {}): Unit
expect var TextView.content: String
expect var TextView.align: Align
expect var TextView.textSize: Dimension