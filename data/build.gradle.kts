plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "com.theapache64.stackzy"
version = "1.0.0-beta05"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Adam : The ADB client
    api("com.malinskiy:adam:0.2.3")

    // Moshi : A modern JSON API for Android and Java
    api("com.squareup.moshi:moshi:1.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")

    // Retrosheet : Turn Google Spreadsheet to JSO  N endpoint (for Android and JVM)
    api("com.github.theapache64:retrosheet:2.0.0-alpha02")

    // Retrofit : A type-safe HTTP client for Android and Java.
    val retrofitVersion = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")


    // Kotlinx Coroutines Core : Coroutines support libraries for Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

    // Arbor : Like Timber, just different.
    api("com.ToxicBakery.logging:arbor-jvm:1.34.109")

    val daggerVersion: String by rootProject.extra
    api("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")


    // GooglePlay API
    implementation("com.google.protobuf:protobuf-java:3.14.0")
    api("com.github.theapache64:google-play-api:0.0.7")

    // SnakeYAML : YAML 1.1 parser and emitter for Java
    implementation("org.yaml:snakeyaml:1.28")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=androidx.compose.foundation.ExperimentalFoundationApi"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=androidx.compose.ui.ExperimentalComposeUiApi"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlin.io.path.ExperimentalPathApi"
}