package com.lightningkite.mppexample


internal expect fun afterTimeout(milliseconds: Long, action: ()->Unit): ()->Unit
