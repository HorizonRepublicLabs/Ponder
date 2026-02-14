plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("jvm") version "1.9.23"
}

repositories {
    exclusiveContent {
        forRepositories(maven("https://maven.ithundxr.dev/releases")).filter {
            includeModule("dev.ithundxr.lotus", "lotus-gradle")
        }
    }

    mavenCentral()
}

gradlePlugin {
    plugins {
        create("ponderPlugin") {
            id = "net.createmod.ponder.gradle"
            implementationClass = "net.createmod.pondergradle.PonderGradlePlugin"
        }
    }
}
