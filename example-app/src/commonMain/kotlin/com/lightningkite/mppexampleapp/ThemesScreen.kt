package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.M3Theme
import com.lightningkite.rock.models.MaterialLikeTheme
import com.lightningkite.rock.models.randomElevationAndCorners
import com.lightningkite.rock.models.randomTitleFontSettings
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("themes")
object ThemesScreen : RockScreen {
    override fun ViewWriter.render() {
        col {
            h1 { content = "Theme Control" }
            col {
                h2 { content = "Theme Sampling" }
                row {
                    space {} in weight(1f)
                    text { content = "Sample" } in withPadding
                    text { content = "Card" } in card
                    text { content = "Important" } in important
                    text { content = "Critical" } in critical
                    space {} in weight(1f)
                } in scrollsHorizontally
                row {
                    space {} in weight(1f)
                    text { content = "Warning" } in warning
                    text { content = "Danger" } in danger
                    text { content = "Affirmitive" } in affirmative
                    space {} in weight(1f)
                } in scrollsHorizontally
            } in card
            col {
                h2 { content = "Randomly Generate Themes" }

                button {
                    h6 { content = "M1 Light" }
                    onClick {
                        appTheme set MaterialLikeTheme.randomLight().randomElevationAndCorners()
                            .randomTitleFontSettings()
                    }
                } in card
                button {
                    h6 { content = "M1 Dark" }
                    onClick {
                        appTheme set MaterialLikeTheme.randomDark().randomElevationAndCorners()
                            .randomTitleFontSettings()
                    }
                } in card
                button {
                    h6 { content = "M3 Light" }
                    onClick {
                        appTheme set M3Theme.randomLight().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in card
                button {
                    h6 { content = "M3 Dark" }
                    onClick {
                        appTheme set M3Theme.randomDark().randomElevationAndCorners().randomTitleFontSettings()
                    }
                } in card
            } in card
        } in scrolls
    }
}