package com.lightningkite.rock.views.direct

import com.lightningkite.rock.ViewWrapper
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewModifierDsl3
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.exists

@ViewModifierDsl3
actual fun ViewWriter.onlyWhen(default: Boolean, condition: suspend ()->Boolean): ViewWrapper {
    beforeNextElementSetup {
        exists = true
        ::exists.invoke(condition)
    }
    return ViewWrapper
}