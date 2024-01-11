package com.lightningkite.rock.views

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.models.Action
import com.lightningkite.rock.models.Angle
import com.lightningkite.rock.reactive.CalculationContext
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import java.util.WeakHashMap

/**
 * A native view in the underlying view system.
 */
actual typealias NView = View

object AndroidAppContext {
    lateinit var applicationCtx: Context
    val res: Resources by lazy { applicationCtx.resources }
    val density: Float by lazy { res.displayMetrics.density }
    val oneRem: Float by lazy { density * 12 }
    var autoCompleteLayoutResource: Int = android.R.layout.simple_list_item_1
    var ktorClient: HttpClient = HttpClient() {
        install(WebSockets)
    }
    var activityCtxRef: WeakReference<RockActivity>? = null
    var activityCtx: RockActivity?
        get() = activityCtxRef?.get()
        set(value) { activityCtxRef = WeakReference(value) }

    fun startActivityForResult(intent: Intent, options: Bundle? = null, onResult: (Int, Intent?)->Unit) = activityCtx?.startActivityForResult(intent = intent, options = options, onResult = onResult)
    fun requestPermissions(vararg permissions: String, onResult: (RockActivity.PermissionResult)->Unit) = activityCtx?.requestPermissions(permissions = permissions, onResult = onResult)
}

private val ViewRemoveListeners = WeakHashMap<View, ArrayList<()->Unit>>()
internal val ViewAction = WeakHashMap<View, Action>()

fun View.shutdown() {
    ViewRemoveListeners[this]?.forEach { it() }
    ViewRemoveListeners.remove(this)
    ViewAction.remove(this)
}

data class NViewCalculationContext(val native: View): CalculationContext {
    override fun onRemove(action: () -> Unit) {
        ViewRemoveListeners.getOrPut(native) { ArrayList() }.add(action)
    }

    override fun notifyStart() {

    }
    override fun notifySuccess() {  }

    override fun notifyFailure(t: Throwable) {

    }
}

actual val NView.calculationContext: CalculationContext
    get() = NViewCalculationContext(this)

actual var NView.nativeRotation: Angle
    get() = Angle(rotation / Angle.DEGREES_PER_CIRCLE)
    set(value) {
        rotation = value.degrees
    }

actual var NView.opacity: Double
    get() {
        return this.alpha.toDouble()
    }

    set(value) {
        this.alpha = value.toFloat()
    }

actual var NView.exists: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

actual var NView.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

actual fun NView.clearNViews() {
    if (this !is ViewGroup) throw RuntimeException("clearChildren can only be called on Android ViewGroups")
    this.children.forEach { it.shutdown() }
    (this as ViewGroup).removeAllViews()
}

actual fun NView.addNView(child: NView) {
    if (this !is ViewGroup) throw RuntimeException("addChild can only be called on Android ViewGroups")
    (this as ViewGroup).addView(child)
}