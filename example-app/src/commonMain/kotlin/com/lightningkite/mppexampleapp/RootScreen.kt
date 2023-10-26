package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.Icons
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*

@Routable("/")
object RootScreen : RockScreen {
    override fun ViewContext.render() {
        col {
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
            } in withPadding
            col {

                fun ViewContext.linkScreen(screen: RockScreen) = link {
                    to = screen
                    row {
                        text { ::content{ screen.title.current } } in weight(1f)
                        image { source = Icons.chevronRight.color(Color.black) }
                    }
                } in card

                linkScreen(ThemesScreen)
                linkScreen(ControlsScreen)
                linkScreen(LayoutExamplesScreen)
                linkScreen(SampleLogInScreen)
                linkScreen(DataLoadingExampleScreen)
                linkScreen(CanvasSampleScreen)
                linkScreen(AnimationSampleScreen)
                linkScreen(ReactivityScreen)
                linkScreen(ArgumentsExampleScreen("test-id"))

            } in withPadding
        } in scrolls
    }
}