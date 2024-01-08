package com.lightningkite.rock.views.direct

import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NExternalLink : NView

@JvmInline
value class ExternalLink(override val native: NExternalLink) : RView<NExternalLink>

@ViewDsl
expect fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit = {}): Unit
expect var ExternalLink.to: String
expect var ExternalLink.newTab: Boolean