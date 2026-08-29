plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
}

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.bundles.mixin)
    // SuperByteBuffer's transform surface is Flywheel's Affine
    compileOnly(variantOf(libs.flywheel.common) { classifier("api") })
}

loom {
    accessWidenerPath = file("catnip_common_source.accesswidener")
}
