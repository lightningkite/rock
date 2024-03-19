package com.lightningkite.rock.views.direct

import android.content.Intent
import android.net.Uri
import android.widget.FrameLayout
import com.lightningkite.rock.launchManualCancel
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.calculationContext
import java.util.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NExternalLink = LinkFrameLayout

actual var ExternalLink.to: String
    get() {
        return (native.tag as? Pair<UUID, String>)?.second ?: ""
    }
    set(value) {
        native.tag = UUID.randomUUID() to value
        native.setOnClickListener { view ->
            val url = if(!value.startsWith("http")) "http://$value" else value
            val temp = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(temp)
            calculationContext.launchManualCancel { native.onNavigate() }
//            val intent = Intent().apply {
//                action = Intent.ACTION_VIEW
//                categories?.add(Intent.CATEGORY_APP_BROWSER)
//                data = Uri.parse(value)
//            }
//            view.context.packageManager.resolveActivity(intent, intent.flags)?.let {
//                view.context.startActivity(intent)
//            }
        }
    }
actual var ExternalLink.newTab: Boolean
    get() {
        return native.tag as? Boolean ?: false
    }
    set(value) {
        native.tag = value
    }
actual fun ExternalLink.onNavigate(action: suspend () -> Unit): Unit {
    native.onNavigate = action
}

@ViewDsl
actual inline fun ViewWriter.externalLinkActual(crossinline setup: ExternalLink.() -> Unit) {
    viewElement(factory = ::LinkFrameLayout, wrapper = ::ExternalLink) {
        handleThemeControl(native) {
            setup(this)
        }
    }
}