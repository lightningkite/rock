import com.lightningkite.deployhelpers.developer
import com.lightningkite.deployhelpers.github
import com.lightningkite.deployhelpers.mit
import com.lightningkite.deployhelpers.standardPublishing

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("org.jetbrains.dokka")
}

group = "com.lightningkite.rock"

buildscript {
    repositories {
        mavenLocal()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.lightningkite:deploy-helpers:master-SNAPSHOT")
    }
}

gradlePlugin {
    plugins {
        create("lightningkite-rock") {
            id = "com.lightningkite.rock"
            implementationClass = "com.lightningkite.rock.RockPlugin"
        }
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.apache.pdfbox:fontbox:2.0.27")
}
tasks.validatePlugins {
    enableStricterValidation.set(true)
}

standardPublishing {
    name.set("Rock-Gradle-Plugin")
    description.set("Automatically create your routers")
    github("lightningkite", "rock")

    licenses {
        mit()
    }

    developers {
        developer(
            id = "LightningKiteJoseph",
            name = "Joseph Ivie",
            email = "joseph@lightningkite.com",
        )
        developer(
            id = "bjsvedin",
            name = "Brady Svedin",
            email = "brady@lightningkite.com",
        )
    }
}

tasks.create("publishLocally", Copy::class.java) {
    from(file("src/main/kotlin/RockPlugin.kt"))
    into(rootProject.file("buildSrc/src/main/kotlin"))
}