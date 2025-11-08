plugins {
    id("net.neoforged.moddev")
}

neoForge {
    neoFormVersion = "neo_form_version"()
    accessTransformers.from("src/main/resources/META-INF/accesstransformer.cfg")

    parchment {
        minecraftVersion = "minecraft_version"()
        mappingsVersion = "parchment_version"()
    }
}

dependencies {
    compileOnly("net.fabricmc:sponge-mixin:0.15.4+mixin.0.8.7")
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)

    compileOnly("dev.engine-room.flywheel:flywheel-common-mojmap-api-${"minecraft_version"()}:${"flywheel_version"()}")

    compileOnly(":ForgeConfigAPIPort-v21.1.3-1.21.1-Fabric-dev")
    compileOnly("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:${"forgeconfigapiport_version"()}")
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets["main"].java.sourceDirectories.singleFile)
    add("commonResources", sourceSets["main"].resources.sourceDirectories.singleFile)
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}
