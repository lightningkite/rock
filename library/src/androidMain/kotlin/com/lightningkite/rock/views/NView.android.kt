package com.lightningkite.rock.views

import android.content.Context

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NContext = Context
actual val NView.nContext: NContext get() = context