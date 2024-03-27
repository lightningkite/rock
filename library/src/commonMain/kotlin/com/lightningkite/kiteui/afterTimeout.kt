package com.lightningkite.kiteui


internal expect fun afterTimeout(milliseconds: Long, action: ()->Unit): ()->Unit
