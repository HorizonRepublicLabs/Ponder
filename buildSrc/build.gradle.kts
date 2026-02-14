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

val asmVersion = "9.9"
val lotusVersion = "0.0.12"

dependencies {
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-tree:$asmVersion")
    implementation("org.ow2.asm:asm-util:$asmVersion")

    implementation("dev.ithundxr.lotus:lotus-gradle:$lotusVersion")
}
