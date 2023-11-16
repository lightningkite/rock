package com.lightningkite.mppexampleapp

import com.lightningkite.rock.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("external-services")
object ExternalServicesScreen : RockScreen {
    override val title: Readable<String>
        get() = super.title

    override fun ViewWriter.render() {
        col {
            col {
                h1 { content = "This screen demonstrates various some external access." }
//                text { content = "Note the use of the multi-layer 'Readable' in `fetching`." }
            } in withPadding

            button {
                text { content = "openTab" }
                onClick { ExternalServices.openTab("https://google.com") }
            }

            button {
                text { content = "requestFile" }
                onClick { ExternalServices.requestFile(listOf("image/*")) {} }
            }

            button {
                text { content = "requestFiles" }
                onClick { ExternalServices.requestFiles(listOf("image/*")) {} }
            }

            button {
                text { content = "requestCaptureSelf" }
                onClick { ExternalServices.requestCaptureSelf(listOf("image/*")) {} }
            }

            button {
                text { content = "requestCaptureEnvironment" }
                onClick { ExternalServices.requestCaptureEnvironment(listOf("image/*")) {} }
            }

        }
    }
}