package com.lightningkite.mppexampleapp.docs

import com.lightningkite.rock.QueryParameter
import com.lightningkite.rock.Routable
import com.lightningkite.rock.models.*
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*

@Routable("docs")
object DocSearchScreen : RockScreen {

    @QueryParameter
    val query = Property<String>("")

    override fun ViewWriter.render() {
        article {
            h1("Documentation")
            text("Here you can find many helpful pages for understanding Rock and its tools.")
            row {
                centered - icon { source = Icon.search }
                expanding - textField {
                    content bind query
                }
                centered - button {
                    spacing = 0.1.rem
                    icon { source = Icon.close }
                    onClick {
                        query set ""
                    }
                }
            }
            expanding - recyclerView {
                children(shared {
                    listOf(
                        DataScreen,
                        NavigationScreen,
                        VideoElementScreen,
                        ViewPagerElementScreen,
                        IconsScreen
                    ).mapNotNull {
                        val q = query.await()
                        if(q.isBlank()) return@mapNotNull it to it.covers
                        val matchingTerms = it.covers.filter { term ->
                            q.split(' ').all { part -> term.contains(part, ignoreCase = true) }
                        }
                        if(matchingTerms.isEmpty()) return@mapNotNull null
                        it to matchingTerms
                    }
                }) {
                    card - link {
                        ::to { it.await().first }
                        col {
                            spacing = 0.25.rem
                            text { ::content { it.await().first.title.await() } }
                            subtext { ::content { it.await().second.joinToString() }}
                        }
                    }
                }
            }
        }
    }

}
