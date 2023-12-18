package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.PlatformNavigator
import com.lightningkite.rock.navigation.RockNavigator
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.navigation.Routes
import com.lightningkite.rock.views.AndroidAppContext
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*

//actual constructor(routes: Routes)
class MainActivity : RockActivity() {
    private val routes = Routes(
        listOf(),
        renderers = mapOf(),
        fallback = object : RockScreen {
            override fun ViewWriter.render() {
                col {
                    text("Default View")
                }
            }
        }
    )

    override var navigator: RockNavigator = PlatformNavigator(routes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).apply {
            col {
                h1 { this.native.text = "H1 Header" } in withDefaultPadding
                text { this.native.text = "H5 HEADER" }
                localDateField {  }
                localTimeField {  }
                textField {
                    range = 5.0..20.0
                }
            }
        }
    }
}