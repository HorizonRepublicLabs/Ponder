configurations {
    consumable("commonMainJava")
    consumable("commonMainResources")
    consumable("commonClientJava")
    consumable("commonClientResources")
    consumable("commonClientOutput")
}

val sourceSets = extensions.getByType<SourceSetContainer>()

artifacts {
    sourceSets["main"].run {
        java.sourceDirectories.forEach { add("commonMainJava", it) }
        resources.sourceDirectories.forEach { add("commonMainResources", it) }
    }

    sourceSets["client"].run {
        java.sourceDirectories.forEach { add("commonClientJava", it) }
        resources.sourceDirectories.forEach { add("commonClientResources", it) }
        // The output has to name the task that builds it, or a consumer
        // compiling against it races the compile that produces it.
        output.forEach { file -> add("commonClientOutput", file) { builtBy(output) } }
    }
}
