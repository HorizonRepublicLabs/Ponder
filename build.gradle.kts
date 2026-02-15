plugins {
    alias(libs.plugins.setup.git.hash)
}

val buildNumber: String = providers.environmentVariable("BUILD_NUMBER").orElse("99999").get()
version = "1.0.$buildNumber+mc${libs.versions.minecraft.get()}"
