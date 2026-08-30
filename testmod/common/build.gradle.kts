plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
}

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.bundles.mixin)
    compileOnlyApi(project(":common"))
    clientCompileOnly(project(":common", configuration = "clientJar"))

    // catnip reaches ponder's common through compileOnly, which does not carry
    // to consumers, so the testmod names it too
    compileOnlyApi(project(":catnip:common"))
    clientCompileOnly(project(":catnip:common", configuration = "clientJar"))

    // catnip's gui elements draw flywheel partial models
    compileOnly(variantOf(libs.flywheel.common) { classifier("api") })
}

loom {
    // manually use the catnip common AW here, it won't be picked up since it's not a fabric mod
    accessWidenerPath = project(":catnip:common").file("catnip_common_source.accesswidener")
}
