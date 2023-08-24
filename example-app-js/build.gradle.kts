plugins {
    kotlin("js")
}

group = "com.lightningkite"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        dependencies {
            implementation(project(":example-app"))
        }
    }
}