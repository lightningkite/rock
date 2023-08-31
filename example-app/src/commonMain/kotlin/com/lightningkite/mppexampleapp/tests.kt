package com.lightningkite.mppexampleapp

import com.lightningkite.mppexample.*

fun ViewContext.alignTest() {
    column {
        column {
            nativeText { content = "Test" } in alignLeft() in withBackground(Background(Color.teal))
            nativeText { content = "Test" } in alignCenter() in withBackground(Background(Color.teal))
            nativeText { content = "Test" } in withBackground(Background(Color.teal)) in weight(1f)
            nativeText { content = "Test" } in alignRight() in withBackground(Background(Color.teal))
        } in withBackground(Background(Color.yellow)) in weight(1f)
        row {
            nativeText { content = "Test" } in alignTop() in withBackground(Background(Color.teal))
            nativeText { content = "Test" } in alignCenter() in withBackground(Background(Color.teal))
            nativeText { content = "Test" } in withBackground(Background(Color.teal)) in weight(1f)
            nativeText { content = "Test" } in alignBottom() in withBackground(Background(Color.teal))
        } in sizedBox(SizeConstraints(height = 100.px)) in withBackground(Background(Color.green))
    }
}

fun ViewContext.fetchTest() {
    val myOwnPage = Property("")
    launch {
        myOwnPage set fetch("/").text()
    }
    val barTextStyle = TextStyle(
        color = Color.white,
        size = 18.0,
        bold = true,
        font = Resources.titleFont
    )
    column {
        nativeText {
            content = "You can enable hot reload via adding  --continuous to your run config"
            textStyle = barTextStyle
        } in padding(1.rem) in withBackground(Background(Color.blue))
        nativeText { ::content { myOwnPage.current } }
    }
}

fun ViewContext.asyncTest() {
    val counter = Property(0)
    val prop = Property(1)
    launch {
        timeoutOrNull(5_000) {
            while (true) {
                delay(1000)
                counter.modify { it + 1 }
                async {
                    for (i in 0..5) {
                        delay(500)
                        prop.modify { it + 1 }
                    }
                }
            }
        }
        println("WAIT STOP")
    }
    column {
        row {
            nativeText { ::content { counter.current.toString() } }
            nativeText { content = " + " }
            nativeText { ::content { prop.current.toString() } }
            nativeText { content = " = " }
            nativeText { ::content { prop.current.plus(counter.current).toString() } }
        }
    }
}

fun ViewContext.simpleElementTest() {
    nativeButton {
        onClick { println("TEST") }
        nativeText { content = "PRINT TEST" }
    }
}
