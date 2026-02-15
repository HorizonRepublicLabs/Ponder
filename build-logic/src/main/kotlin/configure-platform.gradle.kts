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


val authors = findProperty("authors") as String
val contributors = findProperty("contributors") as String

// expand placeholders in metadata files
tasks.processResources {
    val properties = mapOf(
        "version" to project.version,
        "group" to project.group,
        "minecraft_version" to versionOf("minecraft"),
        "neo_version" to versionOf("neoforge"),
        "fabric_api_version" to versionOf("fabric-api"),
        "fabric_loader_version" to versionOf("fabric-loader"),
        "authors" to authors,
        "contributors" to contributors,
        "authors_json" to formatForJson(authors),
        "contributors_json" to formatForJson(contributors)
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

// generate package-infos for the main (and client, if present) sourceSet(s)
extensions.getByType<PackageInfosExtension>().sources(sourceSets.named { it == "main" || it == "client" })

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

// trick to sneak multiple entries into a single placeholder in a JSON file.
// the file must be valid even with placeholders, so we can't just do something like this: [${placeholder}]
// instead, the placeholder is expected to be in a string, like this: ["${placeholder}"]
// this takes a string in the format 'a, b, c' and adds quotes, so the end result will be like this: a", "b", "c
// when filled into the placeholder, you get a valid list: ["a", "b", "c"]
fun formatForJson(entries: String): String {
    return entries.split(", ").joinToString(separator = "\", \"")
}
