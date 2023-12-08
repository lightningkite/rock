package com.lightningkite.rock

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*

lateinit var comparable: KSClassDeclaration
var khrysalisUsed = false

class RouterGeneration(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger,
) : SymbolProcessor {
    var invoked = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) return listOf()
        invoked = true
        val deferredSymbols = ArrayList<KSClassDeclaration>()
//        throw Exception("resolver.getAllFiles(): ${resolver.getAllFiles().joinToString { it.filePath }}")
        val allRoutables = resolver.getAllFiles()
            .flatMap { it.declarations }
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.annotation("Routable") != null }
            .toList()
            .map { ParsedRoutable(it) }
        val fallbackRoute = resolver.getAllFiles()
            .flatMap { it.declarations }
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.annotation("FallbackRoute") != null }
            .toList()
            .singleOrNull()
            ?: resolver.getClassDeclarationByName("com.lightningkite.rock.navigation.RockScreen.Empty")!!

        val topPackage = allRoutables
            .takeIf { it.isNotEmpty() }
            ?.map { it.source.packageName.asString() }
            ?.reduce { a, b -> a.commonPrefixWith(b) }
            ?: ""

        codeGenerator.createNewFile(
//        codeGenerator.createNewFileByPath(
//            dependencies = Dependencies(
//                aggregating = true,
//                sources = *allRoutables.mapNotNull { it.source.containingFile }.toTypedArray()
//            ),
            dependencies = Dependencies.ALL_FILES,
            packageName = topPackage,
            fileName = "AutoRoutes",
//            path = allRoutables.first().source.containingFile?.filePath
        )
            .bufferedWriter()
            .use {
                with(TabAppendable(it)) {
                    appendLine("package $topPackage")
                    appendLine("")
                    appendLine("import com.lightningkite.rock.navigation.*")
                    for (r in allRoutables) appendLine("import ${r.source.qualifiedName!!.asString()}")
                    appendLine("import ${fallbackRoute.qualifiedName!!.asString()}")
                    appendLine("import com.lightningkite.uuid")
                    appendLine("")
                    appendLine("")
                    appendLine("val AutoRoutes = Routes(")
                    tab {
                        appendLine("parsers = listOf(")
                        tab {
                            for (routable in allRoutables) {
                                for (route in routable.routes) {
                                    appendLine("label@{ ")
                                    tab {
                                        appendLine("if (it.segments.size != ${route.size}) return@label null")
                                        for ((index, part) in route.withIndex()) {
                                            when (part) {
                                                is ParsedRoutable.Segment.Constant -> {
                                                    appendLine("if (it.segments[$index] != \"${part.value}\") return@label null")
                                                }

                                                else -> {}
                                            }
                                        }
                                        if(routable.source.classKind == ClassKind.OBJECT)
                                            appendLine("${routable.source.simpleName!!.asString()}")
                                        else {
                                            appendLine("${routable.source.simpleName!!.asString()}(")
                                            tab {
                                                for ((index, part) in route.withIndex()) {
                                                    when (part) {
                                                        is ParsedRoutable.Segment.Variable -> {
                                                            when (part.type.declaration.simpleName?.asString()) {
                                                                "kotlin.String" -> appendLine("${part.name} = it.segments[$index],")
                                                                "UUID" -> appendLine("${part.name} = uuid(it.segments[$index]),")
                                                                else -> appendLine("${part.name} = it.segments[$index].to${part.type.declaration.simpleName!!.asString()}(),")
                                                            }
                                                        }

                                                        else -> {}
                                                    }
                                                }
                                            }
                                            appendLine(")")
                                        }
                                        // TODO: Handle parameters
                                    }
                                    appendLine("},")
                                }
                            }
                        }
                        appendLine("),")
                        appendLine("renderers = mapOf(")
                        tab {
                            for (routable in allRoutables) {
                                val route = routable.routes.first()
                                val rendered = route.joinToString(", ") {
                                    when (it) {
                                        is ParsedRoutable.Segment.Constant -> "\"${it.value}\""
                                        is ParsedRoutable.Segment.Variable -> "it.${it.name}.toString()"
                                    }
                                }
                                appendLine("${routable.source.simpleName!!.asString()}::class to label@{")
                                tab {
                                    appendLine("if (it !is ${routable.source.simpleName!!.asString()}) return@label null")
                                    appendLine("UrlLikePath(")
                                    tab {
                                        appendLine("segments = listOf($rendered),")
                                        appendLine("parameters = mapOf()")  // TODO Handle parameters
                                    }
                                    appendLine(")")
                                }
                                appendLine("},")
                            }
                        }
                        appendLine("),")
                        if (fallbackRoute.classKind == ClassKind.OBJECT)
                            appendLine("fallback = ${fallbackRoute.simpleName!!.asString()}")
                        else
                            appendLine("fallback = ${fallbackRoute.simpleName!!.asString()}()")
                    }
                    appendLine(")")
                }
            }

        logger.info("Complete.")
        return deferredSymbols
    }
}

class MyProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return RouterGeneration(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

class ParsedRoutable(
    val source: KSClassDeclaration
) {
    sealed class Segment {
        data class Constant(val value: String) : Segment()
        data class Variable(val name: String, val type: KSType) : Segment()
    }

    val routes = source.annotations("Routable")!!.map {
        it.arguments[0].value as String
    }.map {
        it.split('/')
            .filter { it.isNotBlank() }
            .map {
                if (it.startsWith('{')) {
                    val n = it.trim('{', '}')
                    Segment.Variable(
                        name = n,
                        type = source.primaryConstructor!!.parameters
                            .find { it.name!!.asString() == n }!!
                            .type
                            .resolve()
                    )
                } else Segment.Constant(it)
            }
    }
}