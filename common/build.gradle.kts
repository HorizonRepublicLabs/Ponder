plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
}

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.bundles.mixin)
    annotationProcessor(libs.mixin.extras)
    compileOnlyApi(project(":catnip:common"))
}

loom {
    clientOnlyMinecraftJar()
    accessWidenerPath = file("src/main/resources/ponder.accesswidener")
}
