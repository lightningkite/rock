package com.lightningkite.rock.views.l2

import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.reactive.shared
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@ViewDsl
fun ViewWriter.icon(icon: Icon, description: String, setup: Image.()->Unit = {}) {
    image {
        val currentTheme = currentTheme
        ::source { icon.toImageSource(currentTheme().foreground) }
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