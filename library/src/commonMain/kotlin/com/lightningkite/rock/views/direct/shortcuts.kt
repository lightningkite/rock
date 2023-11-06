package com.lightningkite.rock.views.direct

import com.lightningkite.rock.contains
import com.lightningkite.rock.launch
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*

@ViewDsl fun ViewContext.h1(text: String) = h1 { content = text }
@ViewDsl fun ViewContext.h2(text: String) = h2 { content = text }
@ViewDsl fun ViewContext.h3(text: String) = h3 { content = text }
@ViewDsl fun ViewContext.h4(text: String) = h4 { content = text }
@ViewDsl fun ViewContext.h5(text: String) = h5 { content = text }
@ViewDsl fun ViewContext.h6(text: String) = h6 { content = text }
@ViewDsl fun ViewContext.text(text: String) = text { content = text }
@ViewDsl fun ViewContext.subtext(text: String) = subtext { content = text }

// TODO: Button with working indicator

fun ViewContext.confirmDanger(title: String, body: String, actionName: String = "OK", action: suspend ()->Unit) {
    navigator.dialog.navigate(object: RockScreen {
        override val title: Readable<String> = Constant(title)
        override fun ViewContext.render() {
            col {
                h2(title)
                text(body)
                row {
                    button {
                        h6("Cancel")
                    }
                    button {
                        h6(actionName)
                        onClick {
                            launch(action)
                            navigator.dismiss()
                        }
                    } in danger
                }
            }
        }
    })
}