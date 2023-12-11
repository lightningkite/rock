package com.lightningkite.rock

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import java.io.File
import java.util.HashMap

interface RockPluginExtension {
    var packageName: String
}

class RockPlugin: Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val ext = extensions.create("rock", RockPluginExtension::class.java)
        tasks.create("commonResources", Task::class.java) {
            group = "build"
            val out = project.file("src/commonMain/kotlin/ResourcesExpect.kt")
            outputs.file(out)
            val resourceFolder = project.file("src/commonMain/resources")
            inputs.files(resourceFolder)
            doLast {
                val lines = resourceFolder.resources()
                    .entries
                    .sortedBy { it.key }
                    .joinToString("\n    ") {
                        when(val r = it.value) {
                            is Resource.Font -> "val ${r.name}: Font"
                            is Resource.Image -> "val ${r.name}: ImageResource"
                            is Resource.Binary -> "suspend fun ${r.name}(): Blob"
                            else -> ""
                        }
                    }
                out.writeText(
                    """
package ${ext.packageName}

import com.lightningkite.rock.models.*

expect object Resources {
    $lines
}
        """.trimIndent()
                )
            }
        }

        tasks.create("jsResources", Copy::class.java) {
            dependsOn("commonResources")
            group = "build"
            from("src/commonMain/resources")
            into("src/jsMain/resources/common")
            val out = project.file("src/jsMain/kotlin/ResourcesActual.kt")
            val gitIgnore = project.file("src/jsMain/resources/common/.gitignore")
            outputs.file(out)
            outputs.file(gitIgnore)
            val resourceFolder = project.file("src/commonMain/resources")
            inputs.files(resourceFolder)
            doLast {
                gitIgnore.writeText("*\n")
                val lines = resourceFolder.resources()
                    .entries
                    .sortedBy { it.key }
                    .joinToString("\n    ") {
                        when(val r = it.value) {
                            is Resource.Font -> "actual val ${r.name}: Font = Font(cssFontFamilyName = \"${r.name}\", direct = FontDirect(normal = \"/common/${r.normal}\", bold = ${r.bold?.let { "\"/common/$it\"" }}, italic = ${r.italic?.let { "\"/common/$it\"" }}, boldItalic = ${r.boldItalic?.let { "\"/common/$it\"" }}))"
                            is Resource.Image -> "actual val ${r.name}: ImageResource = ImageResource(\"/common/${r.file}\")"
                            is Resource.Binary -> "actual suspend fun ${r.name}(): Blob = fetch(\"/common/${r.file}\").blob()"
                            else -> ""
                        }
                    }
                out.writeText(
                    """
package ${ext.packageName}

import com.lightningkite.rock.models.*

actual object Resources {
    $lines
}
        """.trimIndent()
                )
            }
        }

        Unit
    }
}


sealed class Resource {
    data class Font(
        val name: String,
        val normal: File,
        val bold: File? = null,
        val italic: File? = null,
        val boldItalic: File? = null
    ) : Resource()

    data class Image(val name: String, val file: File) : Resource()
    data class Binary(val name: String, val file: File) : Resource()
}

fun File.resources(): Map<String, Resource> {
    val out = HashMap<String, Resource>()
    walkTopDown().forEach { it ->
        if(it.name.isEmpty()) return@forEach
        if(it.isDirectory) return@forEach
        val file = it.relativeTo(this)
        val name = file.path.replace('/', ' ').replace('\\', ' ')
            .substringBeforeLast('.')
            .camelCase()
        when (file.extension) {
            "png", "jpg" -> out[name] = Resource.Image(name, file)
            "otf", "ttf" -> {
                if (file.nameWithoutExtension in setOf(
                        "bold",
                        "bold-italic",
                        "italic",
                        "normal",
                    )
                ) {
                    val folderName = file.parentFile.path.replace('/', ' ').replace('\\', ' ')
                        .substringBeforeLast('.')
                        .camelCase()
                    out[folderName] = (out[folderName] as? Resource.Font)?.let {
                        it.copy(
                            normal = if (file.nameWithoutExtension == "normal") file else it.normal,
                            boldItalic = if (file.nameWithoutExtension == "bold-italic") file else it.boldItalic,
                            italic = if (file.nameWithoutExtension == "italic") file else it.italic,
                            bold = if (file.nameWithoutExtension == "bold") file else it.bold,
                        )
                    } ?: Resource.Font(
                        folderName, file,
                        boldItalic = if (file.nameWithoutExtension == "bold-italic") file else null,
                        italic = if (file.nameWithoutExtension == "italic") file else null,
                        bold = if (file.nameWithoutExtension == "bold") file else null,
                    )
                } else out[name] = Resource.Font(name, file)
            }
            "" -> {}
            else -> out[name] = Resource.Binary(name, file)
        }
    }
    return out
}
val `casing separator regex` = Regex("([-_\\s]+([A-Z]*[a-z0-9]+))|([-_\\s]*[A-Z]+)")
inline fun String.caseAlter(crossinline update: (after: String) -> String): String =
    `casing separator regex`.replace(this) {
        if(it.range.start == 0) it.value
        else update(it.value.filter { !(it == '-' || it == '_' || it.isWhitespace()) })
    }


fun String.titleCase() = caseAlter { " " + it.capitalize() }.capitalize()
fun String.spaceCase() = caseAlter { " " + it }.decapitalize()
fun String.kabobCase() = caseAlter { "-$it" }.toLowerCase()
fun String.snakeCase() = caseAlter { "_$it" }.toLowerCase()
fun String.screamingSnakeCase() = caseAlter { "_$it" }.toUpperCase()
fun String.camelCase() = caseAlter { it.capitalize() }.decapitalize()
fun String.pascalCase() = caseAlter { it.capitalize() }.capitalize()
