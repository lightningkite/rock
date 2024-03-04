package com.lightningkite.rock.views.direct

import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*

@ViewDsl
fun ViewWriter.h1(text: String) = h1 { content = text }
@ViewDsl
fun ViewWriter.h2(text: String) = h2 { content = text }
@ViewDsl
fun ViewWriter.h3(text: String) = h3 { content = text }
@ViewDsl
fun ViewWriter.h4(text: String) = h4 { content = text }
@ViewDsl
fun ViewWriter.h5(text: String) = h5 { content = text }
@ViewDsl
fun ViewWriter.h6(text: String) = h6 { content = text }
@ViewDsl
fun ViewWriter.text(text: String) = text { content = text }
@ViewDsl
fun ViewWriter.subtext(text: String) = subtext { content = text }

// TODO: Button with working indicator

fun ViewWriter.confirmDanger(
    title: String,
    body: String,
    actionName: String = "OK",
    action: suspend () -> Unit
) {
    navigator.dialog.navigate(object : RockScreen {
        override val title: Readable<String> = Constant(title)
        override fun ViewWriter.render() {
            stack {
                ignoreInteraction = true
                centered - card - col {
                    h2(title)
                    text(body)
                    row {
                        button {
                            h6("Cancel")
                            onClick {
                                navigator.dismiss()
                            }
                        }
                        button {
                            h6(actionName)
                            onClick {
                                action()
                                navigator.dismiss()
                            }
                        } in danger
                    }
                }
            }
        }
    })
}
fun ViewWriter.alert(
    title: String,
    body: String,
) {
    navigator.dialog.navigate(object : RockScreen {
        override val title: Readable<String> = Constant(title)
        override fun ViewWriter.render() {
            stack {
                ignoreInteraction = true
                centered - card - col {
//                    ignoreInteraction = false
                    h2(title)
                    text(body)
                    row {
                        button {
                            h6("OK")
                            onClick {
                                navigator.dismiss()
                            }
                        } in danger
                    }
                }
            }
        }
    })
}