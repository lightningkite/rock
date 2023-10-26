import java.io.File
import java.net.URL

// Regenerate Icons

fun String.snakeToCamel(): String = replace(Regex("_([a-zA-Z])")) { it.groupValues[1].uppercase() }

File("/Users/jivie/Projects/rock/library/src/commonMain/kotlin/com/lightningkite/rock/models/Icons.kt").printWriter()
    .use {
        with(it) {
            appendLine("""package com.lightningkite.rock.models""")
            appendLine("""""")
            appendLine("""object Icons {""")
            fun download(url: String, name: String = url.substringAfterLast("materialsymbolsoutlined/").substringBefore('/').snakeToCamel()) {
                val fileContent = URL(url).openStream().reader().readText()
                val viewBox = fileContent.substringAfter("viewBox=\"").substringBefore("\"").split(' ')
                val pathsRegex = Regex("path d=\"([^\"]+)\"")
                val paths = pathsRegex.findAll(fileContent).map { it.groupValues[1] }.toList()
                fun String.toPath() = "ImageVector.Path(Color.black, path = \"$this\")"
                appendLine("""    val $name = ImageVector(24.px, 24.px, ${viewBox.joinToString()}, listOf(${paths.joinToString { it.toPath() }}))""")
            }
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/search/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/home/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/menu/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/close/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/settings/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/done/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/add/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/delete/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/arrow_back/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/chevron_right/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/logout/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/login/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/more_horiz/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/more_vert/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/delete_forever/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/remove/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/download/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/sync/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/block/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/sort/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/filter_list/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/star/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/person/default/24px.svg")
            download("https://fonts.gstatic.com/s/i/short-term/release/materialsymbolsoutlined/group/default/24px.svg")
            appendLine("""""")
            appendLine("""}""")
        }
    }
