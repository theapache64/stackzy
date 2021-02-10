import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("org.jetbrains.compose") version "0.3.0-build152"
}

group = "com.theapache64"
version = "1.0.0-alpha01"

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)

    // Cyclone
    implementation("com.theapache64:cyclone:1.0.0-alpha02")

    // Dagger : A fast dependency injector for Android and Java.
    val daggerVersion = "2.31.2"
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    // Retrofit : A type-safe HTTP client for Android and Java.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Decompose : Decompose
    val decomposeVersion = "0.1.8"
    implementation("com.arkivanov.decompose:decompose-jvm:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains-jvm:$decomposeVersion")

    // Retrosheet : Turn Google Spreadsheet to JSON endpoint (for Android and JVM)
    implementation("com.theapache64:retrosheet:1.2.2")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=androidx.compose.foundation.ExperimentalFoundationApi"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Stackzy"
        }
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}