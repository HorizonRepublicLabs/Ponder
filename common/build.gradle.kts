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
    // manually use the catnip common AW here, it won't be picked up since it's not a fabric mod
    accessWidenerPath = project(":catnip:common").file("catnip.accesswidener")
}
