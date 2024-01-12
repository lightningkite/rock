package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("sample/dialog")
object DialogSamplesScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 { content = "Dialog Samples" }

            button {
                h6("Confirm Test")
                onClick {
                    confirmDanger("Delete", body = "Delete this item?") {
                        println("Delete!")
                    }
                }
            }
            button {
                h6 { content = "Launch Test Dialog" }
                onClick {
                    navigator.dialog.navigate(DialogSampleScreen1)
                }
            }
        }
    }
}

@Routable("sample/dialog/1") object DialogSampleScreen1: RockScreen {
    override fun ViewWriter.render() {
        stack {
            centered - card - col {
                h2 { content = "Sample Dialog" }
                text { content = "This is a sample dialog." }
                row {
                    button {
                        text { content = "OK" }
                        onClick { navigator.dismiss() }
                    } in card
                }
            }
        }
    }
}