package com.lightningkite.rockexample

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.lightningkite.rock.views.ViewWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(this)
        setContentView(linearLayout.apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        })
        ViewWriter(linearLayout).apply {

            element(TextView(currentView.context)) {
                updateLayoutParams {
                    if (this is ViewGroup.MarginLayoutParams) {
                        val margin = (resources.displayMetrics.density * 16).toInt()
                        this.setMargins(margin, margin, margin, margin)
                    }
                    text = "Hello World"
                }
            }
            element(TextView(currentView.context)) { text = "Welcome to Rock" }
        }
    }
}