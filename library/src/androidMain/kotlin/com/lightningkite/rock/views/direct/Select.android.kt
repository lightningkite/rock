package com.lightningkite.rock.views.direct

import android.content.Context
import android.widget.TextView
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import com.lightningkite.rock.reactive.Readable
import com.lightningkite.rock.reactive.Writable
import com.lightningkite.rock.reactive.await
import java.util.*
import com.lightningkite.rock.models.rem
import com.lightningkite.rock.views.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class NSelect(context: Context): AppCompatSpinner(context) {
    lateinit var viewWriter: ViewWriter
}

actual fun <T> Select.bind(
    edits: Writable<T>,
    data: Readable<List<T>>,
    render: (T) -> String
) {
    var suppressChange = false
    var list: List<T> = listOf()
    val adapter = object: BaseAdapter() {
        override fun getCount(): Int = list.size
        override fun getItem(position: Int): Any? = list.get(position)
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(convertView != null) {
                (convertView as TextView).text = render(list[position])
                return convertView
            } else {
                with(native.viewWriter) {
                    withDefaultPadding - text {
                        content = render(list[position])
                    }
                }
                return native.viewWriter.rootCreated!!.also {
                    it.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }
    }
    native.adapter = adapter
    native.minimumHeight = 4.rem.value.toInt()
    native.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if(!suppressChange) {
                launch {
                    suppressChange = true
                    edits set list[position]
                    suppressChange = false
                }
            }
        }
    }
    reactiveScope {
        list = data.await()
        adapter.notifyDataSetChanged()
        val currentlySelected = edits.awaitRaw()  // explicitly DO NOT LISTEN.  We'll handle it on the other side.
        val index = list.indexOf(currentlySelected)
        if(index != -1 && !suppressChange) {
            suppressChange = true
            native.setSelection(index)
            suppressChange = false
        }
    }
    reactiveScope {
        val currentlySelected = edits.await()
        val index = list.indexOf(currentlySelected)
        if(index != -1 && !suppressChange) {
            suppressChange = true
            native.setSelection(index)
            suppressChange = false
        }
    }
}

@ViewDsl
actual fun ViewWriter.select(setup: Select.() -> Unit) {
    return viewElement(factory = ::NSelect, wrapper = ::Select, setup = {
        handleThemeControl(native, viewLoads = true, background = {
            native.setPopupBackgroundDrawable(it.backgroundDrawable(true))
        }) {
            native.viewWriter = newViews()
            setup(this)
        }
    })
}


data class SpinnerTextStyle(
    val textColor: Int,
    val textSize: Float,
    val paddingLeft: Int,
    val paddingTop: Int,
    val paddingRight: Int,
    val paddingBottom: Int,
    val typeface: Typeface?,
    val letterSpacing: Float?,
    val lineSpacingMultiplier: Float?,
    val gravity: Int,
) {
    fun apply(to: TextView) {
        to.gravity = gravity
        to.setTextColor(textColor)
        to.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        to.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        letterSpacing?.let { to.letterSpacing = it }
        lineSpacingMultiplier?.let { to.setLineSpacing(0f, it) }
        typeface?.let { to.typeface = it }
    }
}

var Spinner.spinnerTextStyle: SpinnerTextStyle?
    get() = spinnerTextStyleMap[this]
    set(value) {
        spinnerTextStyleMap[this] = value
    }
private val spinnerTextStyleMap = WeakHashMap<Spinner, SpinnerTextStyle>()
