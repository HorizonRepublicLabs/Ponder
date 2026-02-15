import net.createmod.pondergradle.nullability.PackageInfosExtension

// convention plugin to apply to platform subprojects.

plugins {
    `java-library`
    `maven-publish`
}

// this has to be separate for some reason
plugins.apply("net.createmod.ponder.gradle")
plugins.apply("setup-git-hash")

// set up name and group based on parent project, ex. net.createmod.ponder:ponder-fabric
val modName: String = parent!!.name
base.archivesName = "$modName-$name"
group = "net.createmod.$modName"

// keep version synchronized with the root project
version = rootProject.version

java {
    withSourcesJar()
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<Jar> {
    // copy the license file into every built jar
    from(rootProject.file("LICENSE"))
}

val libs: VersionCatalog = versionCatalogs.named("libs")
fun versionOf(name: String): Any {
    // thank you fabric loader for mangling all non-release versions
    // FIXME remove when on full 26.1
    if (name == "minecraft" && project.name == "fabric")
        return "26.1-alpha.2";

    return libs.findVersion(name).get()
}

// expand placeholders in metadata files
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

// generate package-infos for the main sourceSet
extensions.getByType<PackageInfosExtension>().sources(sourceSets.main.get())

// FIXME: temporary hack - disable everything config-related
tasks.withType<JavaCompile> {
    exclude("**/catnip/config")
    exclude("**/ConfigCommand.java")
    exclude("**/ConfigPathArgument.java")
    exclude("**/CClient.java")
    exclude("**/PonderConfig.java")
}

if (name == "common") {
    plugins.apply("provide-common")
} else {
    plugins.apply("consume-common")
}
