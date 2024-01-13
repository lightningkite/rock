package com.lightningkite.rock.views.direct

import platform.UIKit.UIView

fun UIView.printablePath() = generateSequence(this) { superview }.toList().reversed().joinToString(">") { it::class.simpleName ?: "" }
var debugMeasuring: Boolean = false