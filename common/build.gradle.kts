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
    splitEnvironmentSourceSets()
    accessWidenerPath = file("src/main/resources/ponder.accesswidener")
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
