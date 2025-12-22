plugins {
    id("net.neoforged.moddev")
}

defaultPackageInfos {
    sources(sourceSets["main"])
}

neoForge {
    neoFormVersion = "neo_form_version"()
    accessTransformers.from("src/main/resources/META-INF/accesstransformer.cfg")

    if ("parchment_version"() != "none") {
        parchment {
            minecraftVersion = "parchment_minecraft_version"()
            mappingsVersion = "parchment_version"()
        }
    }
}

dependencies {
    compileOnly("net.fabricmc:sponge-mixin:0.15.4+mixin.0.8.7")
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)

    compileOnly("dev.engine-room.flywheel:flywheel-common-mojmap-api-${"flywheel_minecraft_version"()}:${"flywheel_version"()}")

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
    for (file in sourceSets["main"].java.sourceDirectories.files) add("commonJava", file)
    add("commonResources", sourceSets["main"].resources.sourceDirectories.singleFile)
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}
