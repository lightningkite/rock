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
    fun leafExample(propName: String): FormLeaf {
        val prop = externals.getOrPut(propName) { Property("Test") }
        return FormLeaf(
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
    val form = FormSection(
        title = "Vehicle for Sale",
        subsections = {
            listOf(
                FormSection(
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
                FormSection(
                    title = "Sale Information",
                    leaves = {
                        listOf(
                            leafExample("Mileage"),
                            leafExample("Price"),
                            leafExample("Seller"),
                        )
                    }
                ),
                FormSection(
                    title = "Requires Legal Paperwork",
                    leaves = {
                        listOf(
                            FormLeaf(
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

fun ViewContext.renderForm(section: FormSection) {
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

fun ViewContext.renderFormReadOnly(section: FormSection) {
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


data class FormIssue(
    val field: String,
    val summary: String,
    val description: String,
    val importance: Importance
) {
    enum class Importance {
        WARNING, ERROR
    }
}

data class FormSection(
    val title: String,
    val icon: Icon? = null,
    val helperText: String? = null,
    val directIssues: ReactiveScope.() -> List<FormIssue> = { listOf() },
    val leaves: ReactiveScope.() -> List<FormLeaf> = { listOf() },
    val subsections: ReactiveScope.() -> List<FormSection> = { listOf() },
)

data class FormLeaf(
    val title: String,
    val icon: Icon? = null,
    val helperText: String? = null,
    val directWorkSize: Int = 1,
    val directIssues: ReactiveScope.() -> List<FormIssue> = { listOf() },
    val editor: ViewContext.() -> Unit,
    val viewer: ViewContext.() -> Unit,
)
