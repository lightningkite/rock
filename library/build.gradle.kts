import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import com.lightningkite.deployhelpers.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

group = "com.lightningkite.rock"
repositories {
    mavenCentral()
}

val ktorVersion = "2.3.7"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        dependencies {
            implementation("androidx.transition:transition:1.4.1")
            implementation("androidx.cardview:cardview:1.0.0")
            implementation("com.jakewharton.timber:timber:5.0.1")
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
            implementation("io.ktor:ktor-client-websockets:$ktorVersion")
        }
    }
    jvm()
//    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "library"
//        }
//    }
    js(IR) {
        browser()
    }

    sourceSets {
        applyDefaultHierarchyTemplate()

        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.6.2")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.recyclerview:recyclerview:1.3.2")
            }
        }

        val commonHtmlMain by creating {
            dependsOn(commonMain)
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.7")
                implementation("io.ktor:ktor-client-websockets:2.3.7")
            }
        }

        val jvmMain by getting {
            dependsOn(commonHtmlMain)
            dependencies {
                api("org.apache.commons:commons-lang3:3.12.0")
            }
        }
        val jsMain by getting {
            dependsOn(commonHtmlMain)
        }
    }

//    cocoapods {
//        summary = "Rock"
//        homepage = "https://github.com/lightningkite/rock"
//        ios.deploymentTarget = "12.0"
//
//        pod("FlexLayout") { version = "2.0.03" }
//        pod("PinLayout") {
//            version = "1.10.5"
//            extraOpts += listOf("-compiler-option", "-fmodules")
//        }
//    }
}

//tasks.withType<org.jetbrains.kotlin.gradle.targets.native.tasks.PodGenTask>().configureEach {
//    doLast {
//        podfile.get().appendText("\nENV['SWIFT_VERSION'] = '5'")
//    }
//}

kotlin {
    targets
        .matching { it is org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget }
        .configureEach {
            this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

            compilations.getByName("main") {
                val objcAddition by cinterops.creating {
                    defFile(project.file("src/iosMain/def/objcAddition.def"))
                }
                this.kotlinOptions {
//                    this.freeCompilerArgs += "-Xruntime-logs=gc=info"
//                    this.freeCompilerArgs += "-Xallocator=mimalloc"
                }
            }
        }
}

android {
    namespace = "com.lightningkite.rock"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    }
}

standardPublishing {
    name.set("Rock")
    description.set("A lightweight, highly opinionated UI framework for Kotlin Multiplatform")
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
        developer(
            id = "shanelk",
            name = "Shane Thompson",
            email = "shane@lightningkite.com",
        )
    }
}
