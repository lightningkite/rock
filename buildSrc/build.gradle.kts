plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
}
dependencies {
    implementation("org.apache.pdfbox:fontbox:2.0.27")
}
kotlin {
    jvmToolchain(17)
}