package com.lightningkite.rock.views

import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.CalculationContext


/**
 * A native view in the underlying view system.
 */
expect open class NView

expect class NContext
expect val NView.nContext: NContext

expect val NView.calculationContext: CalculationContext
expect var NView.nativeRotation: Angle
expect var NView.opacity: Double
expect var NView.exists: Boolean
expect var NView.visible: Boolean
expect fun NView.clearNViews()
expect fun NView.addNView(child: NView)
expect fun NView.removeNView(child: NView)
expect fun NView.listNViews(): List<NView>
