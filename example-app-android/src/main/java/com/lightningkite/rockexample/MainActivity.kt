package com.lightningkite.rockexample

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.currentTheme
import com.lightningkite.rock.views.direct.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).apply {
            col {
                h1 { this.native.text = "H1 Header" }
                text { this.native.text = "H5 HEADER" }
                localDateField {  }
                localTimeField {  }
                textField {
                    range = 5.0..20.0
                }
//                row {
//                    text { this.native.text = "Body Text" }
//                    button { this.native.text = "Button" }
//                }
            }
        }
    }
}