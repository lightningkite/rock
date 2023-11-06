package com.lightningkite.mppexampleapp

import com.lightningkite.rock.Routable
import com.lightningkite.rock.contains
import com.lightningkite.rock.models.Icon
import com.lightningkite.rock.navigation.RockScreen
import com.lightningkite.rock.reactive.*
import com.lightningkite.rock.views.*
import com.lightningkite.rock.views.direct.*
import com.lightningkite.rock.views.l2.titledSection

@Routable("forms")
object FormsScreen : RockScreen {

    val externals = HashMap<String, Property<String>>()
    fun leafExample(propName: String): SectionLeaf {
        val prop = externals.getOrPut(propName) { Property("Test") }
        return SectionLeaf(
            title = propName,
            editor = {
                label {
                    content = propName
                    textField { content bind prop }
                }
            },
            viewer = {
                h2 { ::content { prop.current } }
            }
        )
    }

    val lp = Property(false)
    val form = Section(
        title = "Vehicle for Sale",
        subsections = {
            listOf(
                Section(
                    title = "Vehicle Information",
                    leaves = {
                        listOf(
                            leafExample("Year"),
                            leafExample("Make"),
                            leafExample("Model"),
                            leafExample("Submodel"),
                        )
                    }
                ),
                Section(
                    title = "Sale Information",
                    leaves = {
                        listOf(
                            leafExample("Mileage"),
                            leafExample("Price"),
                            leafExample("Seller"),
                        )
                    }
                ),
                Section(
                    title = "Requires Legal Paperwork",
                    leaves = {
                        listOf(
                            SectionLeaf(
                                title = "Requires Legal Paperwork",
                                editor = {
                                    row {
                                        checkbox {
                                            checked bind lp
                                        }
                                        text("Requires legal paperwork?")
                                    }
                                },
                                viewer = {}
                            )
                        ) + (if(lp.current) {
                            listOf(
                                leafExample("Paperwork Entry"),
                            )
                        } else listOf())
                    }
                )
            )
        }
    )

    override fun ViewContext.render() {
        titledSection("Form Testing") {
            renderForm(form)
            renderFormReadOnly(form)
        } in scrolls
    }
}

fun ViewContext.renderForm(section: Section) {
    titledSection(
        titleSetup = { content = section.title },
        content = {
            col {
                forEach(SharedReadable(section.subsections)) {
                    renderForm(it)
                }
            }
            col {
                forEach(SharedReadable(section.leaves)) {
                    it.editor(this)
                }
            }
        }
    )
}

fun ViewContext.renderFormReadOnly(section: Section) {
    titledSection(
        titleSetup = { content = section.title },
        content = {
            col {
                forEach(SharedReadable(section.subsections)) {
                    renderFormReadOnly(it)
                }
            }
            col {
                forEach(SharedReadable(section.leaves)) {
                    it.viewer(this)
                }
            }
        }
    )
}


data class Issue(
    val field: String,
    val summary: String,
    val description: String,
    val importance: Importance
) {
    enum class Importance {
        WARNING, ERROR
    }
}

data class Section(
    val title: String,
    val icon: Icon? = null,
    val helperText: String? = null,
    val directIssues: ReactiveScope.() -> List<Issue> = { listOf() },
    val leaves: ReactiveScope.() -> List<SectionLeaf> = { listOf() },
    val subsections: ReactiveScope.() -> List<Section> = { listOf() },
)

data class SectionLeaf(
    val title: String,
    val icon: Icon? = null,
    val helperText: String? = null,
    val directWorkSize: Int = 1,
    val directIssues: ReactiveScope.() -> List<Issue> = { listOf() },
    val editor: ViewContext.() -> Unit,
    val viewer: ViewContext.() -> Unit,
)
