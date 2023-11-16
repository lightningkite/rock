package com.lightningkite.rock.views

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.models.Theme

@ViewModifierDsl3
actual fun ViewWriter.setTheme(calculate: suspend () -> Theme?): ViewWrapper {
    TODO("Not yet implemented")
}