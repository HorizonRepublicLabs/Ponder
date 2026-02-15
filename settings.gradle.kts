pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "ponder"

for (platform in listOf("common", "fabric", "neoforge")) {
    include(platform)

    include(":catnip:$platform")
    project(":catnip:$platform").projectDir = file("catnip/$platform")
}

includeBuild("build-logic")
