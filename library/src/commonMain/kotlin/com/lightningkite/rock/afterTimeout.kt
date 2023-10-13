package com.lightningkite.rock


internal expect fun afterTimeout(milliseconds: Long, action: ()->Unit): ()->Unit
