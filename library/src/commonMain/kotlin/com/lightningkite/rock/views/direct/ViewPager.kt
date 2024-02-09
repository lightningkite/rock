package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.NView
import com.lightningkite.rock.views.RView
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import kotlin.jvm.JvmInline

expect class NViewPager : NView

@JvmInline
value class ViewPager(override val native: NViewPager) : RView<NViewPager>

@ViewDsl
expect fun ViewWriter.viewPager(setup: ViewPager.() -> Unit = {}): Unit
expect val ViewPager.index: Writable<Int>
expect fun <T> ViewPager.children(items: Readable<List<T>>, render: ViewWriter.(value: Readable<T>) -> Unit): Unit
