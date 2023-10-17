rootProject.name = "rock"

pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
    }
}

include(":library")
include(":example-app")
include(":processor")
//include(":example-app-android")
