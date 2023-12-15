package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.appNav
import okhttp3.OkHttpClient

class MainActivity : RockActivity() {
    override val httpClientFactory: ()  -> OkHttpClient
        get() = {
            OkHttpClient()
        }

    private val routes = Routes(
        parsers = listOf test@{ path ->
            if (path.segments.isEmpty()) return@test null

            if (path.segments[0] == "Test") {
                TestScreen
            } else {
                return@test null
            }
        },
        renderers = mapOf(
            TestScreen::class to test@{
                val p = HashMap<String, String>()
                RouteRendered(
                    UrlLikePath(
                    segments = listOf("Test"),
                    parameters = p
                ), listOf()
                )
            }
        ),
        fallback = object : RockScreen {
            override fun ViewWriter.render() {
                col {
                    text("Default View")
                }
            }
        }
    )

//    override var navigator: RockNavigator = PlatformNavigator(routes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).apply {
            appNav(routes) {
                routes.render(TestScreen)
            }
        }
    }
}

object TestScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 {
                this.native.text = "H1 Header"
            } in withDefaultPadding
            text {
                this.native.text = "H5 HEADER"
            }

            localDateField {}

            localTimeField {}

            textField {
                range = 5.0..20.0
            }
        }
    }
}