package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.CalculationContext


/**
 * A native view in the underlying view system.
 */
expect open class NView

expect val NView.calculationContext: CalculationContext
expect var NView.nativeRotation: Angle
expect var NView.opacity: Double
expect var NView.exists: Boolean
expect var NView.visible: Boolean
expect fun NView.clearChildren()
expect fun NView.addChild(child: NView)
