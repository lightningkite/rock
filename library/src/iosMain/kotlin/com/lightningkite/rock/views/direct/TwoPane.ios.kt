package com.lightningkite.rock.views.direct

import com.lightningkite.rock.models.Dimension
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.models.SizeConstraints
import com.lightningkite.rock.models.plus
import com.lightningkite.rock.reactive.Property
import com.lightningkite.rock.reactive.WindowInfo
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.l2.icon
import platform.UIKit.UIView

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NTwoPane = UIView

@ViewDsl
actual fun ViewWriter.twoPane(
    setup: TwoPane.() -> Unit,
    leftPaneSize: Dimension,
    rightPaneMinSize: Dimension,
    right: ContainingView.() -> Unit,
    left: ContainingView.() -> Unit
) {
    suspend fun tooSmall() = WindowInfo.await().width < leftPaneSize + rightPaneMinSize
    row {
        val leftPane = Property(false)
        sizedBox(SizeConstraints(width = leftPaneSize)) - stack {
            ::exists { !tooSmall() || leftPane.await() }
            left(this)
        }
        button {
            centered - icon(Icon.menu, "Toggle Panes")
            ::exists { tooSmall() }
            onClick { leftPane set !leftPane.await() }
        }
        sizedBox(SizeConstraints(minWidth = rightPaneMinSize)) - expanding - stack {
            right(this)
        }
        setup(TwoPane(native))
    }
}