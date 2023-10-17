import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
//    kotlin("native.cocoapods")
//    id("com.android.library")
}

group = "com.lightningkite"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
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
            }
        }
//        val commonJvmMain by creating {
//            dependsOn(commonMain)
//        }
//        val androidMain by getting {
//            dependsOn(commonJvmMain)
//        }
//        val jvmMain by getting {
//            dependsOn(commonJvmMain)
//        }
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

//android {
//    namespace = "$group.mppexample"
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    compileSdk = 31
//
//    defaultConfig {
//        minSdk = 21
//    }
//    compileOptions {
//        // Flag to enable support for the new language APIs
//        isCoreLibraryDesugaringEnabled = true
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    dependencies {
//        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
//    }
//}
