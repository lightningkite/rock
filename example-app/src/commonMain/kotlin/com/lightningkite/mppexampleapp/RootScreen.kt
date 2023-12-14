package com.lightningkite.mppexampleapp

import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.FullExampleScreen
import com.lightningkite.mppexampleapp.com.lightningkite.mppexampleapp.RecyclerViewScreen
import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Color
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.ViewWriter
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*

@Routable("/")
object RootScreen : RockScreen {
    override fun ViewWriter.render() {
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
            } in withDefaultPadding
            col {

                fun ViewWriter.linkScreen(screen: RockScreen) = link {
                    to = screen
                    row {
                        text { ::content{ screen.title.await() } } in weight(1f)
                        image { source = Icon.chevronRight.toImageSource(Color.black) }
                    }
                } in card

                linkScreen(ThemesScreen)
                linkScreen(ControlsScreen)
                linkScreen(FormsScreen)
                linkScreen(NavigationScreen)
                linkScreen(LayoutExamplesScreen)
                linkScreen(SampleLogInScreen)
                linkScreen(DataLoadingExampleScreen)
                linkScreen(CanvasSampleScreen)
                linkScreen(AnimationSampleScreen)
                linkScreen(ReactivityScreen)
                linkScreen(DialogSamplesScreen)
                linkScreen(ExternalServicesScreen)
                linkScreen(FullExampleScreen())
                linkScreen(RecyclerViewScreen)
                linkScreen(ArgumentsExampleScreen("test-id").also { it.toAdd.value = "Preset" })

            } in withDefaultPadding
        } in scrolls
    }
}
