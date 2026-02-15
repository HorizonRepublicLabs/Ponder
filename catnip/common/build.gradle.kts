plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
}

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.bundles.mixin)
    annotationProcessor(libs.mixin.extras)
}

loom {
    clientOnlyMinecraftJar()
    accessWidenerPath = file("src/main/resources/catnip.accesswidener")
}
