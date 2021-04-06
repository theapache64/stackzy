plugins {
    kotlin("jvm")
}

var stackzyVersion : String by rootProject.extra
group = "com.theapache64"
version = stackzyVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":data"))
}
