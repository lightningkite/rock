import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

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
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":library"))
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
//        // Required properties
//        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
//        version = "1.0"
//        summary = "Some description for a Kotlin/Native module"
//        homepage = "Link to a Kotlin/Native module homepage"
//        ios.deploymentTarget = "11.0"
//
//        // Optional properties
//        // Configure the Pod name here instead of changing the Gradle project name
//        name = "shared"
//
//        framework {
//            baseName = "shared"
//            export(project(":library"))
//            embedBitcode(BitcodeEmbeddingMode.BITCODE)
////            embedBitcode(BitcodeEmbeddingMode.DISABLE)
////            podfile = project.file("../example-app-ios/Podfile")
//        }
////        pod("Library") {
////            version = "1.0"
////            source = path(project.file("../library"))
////        }
//
//        // Maps custom Xcode configuration to NativeBuildType
//        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
//        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
//    }
}

//android {
//    namespace = "$group.mppexampleapp"
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
