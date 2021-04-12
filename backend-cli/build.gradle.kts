plugins {
    kotlin("jvm")
}

var stackzyVersion : String by rootProject.extra
group = "com.theapache64"
version = stackzyVersion

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":data"))
}
