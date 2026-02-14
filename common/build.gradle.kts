plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
    alias(libs.plugins.ponder.gradle)
}

defaultPackageInfos {
    sources(sourceSets.main.get())
}

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.bundles.mixin)
    annotationProcessor(libs.mixin.extras)
}

loom {
    clientOnlyMinecraftJar()
    accessWidenerPath = file("src/main/resources/ponder.accesswidener")
}

sourceSets.main {
    java {
        exclude("**/catnip/config")
        exclude("**/ConfigCommand.java")
        exclude("**/ConfigPathArgument.java")
        exclude("**/CClient.java")
        exclude("**/PonderConfig.java")
    }
}

configurations {
    consumable("commonJava")
    consumable("commonResources")
}

artifacts {
    sourceSets.main.get().run {
        java.sourceDirectories.forEach { add("commonJava", it) }
        resources.sourceDirectories.forEach { add("commonResources", it) }
    }
}
