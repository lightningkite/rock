package com.lightningkite.rock.views.direct

import android.content.Intent
import android.net.Uri
import android.widget.FrameLayout
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import java.util.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = FrameLayout

actual var ExternalLink.to: String
    get() {
        return (native.tag as? Pair<UUID, String>)?.second ?: ""
    }
    set(value) {
        native.tag = UUID.randomUUID() to value
        native.setOnClickListener { view ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                categories.add(Intent.CATEGORY_APP_BROWSER)
                data = Uri.parse(value)
            }
            view.context.packageManager.resolveActivity(intent, intent.flags)?.let {
                view.context.startActivity(intent)
            }
        }
    }
actual var ExternalLink.newTab: Boolean
    get() {
        return native.tag as? Boolean ?: false
    }
    set(value) {
        native.tag = value
    }

@ViewDsl
actual fun ViewWriter.externalLink(setup: ExternalLink.() -> Unit) {
    viewElement(factory = ::FrameLayout, wrapper = ::ExternalLink, setup = setup)
}