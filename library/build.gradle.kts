import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import com.lightningkite.deployhelpers.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
//    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

group = "com.lightningkite.rock"
repositories {
    mavenCentral()
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
//    jvm()
//    android()
//    ios()
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
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
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

//        val jvmMain by getting {
//            dependsOn(commonHtmlMain)
//            dependencies {
//                api("org.apache.commons:commons-lang3:3.12.0")
//            }
//        }
        val jsMain by getting {
            dependsOn(commonHtmlMain)
        }
    }

//    cocoapods {
//        val uppercaseName = project.name
//            .replace(
//                Regex("[-_](a-zA-Z)")
//            ) { it.groupValues[1].toUpperCaseAsciiOnly() }.capitalizeAsciiOnly()
//
//        // Required properties
//        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
//        version = "1.0"
//        summary = "Some description for a Kotlin/Native module"
//        homepage = "Link to a Kotlin/Native module homepage"
//        ios.deploymentTarget = "11.0"
//
//        // Optional properties
//        // Configure the Pod name here instead of changing the Gradle project name
//        name = uppercaseName
//
//        framework {
//            // Required properties
//            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
//            baseName = uppercaseName
//            embedBitcode(BitcodeEmbeddingMode.BITCODE)
////            embedBitcode(BitcodeEmbeddingMode.DISABLE)
//        }
//
////        pod("RxSwift") {
////            version = "~> 6.5.0"
////        }
//
//        // Maps custom Xcode configuration to NativeBuildType
//        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
//        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
//    }
}

android {
    namespace = "com.lightningkite.rock"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    }
}
