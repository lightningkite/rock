plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lightningkite.rockexample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lightningkite.rockexample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
//        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
//    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(project(":library"))
//    implementation(project(":example-app"))
//    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}