package com.lightningkite.rockexample

import android.os.Bundle
import android.widget.FrameLayout
import com.lightningkite.mppexampleapp.*
import com.lightningkite.rock.RockActivity
import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.*
import com.lightningkite.rock.reactive.await
import com.lightningkite.rock.reactive.invoke
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.AppNav
import com.lightningkite.rock.views.l2.navigatorView
import com.lightningkite.rock.views.*

class MainActivity : RockActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        setContentView(frame)
        ViewWriter(frame).app()
//        ViewWriter(frame).apply {
//            with(TestScreen) {
//                render()
//            }
//        }
    }
}

//@Routable("/")
//object RootScreen : RockScreen {
//    override fun ViewWriter.render() {
//        col {
//            col {
//                h1 { content = "Beautiful by default." }
//                separator()
//                text {
//                    content =
//                        "In Rock, styling is beautiful without effort.  No styling or CSS is required to get beautiful layouts.  Just how it should be."
//                }
//                text {
//                    content = "Take a look below at some examples."
//                }
//                text {
//                    content =
//                        "Note the magnifying glass in the top right corner - clicking it will open the source of the current screen on GitHub!"
//                }
//            } in withDefaultPadding
//            col {
//
//                fun ViewWriter.linkScreen(screen: RockScreen) = link {
//                    to = screen
//                    row {
//                        text { ::content{ screen.title.await() } } in weight(1f)
//                        image { source = Icon.chevronRight.toImageSource(Color.black) }
//                    }
//                } in card
//
//                linkScreen(ThemesScreen)
//                linkScreen(ControlsScreen)
//                linkScreen(FormsScreen)
//                linkScreen(NavigationScreen)
//                linkScreen(LayoutExamplesScreen)
//                linkScreen(SampleLogInScreen)
//                linkScreen(DataLoadingExampleScreen)
//                linkScreen(CanvasSampleScreen)
//                linkScreen(AnimationSampleScreen)
//                linkScreen(ReactivityScreen)
//                linkScreen(DialogSamplesScreen)
//                linkScreen(ExternalServicesScreen)
//                linkScreen(FullExampleScreen())
//                linkScreen(RecyclerViewScreen)
//                linkScreen(ArgumentsExampleScreen("test-id").also { it.toAdd.value = "Preset" })
//
//            } in withDefaultPadding
//        } in scrolls
//    }
//}

object TestScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            text {
                content = "ME TEXT"
            } in marginless

            text {
                content = "H5 HEADER"
            } in marginless

            text {
                content = "H5 HEADER"
            } in marginless

            text {
                content = "H5 HEADER"
            } in marginless

            row {
                button {
                    text { content = "Me Button" }
                } in withDefaultPadding

                button {
                    text { content = "What up" }
                } in withDefaultPadding
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