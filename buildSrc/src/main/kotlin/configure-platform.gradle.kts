plugins {
    `java-library`
    `maven-publish`
}

base.archivesName = "ponder-$name"
group = rootProject.group
version = rootProject.version

val libs: VersionCatalog = versionCatalogs.named("libs")
val gitHash: String = rootProject.ext["git_hash"] as String

fun versionOf(name: String): Any {
    // thank you fabric loader for mangling all non-release versions
    // FIXME remove when on full 26.1
    if (name == "minecraft" && project.name == "fabric")
        return "26.1-alpha.2";

    return libs.findVersion(name).orElseThrow { IllegalArgumentException("Unknown library $name") }
}

java {
    withSourcesJar()
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<Jar> {
    from(rootProject.file("LICENSE"))
}

tasks.jar {
    manifest.attributes(mapOf("Git-Hash" to "\"$gitHash\""))
}

tasks.processResources {
    val properties = mapOf(
        "version" to project.version,
        "group" to project.group,
        "minecraft_version" to versionOf("minecraft"),
        "neo_version" to versionOf("neoforge"),
        "fabric_api_version" to versionOf("fabric-api"),
        "fabric_loader_version" to versionOf("fabric-loader")
    )

    inputs.properties(properties)

    filesMatching(setOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
        expand(properties)
    }
}

publishing {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
    }

    repositories {
        maven("https://maven.createmod.net") {
            name = "create"
            credentials(PasswordCredentials::class)
        }
    }
}

tasks.withType<JavaCompile>() {
    exclude("**/catnip/config")
    exclude("**/ConfigCommand.java")
    exclude("**/ConfigPathArgument.java")
    exclude("**/CClient.java")
    exclude("**/PonderConfig.java")
}

// fun fact: if you try to do 'if (name == null) return' you get an internal kotlin compiler error.
if (name != "common") {
    val compileOnly: Configuration by configurations.getting
    val commonJava: Configuration by configurations.dependencyScope("commonJava")
    val commonResources: Configuration by configurations.dependencyScope("commonResources")

    dependencies {
        compileOnly(project(":common"))
        commonJava(project(path = ":common", configuration = "commonJava"))
        commonResources(project(path = ":common", configuration = "commonResources"))
    }

    val resolvableCommonJava: Configuration by configurations.resolvable("resolvableCommonJava") {
        extendsFrom(commonJava)
    }

    val resolvableCommonResources: Configuration by configurations.resolvable("resolvableCommonResources") {
        extendsFrom(commonResources)
    }

    tasks.compileJava {
        dependsOn(resolvableCommonJava)
        source(resolvableCommonJava)
    }

    tasks.processResources {
        dependsOn(resolvableCommonResources)
        from(resolvableCommonResources)
    }

    tasks.named<Jar>("sourcesJar") {
        dependsOn(resolvableCommonJava, resolvableCommonResources)
        from(resolvableCommonJava, resolvableCommonResources)
    }
}
