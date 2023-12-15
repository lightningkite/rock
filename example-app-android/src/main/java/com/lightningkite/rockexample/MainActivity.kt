package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Align
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.bar
import com.lightningkite.rock.views.currentTheme
import com.lightningkite.rock.views.dialog
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.exists
import com.lightningkite.rock.views.l2.AppNav
import com.lightningkite.rock.views.l2.appNav
import com.lightningkite.rock.views.l2.appNavFactory
import com.lightningkite.rock.views.l2.navigatorView
import com.lightningkite.rock.views.l2.navigatorViewDialog
import com.lightningkite.rock.views.navigator
import com.lightningkite.rock.views.themeFromLast
import com.lightningkite.rock.views.visible
import com.lightningkite.rockexample.TestScreen.render
import com.lightningkite.mppexampleapp.AutoRoutes

class MainActivity : RockActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).apply {

//            appNavFactory.value = ViewWriter::appNavBareBones

            appNav(AutoRoutes) {}
//            navigatorView(LocalNavigator(routes))
//            with(TestScreen) {
//                render()
//            }
//            with(routes.fallback) {
//                render()
//            }
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


fun ViewWriter.appNavBareBones(setup: AppNav.() -> Unit) {
    val appNav = AppNav.ByProperty()
    col {
// Nav 3 top and bottom (top)
        row {
//            setup(appNav)
            button {
//                image {
//                    val currentTheme = currentTheme
//                    ::source { Icon.arrowBack.toImageSource(currentTheme().foreground) }
//                    description = "Go Back"
//                }
//                ::visible { navigator.canGoBack.await() }
//                onClick { navigator.goBack() }
            }
            h2 { ::content.invoke {
                navigator.currentScreen.await()?.title?.await() ?: ""
            } }/* in gravity(
                Align.Center,
                Align.Center
            ) in weight(1f)*/
            row {
//                forEachUpdating(appNav.actionsProperty) {
//                    button {
//                        image {
//                            val currentTheme = currentTheme
//                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
//                            ::description { it.await().title }
//                        }
//                        onClick { it.await().onSelect() }
//                    }
//                }
            }
            ::exists { appNav.existsProperty.await() }
        } in bar in marginless
        navigatorView(navigator) in weight(1f) in marginless
        //Nav 3 - top and bottom (bottom/tabs)
        row {
//            forEachUpdating(appNav.navItemsProperty) {
//                link {
//                    ::to { it.await().destination }
//                    col {
//                        image {
//                            val currentTheme = currentTheme
//                            ::source { it.await().icon.toImageSource(currentTheme().foreground) }
//                        } in gravity(Align.Center, Align.Center)
//                        subtext { ::content { it.await().title } } in gravity(Align.Center, Align.Center)
//                    }
//                } in weight(1f) in marginless in themeFromLast { existing ->
//                    if (navigator.currentScreen.await() == it.await().destination)
//                        (existing.bar() ?: existing).down()
//                    else
//                        existing.bar() ?: existing
//                } in marginless
//            }
            ::exists { appNav.existsProperty.await() }
        } in marginless
    } in marginless
}