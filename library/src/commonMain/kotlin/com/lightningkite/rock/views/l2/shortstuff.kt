package com.lightningkite.rock.views.l2

import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.Image
import com.lightningkite.rock.views.direct.description
import com.lightningkite.rock.views.direct.image
import com.lightningkite.rock.views.direct.source

@ViewDsl
fun ViewWriter.icon(icon: Icon, description: String, setup: Image.()->Unit = {}) {
    image {
        val currentTheme = currentTheme
        ::source { icon.toImageSource(currentTheme().foreground) }
        this.description = description
        setup(this)
    }
}