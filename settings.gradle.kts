enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        mavenLocal()
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
        id("com.lightningkite.rock") version "main-SNAPSHOT"
    }
}

rootProject.name = "rock"

include(":library")
include(":example-app")
include(":processor")
include(":gradle-plugin")
include(":example-app-android")
