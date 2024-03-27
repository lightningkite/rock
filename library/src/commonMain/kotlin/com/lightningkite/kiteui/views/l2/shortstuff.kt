package com.lightningkite.kiteui.views.l2

import com.lightningkite.kiteui.models.Icon
import com.lightningkite.kiteui.reactive.Readable
import com.lightningkite.kiteui.reactive.await
import com.lightningkite.kiteui.views.*
import com.lightningkite.kiteui.views.direct.*

@ViewDsl
fun ViewWriter.icon(icon: Icon, description: String, setup: IconView.()->Unit = {}) {
    icon {
        source = icon
        this.description = description
        setup(this)
    }
}

@ViewDsl
fun ViewWriter.lazyExpanding(visible: Readable<Boolean>, sub: ViewWriter.()->Unit) {
    col {
        val split = split()
        var noViewCreated = true
        var view: NView? = null
        reactiveScope {
            val v = visible.await()
            if(v) {
                if(noViewCreated) {
                    noViewCreated = false
                    native.withoutAnimation {
                        split.sub()
                        view = native.listNViews()[0]
                        view?.exists = false
                    }
                    view?.exists = true
                } else {
                    view?.exists = true
                }
            } else {
                view?.exists = false
            }
        }
    }
}