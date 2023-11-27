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
import com.lightningkite.rock.views.direct.button
import com.lightningkite.rock.views.direct.col
import com.lightningkite.rock.views.direct.content
import com.lightningkite.rock.views.direct.h1
import com.lightningkite.rock.views.direct.row
import com.lightningkite.rock.views.direct.text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).apply {
            col {
                h1 { this.native.text = "H1 Header" }
                text { this.native.text = "H5 HEADER" }
                row {
                    text { this.native.text = "Body Text" }
                    button { this.native.text = "Button" }
                }
            }
        }
    }
}