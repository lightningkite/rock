package com.lightningkite.rock.views.direct

import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.views.ViewDsl
import com.lightningkite.rock.views.ViewWriter
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias NRadioButton = HTMLInputElement

fun <T : HTMLElement, V> T.vprop(
    eventName: String,
    get: T.() -> V,
    set: T.(V) -> Unit
): Writable<V> {
    return object : Writable<V> {
        override suspend fun awaitRaw(): V = get(this@vprop)
        override suspend fun set(value: V) {
            set(this@vprop, value)
        }

        private var block = false

        override fun addListener(listener: () -> Unit): () -> Unit {
            val callback: (Event) -> Unit = { listener() }
            this@vprop.addEventListener(eventName, callback)
            return { this@vprop.removeEventListener(eventName, callback) }
        }

    }
}

@ViewDsl
actual fun ViewWriter.radioButton(setup: RadioButton.() -> Unit): Unit =
    themedElementClickable<HTMLInputElement>("input") {
        this.type = "radio"
        setup(RadioButton(this))
    }

actual inline var RadioButton.enabled: Boolean
    get() = !native.disabled
    set(value) {
        native.disabled = !value
    }
actual val RadioButton.checked: Writable<Boolean> get() = native.vprop("input", { checked }, { checked = it })