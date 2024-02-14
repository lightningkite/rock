package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.*
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.docs.VideoScreen
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.docs.ViewPagerScreen
import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.*
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.icon
import com.lightningkite.rock.views.minus
import com.lightningkite.rock.views.reactiveScope

@Routable("/")
object RootScreen : RockScreen {
    override fun ViewWriter.render() {
        scrolls - col {
            col {
                h1 { content = "Beautiful by default." }
                separator()
                text {
                    content =
                        "In Rock, styling is beautiful without effort.  No styling or CSS is required to get beautiful layouts.  Just how it should be."
                }
                text {
                    content = "Take a look below at some examples."
                }
                text {
                    content =
                        "Note the magnifying glass in the top right corner - clicking it will open the source of the current screen on GitHub!"
                }
            }
            col {

                fun ViewWriter.linkScreen(screen: RockScreen) = link {
                    to = screen
                    row {
                        text {
                            ::content{ screen.title.await() }
//                            content  = screen.toString()
                        } in weight(1f)
                        icon(Icon.chevronRight, "Open")
                    }
                } in card

                linkScreen(CameraScreen)
                linkScreen(PlatformSpecificScreen)
                linkScreen(VideoScreen)
                linkScreen(ViewPagerScreen)
                linkScreen(TwoPaneTestScreen)
                linkScreen(ThemesScreen)
                linkScreen(ControlsScreen)
                linkScreen(FormsScreen)
                linkScreen(NavigationScreen)
                linkScreen(LayoutExamplesScreen)
                linkScreen(VectorsTestScreen)
                linkScreen(SampleLogInScreen)
                linkScreen(DataLoadingExampleScreen)
                linkScreen(LoadAnimationTestScreen)
                linkScreen(WebSocketScreen)
                linkScreen(CanvasSampleScreen)
                linkScreen(PongSampleScreen)
                linkScreen(ReactivityScreen)
                linkScreen(DialogSamplesScreen)
                linkScreen(ExternalServicesScreen)
                linkScreen(FullExampleScreen())
                linkScreen(RecyclerViewScreen)
                linkScreen(ArgumentsExampleScreen("test-id").also { it.toAdd.value = "Preset" })

                button {
                    text { content = "GC" }
                    onClick { gc() }
                }

                calculationContext.onRemove {
                    println("Left root screen")
                }
            }
        }
    }
}
