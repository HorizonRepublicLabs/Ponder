plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.configure.platform)
}

loom {
    clientOnlyMinecraftJar()

    runs {
        named("server") {
            runDir = "run/server"
        }

        configureEach {
            ideConfigGenerated(true)
            vmArg("-Dmixin.debug.export=true")
            vmArg("-XX:+IgnoreUnrecognizedVMOptions")
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.bundles.fabric)
}
