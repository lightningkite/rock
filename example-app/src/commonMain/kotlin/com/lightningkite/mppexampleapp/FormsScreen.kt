package com.lightningkite.mppexampleapp

import com.lightningkite.rock.contains
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.views.ViewContext
import com.lightningkite.rock.views.card
import com.lightningkite.rock.views.direct.*

object FormsScreen : RockScreen {
    override fun ViewContext.render() {
        col {
            h2 { content = "Forms" }
            row{
                col{
                    button { text { content = "Form" } } in card
                }
                col{
                    button { text { content = "Flow" } } in card
                }
            }
            row {
                col {
                    h3 { content = "Documentation" }
                    text {
                        content="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut mollis felis ut mi aliquet, scelerisque laoreet tortor porttitor. Aliquam erat volutpat. Etiam a mauris eu tellus hendrerit mattis. Vivamus est nibh, feugiat a orci eu, facilisis vestibulum massa. Nam tempus enim in ipsum hendrerit, tincidunt dictum tellus lacinia. Sed sit amet dui consectetur, vulputate eros vel, consectetur urna. Morbi faucibus, odio sed tristique fringilla, risus tellus fringilla sapien, sed tincidunt velit nisi eget urna. Proin ante sem, lobortis vehicula nunc vitae, pulvinar aliquam nisi. Praesent placerat finibus felis, non pulvinar augue ullamcorper sed. Praesent ornare neque augue. Fusce elementum sem cursus, ullamcorper tellus quis, faucibus nisl. Integer tincidunt dapibus ultrices. Vivamus id volutpat orci, eget ultricies orci."
                    }
                }
            } in card
        }
    }
}